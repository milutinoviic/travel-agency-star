package com.example.demo.model;

public enum PrevoznoSredstvo {
    AVION("Avion"),
    AUTOBUS("Autobus"),
    SOPSTVENI_PREVOZ("Sopstveni prevoz");

    private final String label;

    PrevoznoSredstvo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
