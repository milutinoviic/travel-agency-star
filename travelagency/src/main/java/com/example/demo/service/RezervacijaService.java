package com.example.demo.service;

import com.example.demo.model.Rezervacija;

import java.time.LocalDateTime;
import java.util.List;

public interface RezervacijaService {
    Rezervacija createRezervacija(Long korisnikId, Long putovanjeId, int brojPutnika, LocalDateTime datumRezervacije, Double totalPrice);    Rezervacija getRezervacija(Long id);
    List<Rezervacija> getRezervacijeByKorisnik(Long korisnikId);
    List<Rezervacija> getRezervacijeByPutovanje(Long putovanjeId);
    void deleteRezervacija(Long id);
}
