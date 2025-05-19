const apiUrl = "http://localhost:8080/auth/register";

const form = document.getElementById("registrationForm");

const userNameInput = document.getElementById("userName");
const userEmailInput = document.getElementById("userEmail");
const passwordInput = document.getElementById("password");
const confirmPasswordInput = document.getElementById("confirmPassword");
const dobInput = document.getElementById("dob");
const roleInput = document.getElementById("role");

form.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Reset validation classes
    form.classList.remove('was-validated');

    // Custom validation: Date of birth must be in the past
    const dob = new Date(dobInput.value);
    const today = new Date();
    if (dob >= today) {
        dobInput.setCustomValidity("Date of birth must be in the past.");
    } else {
        dobInput.setCustomValidity("");
    }

    // Passwords must match
    if (passwordInput.value !== confirmPasswordInput.value) {
        confirmPasswordInput.setCustomValidity("Passwords do not match.");
    } else {
        confirmPasswordInput.setCustomValidity("");
    }

    // Trigger built-in validation
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    const userData = {
        userName: userNameInput.value.trim(),
        userEmail: userEmailInput.value.trim(),
        password: passwordInput.value.trim(),
        dob: dobInput.value.trim(),
        role: "user"
    };

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            alert("Registration successful!");
            form.reset();
            window.location.href = "login.html";
        } else {
            const errorText = await response.text();
            alert("Registration failed: " + errorText);
        }
    } catch (error) {
        console.error("Submission error:", error);
        alert("Could not connect to the server. Please try again.");
    }
});
