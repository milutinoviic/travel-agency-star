function validateEmail() {
    try {
        const emailField = document.getElementById("emailAdresa");
        const emailPattern = /^[\w._%+-]+@gmail\.com$/;

        if (!emailPattern.test(emailField.value)) {
            alert("Email must be in the format ___@gmail.com");
            return false;
        }

        return true;
    } catch (error) {
        alert("An error occurred during email validation.");
        return false;
    }
}

function validateNotEmpty(field, fieldName) {
    if (field.value.trim() === "") {
        alert(fieldName + " cannot be empty.");
        return false;
    }
    return true;
}

function validateForm() {
    console.log("validateForm is called");

    const imeField = document.getElementById("ime");
    const prezimeField = document.getElementById("prezime");
    const emailField = document.getElementById("emailAdresa");
    const passwordField = document.getElementById("newPassword");
    const confirmPasswordField = document.getElementById("confirmPassword");

    if (!validateNotEmpty(imeField, "Ime")) return false;
    if (!validateNotEmpty(prezimeField, "Prezime")) return false;
    if (!validateEmail()) return false;

    if (passwordField.value !== "" && passwordField.value !== confirmPasswordField.value) {
        alert("Lozinke se ne poklapaju.");
        return false;
    }

    return true;
}
