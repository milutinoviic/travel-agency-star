package com.example.demo.dao;

import com.example.demo.model.Korisnik;

import java.util.List;

public interface KorisnikDAO {
    Korisnik findOne(Long id);

    List<Korisnik> findAll();

    int save(Korisnik korisnik);

    int update(Korisnik korisnik);

    Korisnik findByUsernameAndPassword(String username, String password);

    boolean checkIfUsernameExists(String korisnickoIme);

    public boolean checkIfEmailExists(String emailAdresa);
}

