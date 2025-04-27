CREATE TABLE Korisnik (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          korisnickoIme VARCHAR(255) NOT NULL,
                          lozinka VARCHAR(255) NOT NULL,
                          emailAdresa VARCHAR(255) UNIQUE NOT NULL,
                          ime VARCHAR(255) NOT NULL,
                          prezime VARCHAR(255) NOT NULL,
                          datumRodjenja DATE NOT NULL,
                          adresa VARCHAR(255) NOT NULL,
                          brojTelefona VARCHAR(20) NOT NULL,
                          datumIVremeRegistracije DATETIME NOT NULL,
                          uloga VARCHAR(50) NOT NULL
);

create table putovanje
(
    id                  bigint auto_increment
        primary key,
    prevoznoSredstvo    varchar(50)    not null,
    smestajnaJedinica   varchar(50)    not null,
    nazivDestinacije    varchar(255)   not null,
    slikaLokacije       varchar(255)   not null,
    kategorijaPutovanja varchar(50)    not null,
    datumVremePolaska   datetime       not null,
    datumVremePovratka  datetime       not null,
    brojNocenja         int            not null,
    cenaAranzmana       decimal(10, 2) not null,
    ukupanBrojMesta     int            not null,
    brojSlobodnihMesta  int            not null,
    procenatPopusta     double         null,
    datumPocetkaPopusta timestamp      null,
    datumKrajaPopusta   timestamp      null
);

INSERT INTO Korisnik (
    korisnickoIme,
    lozinka,
    emailAdresa,
    ime,
    prezime,
    datumRodjenja,
    adresa,
    brojTelefona,
    datumIVremeRegistracije,
    uloga
) VALUES (
             'manager',
             'manager',
             'manager@gmail.com',
             'Manager',
             'Managerovic',
             '1980-01-01',
             'Kralja Petra 18',
             '065343423',
             NOW(),
             'Manager'
         );

INSERT INTO Korisnik (
    korisnickoIme,
    lozinka,
    emailAdresa,
    ime,
    prezime,
    datumRodjenja,
    adresa,
    brojTelefona,
    datumIVremeRegistracije,
    uloga
) VALUES (
             'user',
             'user',
             'user@gmail.com',
             'User',
             'Userovic',
             '1980-01-01',
             'Kralja Petra 12',
             '064213213',
             NOW(),
             'User'
         );

create table rezervacija
(
    id                bigint auto_increment
        primary key,
    korisnik_id       bigint   not null,
    putovanje_id      bigint   not null,
    broj_putnika      int      not null,
    datum_rezervacije datetime not null,
    ukupna_cena       double   not null,
    constraint rezervacija_ibfk_1
        foreign key (korisnik_id) references korisnik (id),
    constraint rezervacija_ibfk_2
        foreign key (putovanje_id) references putovanje (id)
);

create index korisnik_id
    on rezervacija (korisnik_id);

create index putovanje_id
    on rezervacija (putovanje_id);

INSERT INTO Korisnik (
    korisnickoIme,
    lozinka,
    emailAdresa,
    ime,
    prezime,
    datumRodjenja,
    adresa,
    brojTelefona,
    datumIVremeRegistracije,
    uloga
) VALUES (
             'user10',
             'user',
             'user11111@gmail.com',
             'User',
             'Userovic',
             '1980-01-01',
             'Kralja Petra 12',
             '064213213',
             NOW(),
             'User'
         );