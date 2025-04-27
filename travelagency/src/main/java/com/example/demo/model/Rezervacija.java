package com.example.demo.model;

import java.time.LocalDateTime;

public class Rezervacija {
    private Long id;
    private Long korisnikId;
    private Long putovanjeId;
    private int brojPutnika;
    private LocalDateTime datumRezervacije;
    private Double totalPrice;

    private transient Putovanje putovanje;

    public Rezervacija() {
    }

    public Rezervacija(Long id, Long korisnikId, Long putovanjeId, int brojPutnika, LocalDateTime datumRezervacije, Double totalPrice) {
        this.id = id;
        this.korisnikId = korisnikId;
        this.putovanjeId = putovanjeId;
        this.brojPutnika = brojPutnika;
        this.datumRezervacije = datumRezervacije;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Long korisnikId) {
        this.korisnikId = korisnikId;
    }

    public Long getPutovanjeId() {
        return putovanjeId;
    }

    public void setPutovanjeId(Long putovanjeId) {
        this.putovanjeId = putovanjeId;
    }

    public int getBrojPutnika() {
        return brojPutnika;
    }

    public void setBrojPutnika(int brojPutnika) {
        this.brojPutnika = brojPutnika;
    }

    public LocalDateTime getDatumRezervacije() {
        return datumRezervacije;
    }

    public void setDatumRezervacije(LocalDateTime datumRezervacije) {
        this.datumRezervacije = datumRezervacije;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Putovanje getPutovanje() {
        return putovanje;
    }

    public void setPutovanje(Putovanje putovanje) {
        this.putovanje = putovanje;
    }
}
