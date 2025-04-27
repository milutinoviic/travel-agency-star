package com.example.demo.dao;

import com.example.demo.model.KategorijaPutovanja;
import com.example.demo.model.PrevoznoSredstvo;
import com.example.demo.model.Putovanje;

import java.util.List;

public interface PutovanjeDAO {
    int save(Putovanje putovanje);
    List<Putovanje> findAll();
    List<Putovanje> findPromotionalOffers(int n);
    List<Putovanje> findSeasonalOffers(KategorijaPutovanja category, int n);
    int update(Putovanje putovanje);
    Putovanje findOne(Long id);
    boolean delete(Long id);
    List<Putovanje> search(PrevoznoSredstvo prevoznoSredstvo, String nazivDestinacije, KategorijaPutovanja kategorijaPutovanja, Integer brojNocenja, Double minCena, Double maxCena, String sortOrder);
}
