package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.PutovanjeService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/putovanje")
public class PutovanjeController {

    @Autowired
    private PutovanjeService putovanjeService;

    @GetMapping
    public ModelAndView index(HttpSession session, HttpServletResponse response) throws IOException {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        LocalDateTime today = LocalDateTime.now();

        List<Putovanje> putovanja;
        List<Putovanje> aktivnaPutovanja = new ArrayList<>();
        if (korisnik != null && "Manager".equals(korisnik.getUloga())) {
            aktivnaPutovanja = putovanjeService.findAll();



        } else {
            putovanja = putovanjeService.findAll().stream()
                    .filter(putovanje -> putovanje.getBrojSlobodnihMesta() > 0)
                    .collect(Collectors.toList());
            for(Putovanje putovanje:putovanja){

                if (putovanje.getDatumVremePolaska().isAfter(today)) {
                    aktivnaPutovanja.add(putovanje);

                }
            }
        }

        for (Putovanje putovanje : aktivnaPutovanja) {
            putovanjeService.checkAndRemoveExpiredDiscount(putovanje);
        }

        int brojPonuda = 5;
        List<Putovanje> promotivnePonude = putovanjeService.getPromotionalOffers(brojPonuda);

        List<Putovanje> aktivnaPromotivnaPonuda=new ArrayList<>();


        if (promotivnePonude.isEmpty()) {
            promotivnePonude = putovanjeService.getSeasonalOffers(brojPonuda);
        }
        for(Putovanje putovanje:promotivnePonude){

            if (putovanje.getDatumVremePolaska().isAfter(today)) {
                aktivnaPromotivnaPonuda.add(putovanje);

            }
        }

        List<KategorijaPutovanja> kategorijePutovanja = Arrays.asList(KategorijaPutovanja.values());
        List<PrevoznoSredstvo> tipoviPrevoznogSredstva = Arrays.asList(PrevoznoSredstvo.values());
        List<SmestajnaJedinica> tipoviSmestajneJedinice = Arrays.asList(SmestajnaJedinica.values());

        ModelAndView result = new ModelAndView("putovanje");
        result.addObject("putovanja",aktivnaPutovanja);
        result.addObject("promotivnePonude", aktivnaPromotivnaPonuda);
        result.addObject("kategorijePutovanja", kategorijePutovanja);
        result.addObject("tipoviPrevoznogSredstva", tipoviPrevoznogSredstva);
        result.addObject("tipoviSmestajneJedinice", tipoviSmestajneJedinice);
        return result;
    }

    @GetMapping(value = "/add")
    public ModelAndView create(HttpSession session, HttpServletResponse response) throws IOException {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if(korisnik == null || !korisnik.getUloga().equals("Manager")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Zabranjen pristup stranici");
            return null;
        }

        List<PrevoznoSredstvo> prevoznaSredstva = Arrays.asList(PrevoznoSredstvo.values());
        List<SmestajnaJedinica> smestajneJedinice = Arrays.asList(SmestajnaJedinica.values());
        List<KategorijaPutovanja> kategorijePutovanja = Arrays.asList(KategorijaPutovanja.values());

        ModelAndView result = new ModelAndView("putovanjeAdd");
        result.addObject("putovanje", new Putovanje());
        result.addObject("prevoznaSredstva", prevoznaSredstva);
        result.addObject("smestajneJedinice", smestajneJedinice);
        result.addObject("kategorijePutovanja", kategorijePutovanja);
        return result;
    }

    @PostMapping(value = "/add")
    public String save(@ModelAttribute("putovanje") Putovanje putovanje, HttpSession session, HttpServletResponse response, ModelAndView model) throws IOException {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if(korisnik == null || !korisnik.getUloga().equals("Manager")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Zabranjen pristup stranici");
            return null;
        }
        try {
            putovanjeService.save(putovanje);
            return "redirect:/putovanje";
        } catch (Exception e) {
            model.addObject("errorMessage", "Greška prilikom dodavanja putovanja.");
            return "putovanjeAdd";
        }
    }

