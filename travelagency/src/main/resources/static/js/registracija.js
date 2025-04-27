function validateNotEmpty(field, fieldName) {
    if (field.value.trim() === "") {
        alert(fieldName + " cannot be empty.");
        return false;
    }
    const length = field.value.length;
    if (length < 3) {
        alert(fieldName +" must be at least 3 characters long.");
        return false;
    }
    return true;
}

function validateUsernameLength(field, fieldName) {
    if (field.value.trim() === "") {
        alert(fieldName + " cannot be empty.");
        return false;
    }
    if (field.value.length < 7) {
        alert(fieldName +" must be at least 7 characters long.");
        return false;
    }
    return true;
}


function validateEmail() {
    const emailField = document.getElementById("emailAdresa");
    const emailPattern = /^[\w._%+-]+@gmail\.com$/;

    if (!emailPattern.test(emailField.value)) {
        alert("Email must be in the format ___@gmail.com");
        return false;
    }

    return true;
}

function validatePasswords(passwordField, confirmPasswordField) {
    if (passwordField.value !== confirmPasswordField.value) {
        alert("Passwords do not match.");
        return false;
    }
    return true;
}

function validateForm() {
    const korisnickoImeField = document.getElementById("korisnickoIme");
    const passwordField = document.getElementById("lozinka");
    const confirmPasswordField = document.getElementById("confirmPassword");
    const imeField = document.getElementById("ime");
    const prezimeField = document.getElementById("prezime");
    const datumRodjenjaField = document.getElementById("datumRodjenja");
    const adresaField = document.getElementById("adresa");
    const brojTelefonaField = document.getElementById("brojTelefona");


    if (!validateUsernameLength(korisnickoImeField,"Username")) return false;
    if (!validateUsernameLength(passwordField, "Password")) return false;
    if (!validateNotEmpty(confirmPasswordField, "Confirm Password")) return false;
    if (!validateNotEmpty(imeField, "First Name")) return false;
    if (!validateNotEmpty(prezimeField, "Last Name")) return false;
    if (!validateNotEmpty(datumRodjenjaField, "Birth Date")) return false;
    if (!validateNotEmpty(adresaField, "Address")) return false;
    if (!validateNotEmpty(brojTelefonaField, "Phone Number")) return false;

    if (!validatePasswords(passwordField, confirmPasswordField)) return false;

    return validateEmail();
}
