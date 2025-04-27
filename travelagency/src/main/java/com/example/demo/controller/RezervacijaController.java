package com.example.demo.controller;

import com.example.demo.model.Korisnik;
import com.example.demo.model.Putovanje;
import com.example.demo.service.PutovanjeService;
import com.example.demo.service.RezervacijaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/rezervacija")
public class RezervacijaController {

    @Autowired
    private RezervacijaService rezervacijaService;

    @Autowired
    private PutovanjeService putovanjeService;

    @GetMapping("/make/{putovanjeId}")
    public ModelAndView showReservationForm(@PathVariable Long putovanjeId, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null) {
            return new ModelAndView("redirect:/login");
        }

        Putovanje putovanje = putovanjeService.findOne(putovanjeId);
        if (putovanje == null) {
            return new ModelAndView("redirect:/putovanje?error=PutovanjeNotFound");
        }

        ModelAndView modelAndView = new ModelAndView("rezervacijaForm");
        modelAndView.addObject("putovanje", putovanje);
        return modelAndView;
    }

    @PostMapping("/create")
    public String createReservation(
            @RequestParam Long putovanjeId,
            @RequestParam int brojPutnika,
            HttpSession session) {

        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null) {
            return "redirect:/login";
        }

        Putovanje putovanje = putovanjeService.findOne(putovanjeId);
        if (putovanje == null || brojPutnika > putovanje.getBrojSlobodnihMesta()) {
            return "redirect:/putovanje/detail/" + putovanjeId + "?error=InvalidReservation";
        }

        double pricePerPassenger = putovanje.getCenaAranzmana();

        LocalDateTime now = LocalDateTime.now();
        if (putovanje.getProcenatPopusta() != null &&
                putovanje.getDatumPocetkaPopusta() != null &&
                putovanje.getDatumKrajaPopusta() != null &&
                now.isAfter(putovanje.getDatumPocetkaPopusta()) &&
                now.isBefore(putovanje.getDatumKrajaPopusta())) {

            pricePerPassenger -= pricePerPassenger * putovanje.getProcenatPopusta() / 100;
        }

        double ukupnaCena = pricePerPassenger * brojPutnika;

        rezervacijaService.createRezervacija(
                korisnik.getId(),
                putovanjeId,
                brojPutnika,
                putovanje.getDatumVremePolaska(),
                ukupnaCena
        );
        System.out.println(putovanje.getDatumVremePolaska());
        putovanje.setBrojSlobodnihMesta(putovanje.getBrojSlobodnihMesta() - brojPutnika);
        putovanjeService.update(putovanje);

        return "redirect:/putovanje/detail/" + putovanjeId + "?success=ReservationSuccessful";
    }
}