    @GetMapping("/detail/{id}")
    public ModelAndView viewDetails(@PathVariable Long id) {
        Putovanje putovanje = putovanjeService.findOne(id);
        boolean hasReservations = putovanjeService.hasAnyReservations(id);
        putovanjeService.checkAndRemoveExpiredDiscount(putovanje);

        ModelAndView modelAndView = new ModelAndView("putovanjeDetail");
        modelAndView.addObject("putovanje", putovanje);
        modelAndView.addObject("hasReservations", hasReservations);


        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editPutovanje(@PathVariable Long id, HttpSession session, HttpServletResponse response) throws IOException {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null || !"Manager".equals(korisnik.getUloga())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Zabranjen pristup stranici");
            return null;
        }

        Putovanje putovanje = putovanjeService.findOne(id);

        ModelAndView modelAndView = new ModelAndView("putovanjeEdit");
        modelAndView.addObject("putovanje", putovanje);
        modelAndView.addObject("prevoznaSredstva", Arrays.asList(PrevoznoSredstvo.values()));
        modelAndView.addObject("smestajneJedinice", Arrays.asList(SmestajnaJedinica.values()));
        modelAndView.addObject("kategorijePutovanja", Arrays.asList(KategorijaPutovanja.values()));

        return modelAndView;
    }


