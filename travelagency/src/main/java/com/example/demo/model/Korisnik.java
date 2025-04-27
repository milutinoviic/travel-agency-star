package com.example.demo.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Korisnik implements Serializable {

    private Long id;
    private String korisnickoIme;
    private String lozinka;
    private String emailAdresa;
    private String ime;
    private String prezime;
    private LocalDate datumRodjenja;
    private String adresa;
    private String brojTelefona;
    private LocalDateTime datumIVremeRegistracije;
    private String uloga;


    public Korisnik(Long id, String korisnickoIme, String lozinka, String emailAdresa, String ime, String prezime, LocalDate datumRodjenja, String adresa, String brojTelefona, LocalDateTime datumIVremeRegistracije, String uloga) {
        this.id = id;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.emailAdresa = emailAdresa;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.datumIVremeRegistracije = datumIVremeRegistracije;
        this.uloga = uloga;
    }

    public Korisnik(String korisnickoIme, String lozinka, String emailAdresa, String ime, String prezime,
                    LocalDate datumRodjenja, String adresa, String brojTelefona)
    {
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.emailAdresa = emailAdresa;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
    }

    public Korisnik() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getEmailAdresa() {
        return emailAdresa;
    }

    public void setEmailAdresa(String emailAdresa) {
        this.emailAdresa = emailAdresa;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    public LocalDateTime getDatumIVremeRegistracije() {
        return datumIVremeRegistracije;
    }

    public void setDatumIVremeRegistracije(LocalDateTime datumIVremeRegistracije) {
        this.datumIVremeRegistracije = datumIVremeRegistracije;
    }

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }

}
