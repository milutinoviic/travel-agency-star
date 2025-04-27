package com.example.demo.dao.implementation;

import com.example.demo.dao.RezervacijaDAO;
import com.example.demo.model.Rezervacija;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class RezervacijaDAOImpl implements RezervacijaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Rezervacija> rowMapper = (rs, rowNum) -> {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setId(rs.getLong("id"));
        rezervacija.setKorisnikId(rs.getLong("korisnik_id"));
        rezervacija.setPutovanjeId(rs.getLong("putovanje_id"));
        rezervacija.setBrojPutnika(rs.getInt("broj_putnika"));
        rezervacija.setDatumRezervacije(rs.getTimestamp("datum_rezervacije").toLocalDateTime());
        rezervacija.setTotalPrice(rs.getDouble("ukupna_cena"));
        return rezervacija;
    };

    @Override
    public Rezervacija save(Rezervacija rezervacija) {
        String sql = "INSERT INTO rezervacija (korisnik_id, putovanje_id, broj_putnika, datum_rezervacije, ukupna_cena) VALUES (?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, rezervacija.getKorisnikId());
            ps.setLong(2, rezervacija.getPutovanjeId());
            ps.setInt(3, rezervacija.getBrojPutnika());
            ps.setTimestamp(4, Timestamp.valueOf(rezervacija.getDatumRezervacije()));
            ps.setDouble(5, rezervacija.getTotalPrice());
            return ps;
        }, keyHolder);

        rezervacija.setId(keyHolder.getKey().longValue());
        return rezervacija;
    }

    @Override
    public Rezervacija findOne(Long id) {
        String sql = "SELECT * FROM rezervacija WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Rezervacija> findByKorisnikId(Long korisnikId) {
        String sql = "SELECT * FROM rezervacija WHERE korisnik_id = ?";
        return jdbcTemplate.query(sql, rowMapper, korisnikId);
    }

    @Override
    public List<Rezervacija> findByPutovanjeId(Long putovanjeId) {
        String sql = "SELECT * FROM rezervacija WHERE putovanje_id = ?";
        return jdbcTemplate.query(sql, rowMapper, putovanjeId);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM rezervacija WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByPutovanjeId(Long putovanjeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM rezervacija WHERE putovanje_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, putovanjeId);
    }
}