    @PostMapping("/edit/{id}")
    public ModelAndView editPutovanje(HttpSession session,
                                      @PathVariable Long id,
                                      @RequestParam(required = false) String nazivDestinacije,
                                      @RequestParam(required = false) PrevoznoSredstvo prevoznoSredstvo,
                                      @RequestParam(required = false) SmestajnaJedinica smestajnaJedinica,
                                      @RequestParam(required = false) KategorijaPutovanja kategorijaPutovanja,
                                      @RequestParam(required = false) String datumVremePolaska,
                                      @RequestParam(required = false) String datumVremePovratka,
                                      @RequestParam(required = false) Integer brojNocenja,
                                      @RequestParam(required = false) Double cenaAranzmana,
                                      @RequestParam(required = false) Integer ukupanBrojMesta,
                                      @RequestParam(required = false) Integer brojSlobodnihMesta) {

        ModelAndView result = new ModelAndView("putovanjeEdit");
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");

        if (korisnik != null && "Manager".equals(korisnik.getUloga())) {
            Putovanje putovanje = putovanjeService.findOne(id);

            if (putovanje == null) {
                result.addObject("error", "Putovanje nije pronađeno.");
                return result;
            }

            boolean isUpdated = false;

            if (nazivDestinacije != null && !nazivDestinacije.trim().isEmpty()) {
                putovanje.setNazivDestinacije(nazivDestinacije);
                isUpdated = true;
            }
            if (prevoznoSredstvo != null) {
                putovanje.setPrevoznoSredstvo(prevoznoSredstvo);
                isUpdated = true;
            }
            if (smestajnaJedinica != null) {
                putovanje.setSmestajnaJedinica(smestajnaJedinica);
                isUpdated = true;
            }
            if (kategorijaPutovanja != null) {
                putovanje.setKategorijaPutovanja(kategorijaPutovanja);
                isUpdated = true;
            }
            if (datumVremePolaska != null && !datumVremePolaska.trim().isEmpty()) {
                putovanje.setDatumVremePolaska(LocalDateTime.parse(datumVremePolaska, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            }
            if (datumVremePovratka != null && !datumVremePovratka.trim().isEmpty()) {
                putovanje.setDatumVremePovratka(LocalDateTime.parse(datumVremePovratka, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            }
            if (brojNocenja != null && brojNocenja > 0) {
                putovanje.setBrojNocenja(brojNocenja);
                isUpdated = true;
            }
            if (cenaAranzmana != null && cenaAranzmana > 0) {
                putovanje.setCenaAranzmana(cenaAranzmana);
                isUpdated = true;
            }
            if (ukupanBrojMesta != null && ukupanBrojMesta > 0) {
                putovanje.setUkupanBrojMesta(ukupanBrojMesta);
                isUpdated = true;
            }
            if (brojSlobodnihMesta != null && brojSlobodnihMesta >= 0) {
                putovanje.setBrojSlobodnihMesta(brojSlobodnihMesta);
                isUpdated = true;
            }


            if (isUpdated) {
                putovanjeService.update(putovanje);
                result.addObject("success", "Putovanje uspešno ažurirano.");
                result.setViewName("redirect:/putovanje/detail/" + id);
            } else {
                result.addObject("error", "Nema promena za ažuriranje.");
            }

            result.addObject("putovanje", putovanje);
        } else {
            result.addObject("error", "Nemate ovlašćenje za izmenu ovog putovanja.");
        }

        return result;
    }

    @GetMapping(value = "/search")
    public ModelAndView search(
            @RequestParam(required = false) PrevoznoSredstvo prevoznoSredstvo,
            @RequestParam(required = false) String nazivDestinacije,
            @RequestParam(required = false) KategorijaPutovanja kategorijaPutovanja,
            @RequestParam(required = false) Integer brojNocenja,
            @RequestParam(required = false) Double minCena,
            @RequestParam(required = false) Double maxCena,
            @RequestParam(required = false) String sortOrder,
            HttpSession session,
            HttpServletResponse response) throws IOException {

        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        List<Putovanje> putovanja = putovanjeService.search(prevoznoSredstvo, nazivDestinacije, kategorijaPutovanja, brojNocenja, minCena, maxCena, sortOrder);

        if (korisnik == null || !"Manager".equals(korisnik.getUloga())) {
            putovanja = putovanja.stream()
                    .filter(putovanje -> putovanje.getBrojSlobodnihMesta() > 0)
                    .collect(Collectors.toList());
        }

        List<KategorijaPutovanja> kategorijePutovanja = Arrays.asList(KategorijaPutovanja.values());
        List<PrevoznoSredstvo> tipoviPrevoznogSredstva = Arrays.asList(PrevoznoSredstvo.values());
        List<SmestajnaJedinica> tipoviSmestajneJedinice = Arrays.asList(SmestajnaJedinica.values());

        ModelAndView result = new ModelAndView("putovanje");
        result.addObject("putovanja", putovanja);
        result.addObject("kategorijePutovanja", kategorijePutovanja);
        result.addObject("tipoviPrevoznogSredstva", tipoviPrevoznogSredstva);
        result.addObject("tipoviSmestajneJedinice", tipoviSmestajneJedinice);
        return result;
    }


    @GetMapping("/delete/{id}")
    public ModelAndView deletePutovanje(@PathVariable Long id, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        ModelAndView modelAndView = new ModelAndView("putovanje");

        if (korisnik == null || !korisnik.getUloga().equals("Manager")) {
            modelAndView.addObject("error", "Unauthorized");
            return modelAndView;
        }

        if (putovanjeService.hasAnyReservations(id)) {
            modelAndView.addObject("error", "Cannot Delete Trip With Reservation");
            return modelAndView;
        }

        putovanjeService.delete(id);
        modelAndView.addObject("success", "TripDeleted");

        List<Putovanje> putovanja = putovanjeService.findAll();
        modelAndView.addObject("putovanja", putovanja);

        return modelAndView;
    }

    @PostMapping("/addDiscount")
    public String addDiscount(
            @RequestParam Long id,
            @RequestParam Double procenatPopusta,
            @RequestParam String datumPocetkaPopusta,
            @RequestParam String datumKrajaPopusta,
            HttpSession session,
            HttpServletResponse response) throws IOException {

        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null || !"Manager".equals(korisnik.getUloga())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Zabranjen pristup stranici");
            return null;
        }

        Putovanje putovanje = putovanjeService.findOne(id);
        if (putovanje == null) {
            return "redirect:/putovanje";
        }

        LocalDateTime now = LocalDateTime.now();
        if (putovanje.getDatumPocetkaPopusta() != null && putovanje.getDatumKrajaPopusta() != null
                && now.isAfter(putovanje.getDatumPocetkaPopusta()) && now.isBefore(putovanje.getDatumKrajaPopusta())) {
            return "redirect:/putovanje/detail/" + id + "?error=ActiveDiscount";
        }

        putovanje.setProcenatPopusta(procenatPopusta);
        putovanje.setDatumPocetkaPopusta(LocalDateTime.parse(datumPocetkaPopusta, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        putovanje.setDatumKrajaPopusta(LocalDateTime.parse(datumKrajaPopusta, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));

        putovanjeService.update(putovanje);

        return "redirect:/putovanje/detail/" + id;
    }
}
