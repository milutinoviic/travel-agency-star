package com.example.demo.service;

import com.example.demo.model.Korisnik;

import java.time.LocalDate;
import java.util.List;

public interface KorisnikService {
    Korisnik save(Korisnik korisnik);
    LocalDate parseDatumRodjenja(String datumRodjenja);
    boolean checkIfUsernameExists(String korisnickoIme);
    Korisnik findOne(Long id);
    List<Korisnik> findAll();
    Korisnik update(Korisnik korisnik);
    Korisnik findByUsernameAndPassword(String username, String password);
    boolean checkIfEmailExists(String emailAdresa);


}
