package com.example.demo.controller;

import com.example.demo.model.Korisnik;
import com.example.demo.service.KorisnikService;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/registracija")
public class RegistracijaController {

    @Autowired
    private KorisnikService korisnikService;


    @GetMapping
    public ModelAndView registracijaKorisnika(Model model) {
        model.addAttribute("korisnik", new Korisnik());
        return new ModelAndView("registracija");
    }

    @PostMapping
    public String register(@ModelAttribute("korisnik") Korisnik korisnik,
                           Model model) {


        if (korisnikService.checkIfUsernameExists(korisnik.getKorisnickoIme())) {
            model.addAttribute("error", "Username already exists");
            return "registracija";
        }

        if (korisnikService.checkIfEmailExists(korisnik.getEmailAdresa())) {
            model.addAttribute("error", "Email already exists");
            return "registracija";
        }

        korisnikService.save(korisnik);
        return "redirect:/login";
    }
}

