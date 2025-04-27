package com.example.demo.dao.implementation;

import com.example.demo.dao.KorisnikDAO;
import com.example.demo.model.Korisnik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class KorisnikDAOImpl implements KorisnikDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class KorisniciRowCallbackHandler implements RowCallbackHandler {

        private Map<Long, Korisnik> korisnici = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            String korisnickoIme = rs.getString(index++);
            String lozinka = rs.getString(index++);
            String emailAdresa = rs.getString(index++);
            String ime = rs.getString(index++);
            String prezime = rs.getString(index++);
            LocalDate datumRodjenja = rs.getDate(index++).toLocalDate();
            String adresa = rs.getString(index++);
            String brojTelefona = rs.getString(index++);
            LocalDateTime datumIVremeRegistracije = rs.getTimestamp(index++).toLocalDateTime();
            String uloga = rs.getString(index++);
            Korisnik korisnik = korisnici.get(id);
            if(korisnik == null) {
                korisnik = new Korisnik(id, korisnickoIme, lozinka, emailAdresa, ime, prezime, datumRodjenja, adresa, brojTelefona, datumIVremeRegistracije, uloga);
                korisnici.put(korisnik.getId(), korisnik);
            }
        }

        public List<Korisnik> getKorisnici(){
            return new ArrayList<>(korisnici.values());
        }

    }

    @Override
    public Korisnik findOne(Long id) {
        String sql =
                "SELECT * FROM korisnik " +
                        "WHERE id = ?";

        KorisniciRowCallbackHandler rowCallbackHandler = new KorisniciRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        return rowCallbackHandler.getKorisnici().get(0);
    }

    @Override
    public List<Korisnik> findAll() {
        String sql =
                "SELECT * FROM korisnik";

        KorisniciRowCallbackHandler rowCallbackHandler = new KorisniciRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getKorisnici();
    }

    @Transactional
    @Override
    public int save(Korisnik korisnik) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO korisnik (korisnickoIme, lozinka, emailAdresa, ime, prezime, datumRodjenja, adresa, brojTelefona, datumIVremeRegistracije, uloga) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, korisnik.getKorisnickoIme());
                preparedStatement.setString(index++, korisnik.getLozinka());
                preparedStatement.setString(index++, korisnik.getEmailAdresa());
                preparedStatement.setString(index++, korisnik.getIme());
                preparedStatement.setString(index++, korisnik.getPrezime());

                if (korisnik.getDatumRodjenja() != null) {
                    Date sqlDate = Date.valueOf(korisnik.getDatumRodjenja());
                    preparedStatement.setDate(index++, sqlDate);
                } else {
                    preparedStatement.setNull(index++, Types.DATE);
                }

                preparedStatement.setString(index++, korisnik.getAdresa());
                preparedStatement.setString(index++, korisnik.getBrojTelefona());

                if (korisnik.getDatumIVremeRegistracije() != null) {
                    Timestamp timestamp = Timestamp.valueOf(korisnik.getDatumIVremeRegistracije());
                    preparedStatement.setTimestamp(index++, timestamp);
                } else {
                    preparedStatement.setNull(index++, Types.TIMESTAMP);
                }

                preparedStatement.setString(index++, korisnik.getUloga());
                return preparedStatement;
            }

        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }


    @Transactional
    @Override
    public int update(Korisnik korisnik) {
        String sql = "UPDATE korisnik SET korisnickoIme = ?, lozinka = ?, emailAdresa = ?, ime = ?, prezime = ?, datumRodjenja = ?, adresa = ?, brojTelefona = ?, datumIVremeRegistracije = ?, uloga = ? WHERE id = ?";
        boolean uspeh = jdbcTemplate.update(sql, korisnik.getKorisnickoIme(), korisnik.getLozinka(), korisnik.getEmailAdresa(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getDatumRodjenja(), korisnik.getAdresa(), korisnik.getBrojTelefona(), korisnik.getDatumIVremeRegistracije(), korisnik.getUloga(), korisnik.getId()) == 1;

        return uspeh?1:0;
    }

    @Override
    public Korisnik findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM korisnik WHERE korisnickoIme = ? AND lozinka = ?";

        KorisniciRowCallbackHandler rowCallbackHandler = new KorisniciRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, username, password);

        if (rowCallbackHandler.getKorisnici().isEmpty()) {
            return null;
        } else {
            return rowCallbackHandler.getKorisnici().get(0);
        }
    }


    @Override
    public boolean checkIfUsernameExists(String korisnickoIme) {
        String sql = "SELECT * FROM korisnik WHERE korisnickoIme = ?";
        KorisniciRowCallbackHandler rowCallbackHandler = new KorisniciRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, korisnickoIme);
        List<Korisnik> korisnici = rowCallbackHandler.getKorisnici();
        if(!korisnici.isEmpty()) {
            return true;
        }
        else
        {
            return false;
        }
    }
    @Override
    public boolean checkIfEmailExists(String emailAdresa) {
        String sql = "SELECT * FROM korisnik WHERE emailAdresa = ?";
        KorisniciRowCallbackHandler rowCallbackHandler = new KorisniciRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler,emailAdresa);
        List<Korisnik> korisnici = rowCallbackHandler.getKorisnici();
        if(!korisnici.isEmpty()) {
            return true;
        }
        else
        {
            return false;
        }
    }

}
