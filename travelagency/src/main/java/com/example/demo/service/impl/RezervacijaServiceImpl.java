package com.example.demo.service.impl;

import com.example.demo.dao.RezervacijaDAO;
import com.example.demo.model.Rezervacija;
import com.example.demo.service.RezervacijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RezervacijaServiceImpl implements RezervacijaService {

    @Autowired
    private RezervacijaDAO rezervacijaDAO;

    @Override
    public Rezervacija createRezervacija(Long korisnikId, Long putovanjeId, int brojPutnika, LocalDateTime datumRezervacije, Double totalPrice) {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setKorisnikId(korisnikId);
        rezervacija.setPutovanjeId(putovanjeId);
        rezervacija.setBrojPutnika(brojPutnika);
        rezervacija.setDatumRezervacije(datumRezervacije);
        rezervacija.setTotalPrice(totalPrice);

        return rezervacijaDAO.save(rezervacija);
    }

    @Override
    public Rezervacija getRezervacija(Long id) {
        return rezervacijaDAO.findOne(id);
    }

    @Override
    public List<Rezervacija> getRezervacijeByKorisnik(Long korisnikId) {
        return rezervacijaDAO.findByKorisnikId(korisnikId);
    }

    @Override
    public List<Rezervacija> getRezervacijeByPutovanje(Long putovanjeId) {
        return rezervacijaDAO.findByPutovanjeId(putovanjeId);
    }

    @Override
    public void deleteRezervacija(Long id) {
        rezervacijaDAO.delete(id);
    }
}
