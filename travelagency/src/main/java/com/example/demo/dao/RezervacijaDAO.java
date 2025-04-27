package com.example.demo.dao;

import com.example.demo.model.Rezervacija;

import java.util.List;

public interface RezervacijaDAO {
    Rezervacija save(Rezervacija rezervacija);
    Rezervacija findOne(Long id);
    List<Rezervacija> findByKorisnikId(Long korisnikId);
    List<Rezervacija> findByPutovanjeId(Long putovanjeId);
    void delete(Long id);
    boolean existsByPutovanjeId(Long putovanjeId);
}
