package com.example.demo.service.impl;

import com.example.demo.dao.KorisnikDAO;
import com.example.demo.model.Korisnik;
import com.example.demo.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class KorisnikServiceImpl implements KorisnikService {

    @Autowired
    private KorisnikDAO korisnikDAO;

    @Override
    public Korisnik findOne(Long id) {
        return korisnikDAO.findOne(id);
    }

    @Override
    public List<Korisnik> findAll() {
        return korisnikDAO.findAll();
    }

    @Override
    public Korisnik update(Korisnik korisnik) {
        korisnikDAO.update(korisnik);
        return korisnik;
    }

    @Override
    public Korisnik save(Korisnik korisnik) {
        LocalDateTime vremeRegistracije = LocalDateTime.now().plusHours(1);
        korisnik.setDatumIVremeRegistracije(vremeRegistracije);
        korisnik.setUloga("User");

        korisnikDAO.save(korisnik);
        return korisnik;
    }


    @Override
    public boolean checkIfUsernameExists(String korisnickoIme) {
        return korisnikDAO.checkIfUsernameExists(korisnickoIme);
    }
    @Override
    public boolean checkIfEmailExists(String emailAdresa){
        return korisnikDAO.checkIfEmailExists(emailAdresa);
    }


    public LocalDate parseDatumRodjenja (String datum)
    {
        return LocalDate.parse(datum, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public Korisnik findByUsernameAndPassword(String username, String password) {
        return korisnikDAO.findByUsernameAndPassword(username, password);
    }
}
