package com.example.demo.model;

public enum KategorijaPutovanja {
    Zimovanje("Zimovanje"),
    Letovanje("Letovanje"),
    NovaGodina("Nova godina"),
    LastMinute("Last minute"),
    Sve("Sve");

    private final String label;

    KategorijaPutovanja(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
