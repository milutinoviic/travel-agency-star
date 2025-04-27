package com.example.demo.controller;

import com.example.demo.model.Korisnik;
import com.example.demo.service.KorisnikService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("/login")
public class LoginController{

    @Autowired
    private ServletContext servletContext;
    private String bURL;

    @Autowired
    private KorisnikService korisnikService;

    @PostConstruct
    public void init() {
        bURL = servletContext.getContextPath() + "/";
    }

    @GetMapping
    public ModelAndView loginKorisnika(Model model) {
        model.addAttribute("korisnik", new Korisnik());
        return new ModelAndView("login");
    }

    @PostMapping
    public String login(@ModelAttribute("korisnik") Korisnik korisnik, Model model, HttpSession session) {
        Korisnik existingKorisnik = korisnikService.findByUsernameAndPassword(korisnik.getKorisnickoIme(), korisnik.getLozinka());
        if (existingKorisnik != null) {
            session.setAttribute("korisnik", existingKorisnik);
                return "redirect:/putovanje";
        } else {
            model.addAttribute("error", "User does not exist or invalid credentials");
            return "login";
        }
    }

    @GetMapping(value = "/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect(bURL + "login");
    }
}
