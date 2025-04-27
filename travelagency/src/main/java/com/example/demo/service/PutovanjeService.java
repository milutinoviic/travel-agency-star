package com.example.demo.service;

import com.example.demo.model.KategorijaPutovanja;
import com.example.demo.model.PrevoznoSredstvo;
import com.example.demo.model.Putovanje;

import java.util.List;

public interface PutovanjeService {
    Putovanje save(Putovanje putovanje);
    List<Putovanje> findAll();
    void checkAndRemoveExpiredDiscount(Putovanje putovanje);
    List<Putovanje> getPromotionalOffers(int n);
    List<Putovanje> getSeasonalOffers(int n);
    Putovanje findOne(Long id);
    Putovanje update(Putovanje putovanje);
    boolean delete(Long id);
    boolean hasAnyReservations(Long putovanjeId);
    List<Putovanje> search(PrevoznoSredstvo prevoznoSredstvo, String nazivDestinacije, KategorijaPutovanja kategorijaPutovanja, Integer brojNocenja, Double minCena, Double maxCena, String sortOrder);

}
