package com.example.demo.service.impl;

import com.example.demo.dao.PutovanjeDAO;
import com.example.demo.dao.RezervacijaDAO;
import com.example.demo.model.KategorijaPutovanja;
import com.example.demo.model.PrevoznoSredstvo;
import com.example.demo.model.Putovanje;
import com.example.demo.model.Rezervacija;
import com.example.demo.service.PutovanjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PutovanjeServiceImpl implements PutovanjeService {

    @Autowired
    private PutovanjeDAO putovanjeDAO;

    @Autowired
    private RezervacijaDAO rezervacijaDAO;

    @Override
    public Putovanje findOne(Long id) {
        return putovanjeDAO.findOne(id);
    }

    @Override
    public List<Putovanje> search(PrevoznoSredstvo prevoznoSredstvo, String nazivDestinacije, KategorijaPutovanja kategorijaPutovanja, Integer brojNocenja, Double minCena, Double maxCena, String sortOrder) {
        return putovanjeDAO.search(prevoznoSredstvo, nazivDestinacije, kategorijaPutovanja, brojNocenja, minCena, maxCena, sortOrder);
    }

    @Override
    public Putovanje save(Putovanje putovanje) {
        putovanje.setSlikaLokacije("/");
        putovanjeDAO.save(putovanje);
        return putovanje;
    }

    @Override
    public List<Putovanje> findAll() {
        return putovanjeDAO.findAll();
    }

    @Override
    public void checkAndRemoveExpiredDiscount(Putovanje putovanje) {
        if (putovanje.getDatumKrajaPopusta() != null && LocalDateTime.now().isAfter(putovanje.getDatumKrajaPopusta())) {
            putovanje.setProcenatPopusta(null);
            putovanje.setDatumPocetkaPopusta(null);
            putovanje.setDatumKrajaPopusta(null);
            putovanjeDAO.update(putovanje);
        }
    }

    @Override
    public Putovanje update(Putovanje putovanje) {
        putovanjeDAO.update(putovanje);
        return putovanje;
    }

    @Override
    public List<Putovanje> getPromotionalOffers(int n) {
        return putovanjeDAO.findPromotionalOffers(n);
    }

    @Override
    public List<Putovanje> getSeasonalOffers(int n) {
        LocalDateTime now = LocalDateTime.now();
        KategorijaPutovanja category = determineSeason(now);
        return putovanjeDAO.findSeasonalOffers(category, n);
    }

    private KategorijaPutovanja determineSeason(LocalDateTime now) {
        int month = now.getMonthValue();
        if (month >= 6 && month <= 8) {
            return KategorijaPutovanja.Letovanje;
        } else if (month >= 12 || month <= 2) {
            return KategorijaPutovanja.Zimovanje;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        return putovanjeDAO.delete(id);
    }


    @Override
    public boolean hasAnyReservations(Long putovanjeId) {
        return rezervacijaDAO.existsByPutovanjeId(putovanjeId);
    }
}
