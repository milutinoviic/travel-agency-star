package com.example.demo.dao.implementation;

import com.example.demo.dao.PutovanjeDAO;
import com.example.demo.model.KategorijaPutovanja;
import com.example.demo.model.PrevoznoSredstvo;
import com.example.demo.model.Putovanje;
import com.example.demo.model.SmestajnaJedinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PutovanjeDAOImpl implements PutovanjeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class PutovanjaRowCallbackHandler implements RowCallbackHandler {
        private Map<Long, Putovanje> putovanja = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            PrevoznoSredstvo prevoznoSredstvo = PrevoznoSredstvo.valueOf(rs.getString(index++));
            SmestajnaJedinica smestajnaJedinica = SmestajnaJedinica.valueOf(rs.getString(index++));
            String nazivDestinacije = rs.getString(index++);
            String slikaLokacije = rs.getString(index++);
            KategorijaPutovanja kategorijaPutovanja = KategorijaPutovanja.valueOf(rs.getString(index++));
            Timestamp datumVremePolaska = rs.getTimestamp(index++);
            Timestamp datumVremePovratka = rs.getTimestamp(index++);
            LocalDateTime datumPolaska = datumVremePolaska != null ? datumVremePolaska.toLocalDateTime() : null;
            LocalDateTime datumDolaska = datumVremePovratka != null ? datumVremePovratka.toLocalDateTime() : null;
            int brojNocenja = rs.getInt(index++);
            double cenaAranzmana = rs.getDouble(index++);
            int ukupanBrojMesta = rs.getInt(index++);
            int brojSlobodnihMesta = rs.getInt(index++);
            Double procenatPopusta = rs.getObject(index++, Double.class); // Handling the discount
            Timestamp datumPocetkaPopusta = rs.getTimestamp(index++);
            Timestamp datumKrajaPopusta = rs.getTimestamp(index++);

            LocalDateTime datumPocetkaPopustaLDT = datumPocetkaPopusta != null ? datumPocetkaPopusta.toLocalDateTime() : null;
            LocalDateTime datumKrajaPopustaLDT = datumKrajaPopusta != null ? datumKrajaPopusta.toLocalDateTime() : null;

            Putovanje putovanje = new Putovanje(id, prevoznoSredstvo, smestajnaJedinica, nazivDestinacije, slikaLokacije, kategorijaPutovanja, datumPolaska, datumDolaska, brojNocenja, cenaAranzmana, ukupanBrojMesta, brojSlobodnihMesta, procenatPopusta, datumPocetkaPopustaLDT, datumKrajaPopustaLDT);
            putovanja.put(id, putovanje);
        }

        public List<Putovanje> getPutovanja(){
            return new ArrayList<>(putovanja.values());
        }
    }

    @Override
    public Putovanje findOne(Long id) {
        String sql = "SELECT * FROM putovanje WHERE id = ?";
        PutovanjaRowCallbackHandler rowCallbackHandler = new PutovanjaRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);
        return rowCallbackHandler.getPutovanja().isEmpty() ? null : rowCallbackHandler.getPutovanja().get(0);
    }

    @Override
    public List<Putovanje> findAll() {
        String sql = "SELECT * FROM putovanje";
        PutovanjaRowCallbackHandler rowCallbackHandler = new PutovanjaRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);
        return rowCallbackHandler.getPutovanja();
    }

    @Override
    public List<Putovanje> findPromotionalOffers(int n) {
        String sql = "SELECT * FROM putovanje WHERE procenatPopusta IS NOT NULL ORDER BY datumPocetkaPopusta DESC LIMIT ?";
        PutovanjaRowCallbackHandler rowCallbackHandler = new PutovanjaRowCallbackHandler();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, n);
            return ps;
        }, rowCallbackHandler);
        return rowCallbackHandler.getPutovanja();
    }
    @Override
    public List<Putovanje> findSeasonalOffers(KategorijaPutovanja category, int n) {
        String sql = "SELECT * FROM putovanje WHERE kategorijaPutovanja = ? ORDER BY datumVremePolaska ASC LIMIT ?";
        PutovanjaRowCallbackHandler rowCallbackHandler = new PutovanjaRowCallbackHandler();
        jdbcTemplate.query(con -> {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, category.name());
            ps.setInt(2, n);
            return ps;
        }, rowCallbackHandler);
        return rowCallbackHandler.getPutovanja();
    }

    @Transactional
    @Override
    public int save(Putovanje putovanje) {
        String sql = "INSERT INTO putovanje (prevoznoSredstvo, smestajnaJedinica, nazivDestinacije, slikaLokacije, kategorijaPutovanja, datumVremePolaska, datumVremePovratka, brojNocenja, cenaAranzmana, ukupanBrojMesta, brojSlobodnihMesta, procenatPopusta, datumPocetkaPopusta, datumKrajaPopusta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatementCreator psc = connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, putovanje.getPrevoznoSredstvo().name());
            ps.setString(index++, putovanje.getSmestajnaJedinica().name());
            ps.setString(index++, putovanje.getNazivDestinacije());
            ps.setString(index++, putovanje.getSlikaLokacije());
            ps.setString(index++, putovanje.getKategorijaPutovanja().name());
            ps.setTimestamp(index++, Timestamp.valueOf(putovanje.getDatumVremePolaska()));
            ps.setTimestamp(index++, Timestamp.valueOf(putovanje.getDatumVremePovratka()));
            ps.setInt(index++, putovanje.getBrojNocenja());
            ps.setDouble(index++, putovanje.getCenaAranzmana());
            ps.setInt(index++, putovanje.getUkupanBrojMesta());
            ps.setInt(index++, putovanje.getBrojSlobodnihMesta());
            ps.setObject(index++, putovanje.getProcenatPopusta(), Types.DOUBLE);
            ps.setTimestamp(index++, putovanje.getDatumPocetkaPopusta() != null ? Timestamp.valueOf(putovanje.getDatumPocetkaPopusta()) : null);
            ps.setTimestamp(index++, putovanje.getDatumKrajaPopusta() != null ? Timestamp.valueOf(putovanje.getDatumKrajaPopusta()) : null);
            return ps;
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        return jdbcTemplate.update(psc, keyHolder);
    }

    public int update(Putovanje putovanje) {
        String sql = "UPDATE putovanje SET prevoznoSredstvo = ?, smestajnaJedinica = ?, nazivDestinacije = ?, kategorijaPutovanja = ?, datumVremePolaska = ?, datumVremePovratka = ?, brojNocenja = ?, cenaAranzmana = ?, ukupanBrojMesta = ?, brojSlobodnihMesta = ?, procenatPopusta = ?, datumPocetkaPopusta = ?, datumKrajaPopusta = ? WHERE id = ?";
        return jdbcTemplate.update(sql, putovanje.getPrevoznoSredstvo().name(), putovanje.getSmestajnaJedinica().name(), putovanje.getNazivDestinacije(), putovanje.getKategorijaPutovanja().name(), Timestamp.valueOf(putovanje.getDatumVremePolaska()), Timestamp.valueOf(putovanje.getDatumVremePovratka()), putovanje.getBrojNocenja(), putovanje.getCenaAranzmana(), putovanje.getUkupanBrojMesta(), putovanje.getBrojSlobodnihMesta(), putovanje.getProcenatPopusta(), putovanje.getDatumPocetkaPopusta() != null ? Timestamp.valueOf(putovanje.getDatumPocetkaPopusta()) : null, putovanje.getDatumKrajaPopusta() != null ? Timestamp.valueOf(putovanje.getDatumKrajaPopusta()) : null, putovanje.getId());
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM putovanje WHERE id = ?";
        return jdbcTemplate.update(sql, id) == 1;
    }

    @Override
    public List<Putovanje> search(PrevoznoSredstvo prevoznoSredstvo, String nazivDestinacije, KategorijaPutovanja kategorijaPutovanja, Integer brojNocenja, Double minCena, Double maxCena, String sortOrder) {
        StringBuilder sql = new StringBuilder("SELECT * FROM putovanje WHERE 1=1");
        if (prevoznoSredstvo != null) {
            sql.append(" AND prevoznoSredstvo = '").append(prevoznoSredstvo.name()).append("'");
        }
        if (nazivDestinacije != null && !nazivDestinacije.isEmpty()) {
            sql.append(" AND nazivDestinacije LIKE '%").append(nazivDestinacije).append("%'");
        }
        if (kategorijaPutovanja != null) {
            sql.append(" AND kategorijaPutovanja = '").append(kategorijaPutovanja.name()).append("'");
        }
        if (brojNocenja != null) {
            sql.append(" AND brojNocenja = ").append(brojNocenja);
        }
        if (minCena != null) {
            sql.append(" AND cenaAranzmana >= ").append(minCena);
        }
        if (maxCena != null) {
            sql.append(" AND cenaAranzmana <= ").append(maxCena);
        }
        if ("nazivDestinacije".equals(sortOrder)) {
            sql.append(" ORDER BY nazivDestinacije");
        } else if ("cena".equals(sortOrder)) {
            sql.append(" ORDER BY cenaAranzmana");
        } else if ("brojNocenja".equals(sortOrder)) {
            sql.append(" ORDER BY brojNocenja");
        }
        PutovanjaRowCallbackHandler rowCallbackHandler = new PutovanjaRowCallbackHandler();
        jdbcTemplate.query(sql.toString(), rowCallbackHandler);
        return rowCallbackHandler.getPutovanja();
    }

}
