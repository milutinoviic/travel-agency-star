package com.example.demo.controller;

import com.example.demo.model.Korisnik;
import com.example.demo.model.Putovanje;
import com.example.demo.model.Rezervacija;
import com.example.demo.service.KorisnikService;
import com.example.demo.service.PutovanjeService;
import com.example.demo.service.RezervacijaService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    private String bURL;

    @Autowired
    private KorisnikService korisnikService;
    @Autowired
    private PutovanjeService putovanjeService;
    @Autowired
    private RezervacijaService rezervacijaService;

    @GetMapping
    public ModelAndView index(HttpSession session, HttpServletResponse response) throws IOException {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Korisnik nije ulogovan i ne može videti profil");
            return null;
        }

        List<Rezervacija> sveRezervacije = rezervacijaService.getRezervacijeByKorisnik(korisnik.getId());

        LocalDateTime now = LocalDateTime.now();
        List<Map<String, Object>> buduceRezervacije = sveRezervacije.stream()
                .filter(rezervacija -> {
                    rezervacija.setPutovanje(putovanjeService.findOne(rezervacija.getPutovanjeId()));
                    return rezervacija.getDatumRezervacije().isAfter(now);
                })
                .map(rezervacija -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("rezervacija", rezervacija);
                    map.put("canCancel", rezervacija.getDatumRezervacije().isAfter(now.plusHours(48)));
                    return map;
                })
                .collect(Collectors.toList());

        List<Rezervacija> prosleRezervacije = sveRezervacije.stream()
                .filter(rezervacija -> {
                    rezervacija.setPutovanje(putovanjeService.findOne(rezervacija.getPutovanjeId()));
                    return rezervacija.getDatumRezervacije().isBefore(now);
                })
                .collect(Collectors.toList());

        ModelAndView result = new ModelAndView("profile");
        result.addObject("korisnik", korisnik);
        result.addObject("buduceRezervacije", buduceRezervacije);
        result.addObject("prosleRezervacije", prosleRezervacije);

        return result;
    }




    @PostMapping("/cancelReservation")
    public String cancelReservation(@RequestParam Long rezervacijaId, HttpSession session) {
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik == null) {
            return "redirect:/login";
        }

        Rezervacija rezervacija = rezervacijaService.getRezervacija(rezervacijaId);
        if (rezervacija == null) {
            return "redirect:/profile?error=ReservationNotFound";
        }

        Putovanje putovanje = putovanjeService.findOne(rezervacija.getPutovanjeId());
        if (putovanje == null) {
            return "redirect:/profile?error=TripNotFound";
        }

        LocalDateTime now = LocalDateTime.now();
        if (rezervacija.getDatumRezervacije().isBefore(now.plusHours(48))) {
            return "redirect:/profile?error=CannotCancelReservation";
        }

        putovanje.setBrojSlobodnihMesta(putovanje.getBrojSlobodnihMesta() + rezervacija.getBrojPutnika());
        putovanjeService.update(putovanje);

        rezervacijaService.deleteRezervacija(rezervacijaId);

        return "redirect:/profile?success=ReservationCanceled";
    }

    @PostMapping(value="/update")
    public ModelAndView edit(HttpSession session,
                             @RequestParam(required = false) String ime,
                             @RequestParam(required = false) String prezime,
                             @RequestParam(required = false) String korisnickoIme,
                             @RequestParam(required = false) String newPassword,
                             @RequestParam(required = false) String confirmPassword,
                             @RequestParam(required = false) String emailAdresa,
                             @RequestParam(required = false) String adresa,
                             @RequestParam(required = false) String brojTelefona) {

        ModelAndView result = new ModelAndView("profile");
        Korisnik korisnik = (Korisnik) session.getAttribute("korisnik");
        if (korisnik != null) {
            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmPassword)) {
                    result.addObject("error", "Lozinke se ne poklapaju.");
                    result.addObject("korisnik", korisnik);
                    return result;
                }
                korisnik.setLozinka(newPassword);
            }

            boolean isUpdated = false;
            if (ime != null && !ime.trim().isEmpty()) {
                korisnik.setIme(ime);
                isUpdated = true;
            }
            if (prezime != null && !prezime.trim().isEmpty()) {
                korisnik.setPrezime(prezime);
                isUpdated = true;
            }
            if (korisnickoIme != null && !korisnickoIme.trim().isEmpty()) {
                korisnik.setKorisnickoIme(korisnickoIme);
                isUpdated = true;
            }
            if (emailAdresa != null && !emailAdresa.trim().isEmpty()) {
                korisnik.setEmailAdresa(emailAdresa);
                isUpdated = true;
            }
            if (adresa != null && !adresa.trim().isEmpty()) {
                korisnik.setAdresa(adresa);
                isUpdated = true;
            }
            if (brojTelefona != null && !brojTelefona.trim().isEmpty()) {
                korisnik.setBrojTelefona(brojTelefona);
                isUpdated = true;
            }

            if (isUpdated) {
                korisnikService.update(korisnik);
                session.setAttribute("korisnik", korisnik);
                result.addObject("success", "Profil uspešno ažuriran.");
            }
        }

        result.addObject("korisnik", korisnik);
        return result;
    }

}
