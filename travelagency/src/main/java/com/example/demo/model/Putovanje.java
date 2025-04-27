package com.example.demo.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Putovanje {
    private Long id;
    private PrevoznoSredstvo prevoznoSredstvo;
    private SmestajnaJedinica smestajnaJedinica;
    private String nazivDestinacije;
    private String slikaLokacije;
    private KategorijaPutovanja kategorijaPutovanja;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datumVremePolaska;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datumVremePovratka;
    private int brojNocenja;
    private double cenaAranzmana;
    private int ukupanBrojMesta;
    private int brojSlobodnihMesta;

    private Double procenatPopusta;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datumPocetkaPopusta;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datumKrajaPopusta;

    public Putovanje() {
    }

    public Putovanje(Long id, PrevoznoSredstvo prevoznoSredstvo, SmestajnaJedinica smestajnaJedinica, String nazivDestinacije, String slikaLokacije, KategorijaPutovanja kategorijaPutovanja, LocalDateTime datumVremePolaska, LocalDateTime datumVremePovratka, int brojNocenja, double cenaAranzmana, int ukupanBrojMesta, int brojSlobodnihMesta, Double procenatPopusta, LocalDateTime datumPocetkaPopusta, LocalDateTime datumKrajaPopusta) {
        this.id = id;
        this.prevoznoSredstvo = prevoznoSredstvo;
        this.smestajnaJedinica = smestajnaJedinica;
        this.nazivDestinacije = nazivDestinacije;
        this.slikaLokacije = slikaLokacije;
        this.kategorijaPutovanja = kategorijaPutovanja;
        this.datumVremePolaska = datumVremePolaska;
        this.datumVremePovratka = datumVremePovratka;
        this.brojNocenja = brojNocenja;
        this.cenaAranzmana = cenaAranzmana;
        this.ukupanBrojMesta = ukupanBrojMesta;
        this.brojSlobodnihMesta = brojSlobodnihMesta;
        this.procenatPopusta = procenatPopusta;
        this.datumPocetkaPopusta = datumPocetkaPopusta;
        this.datumKrajaPopusta = datumKrajaPopusta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrevoznoSredstvo getPrevoznoSredstvo() {
        return prevoznoSredstvo;
    }

    public void setPrevoznoSredstvo(PrevoznoSredstvo prevoznoSredstvo) {
        this.prevoznoSredstvo = prevoznoSredstvo;
    }

    public SmestajnaJedinica getSmestajnaJedinica() {
        return smestajnaJedinica;
    }

    public void setSmestajnaJedinica(SmestajnaJedinica smestajnaJedinica) {
        this.smestajnaJedinica = smestajnaJedinica;
    }

    public String getNazivDestinacije() {
        return nazivDestinacije;
    }

    public void setNazivDestinacije(String nazivDestinacije) {
        this.nazivDestinacije = nazivDestinacije;
    }

    public String getSlikaLokacije() {
        return slikaLokacije;
    }

    public void setSlikaLokacije(String slikaLokacije) {
        this.slikaLokacije = slikaLokacije;
    }

    public KategorijaPutovanja getKategorijaPutovanja() {
        return kategorijaPutovanja;
    }

    public void setKategorijaPutovanja(KategorijaPutovanja kategorijaPutovanja) {
        this.kategorijaPutovanja = kategorijaPutovanja;
    }

    public LocalDateTime getDatumVremePolaska() {
        return datumVremePolaska;
    }

    public void setDatumVremePolaska(LocalDateTime datumVremePolaska) {
        this.datumVremePolaska = datumVremePolaska;
    }

    public LocalDateTime getDatumVremePovratka() {
        return datumVremePovratka;
    }

    public void setDatumVremePovratka(LocalDateTime datumVremePovratka) {
        this.datumVremePovratka = datumVremePovratka;
    }

    public int getBrojNocenja() {
        return brojNocenja;
    }

    public void setBrojNocenja(int brojNocenja) {
        this.brojNocenja = brojNocenja;
    }

    public double getCenaAranzmana() {
        return cenaAranzmana;
    }

    public void setCenaAranzmana(double cenaAranzmana) {
        this.cenaAranzmana = cenaAranzmana;
    }

    public int getUkupanBrojMesta() {
        return ukupanBrojMesta;
    }

    public void setUkupanBrojMesta(int ukupanBrojMesta) {
        this.ukupanBrojMesta = ukupanBrojMesta;
    }

    public int getBrojSlobodnihMesta() {
        return brojSlobodnihMesta;
    }

    public void setBrojSlobodnihMesta(int brojSlobodnihMesta) {
        this.brojSlobodnihMesta = brojSlobodnihMesta;
    }
    public Double getProcenatPopusta() {
        return procenatPopusta;
    }

    public void setProcenatPopusta(Double procenatPopusta) {
        this.procenatPopusta = procenatPopusta;
    }

    public LocalDateTime getDatumPocetkaPopusta() {
        return datumPocetkaPopusta;
    }

    public void setDatumPocetkaPopusta(LocalDateTime datumPocetkaPopusta) {
        this.datumPocetkaPopusta = datumPocetkaPopusta;
    }

    public LocalDateTime getDatumKrajaPopusta() {
        return datumKrajaPopusta;
    }

    public void setDatumKrajaPopusta(LocalDateTime datumKrajaPopusta) {
        this.datumKrajaPopusta = datumKrajaPopusta;
    }

    public double getTrenutnaCena() {
        if (procenatPopusta != null && datumPocetkaPopusta != null && datumKrajaPopusta != null) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(datumPocetkaPopusta) && now.isBefore(datumKrajaPopusta)) {
                return cenaAranzmana - (cenaAranzmana * procenatPopusta / 100);
            }
        }
        return cenaAranzmana;
    }
}
