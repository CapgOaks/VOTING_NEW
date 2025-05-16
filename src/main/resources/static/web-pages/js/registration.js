const apiUrl = "http://localhost:8080/auth/register";  // URL to your backend endpoint
const form = document.getElementById("registrationForm");

const userNameInput = document.getElementById("userName");
const userEmailInput = document.getElementById("userEmail");
const passwordInput = document.getElementById("password");
const confirmPasswordInput = document.getElementById("confirmPassword");
const dobInput = document.getElementById("dob");
const roleInput = document.getElementById("role");

form.addEventListener("submit", async function (e) {
    e.preventDefault();  // Prevent form submission to handle it with JavaScript

    const userData = buildUserPayload();
    
    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData)
        });

        if (response.ok) {
            form.reset();
            window.location.href = "login.html"; // Redirect to login page
        } else {
            alert("Something went wrong while submitting the form.");
        }
    } catch (error) {
        console.error("Error during form submission:", error);
        alert("There was an issue connecting to the server.");
    }
});

function buildUserPayload() {
    const userData = {
        userName: userNameInput.value.trim(),
        userEmail: userEmailInput.value.trim(),
        password: passwordInput.value.trim(),
        dob: dobInput.value.trim(),
        role: "user"
    };

    return userData;
}
