function validateForm() {
    let isValid = true;
    let errorMessage = "";

    const prevoznoSredstvo = document.getElementById("prevoznoSredstvo");
    if (prevoznoSredstvo.value === "") {
        isValid = false;
        errorMessage += "Izaberite prevozno sredstvo.\n";
    }

    const smestajnaJedinica = document.getElementById("smestajnaJedinica");
    if (smestajnaJedinica.value === "") {
        isValid = false;
        errorMessage += "Izaberite smeštajnu jedinicu.\n";
    }

    const nazivDestinacije = document.getElementById("nazivDestinacije");
    if (nazivDestinacije.value.trim() === "") {
        isValid = false;
        errorMessage += "Unesite naziv destinacije.\n";
    }

    const kategorijaPutovanja = document.getElementById("kategorijaPutovanja");
    if (kategorijaPutovanja.value === "") {
        isValid = false;
        errorMessage += "Izaberite kategoriju putovanja.\n";
    }

    const datumVremePolaska = document.getElementById("datumVremePolaska");
    const datumVremePovratka = document.getElementById("datumVremePovratka");
    if (datumVremePolaska.value === "" || datumVremePovratka.value === "") {
        isValid = false;
        errorMessage += "Unesite datume i vremena polaska i povratka.\n";
    } else if (new Date(datumVremePolaska.value) >= new Date(datumVremePovratka.value)) {
        isValid = false;
        errorMessage += "Datum i vreme povratka mora biti nakon datuma i vremena polaska.\n";
    }

    const brojNocenja = document.getElementById("brojNocenja");
    if (brojNocenja.value === "" || parseInt(brojNocenja.value) <= 0) {
        isValid = false;
        errorMessage += "Unesite validan broj noćenja.\n";
    }

    const cenaAranzmana = document.getElementById("cenaAranzmana");
    if (cenaAranzmana.value === "" || parseFloat(cenaAranzmana.value) <= 0) {
        isValid = false;
        errorMessage += "Unesite validnu cenu aranžmana.\n";
    }

    const ukupanBrojMesta = document.getElementById("ukupanBrojMesta");
    if (ukupanBrojMesta.value === "" || parseInt(ukupanBrojMesta.value) <= 0) {
        isValid = false;
        errorMessage += "Unesite validan ukupan broj mesta.\n";
    }

    const brojSlobodnihMesta = document.getElementById("brojSlobodnihMesta");
    if (brojSlobodnihMesta.value === "" || parseInt(brojSlobodnihMesta.value) < 0) {
        isValid = false;
        errorMessage += "Unesite validan broj slobodnih mesta.\n";
    }

    const ukupnoMesta = parseInt(ukupanBrojMesta.value);
    const slobodnaMesta = parseInt(brojSlobodnihMesta.value);
    if (slobodnaMesta > ukupnoMesta) {
        isValid = false;
        errorMessage += "Broj slobodnih mesta ne može biti veći od ukupnog broja mesta.\n";
    }

    if (!isValid) {
        alert(errorMessage);
    }

    return isValid;
}
