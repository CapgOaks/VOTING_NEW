import config from "../config/config.js";
import { getToken, decodeJWT, getUserId, getUserName, getUserEmail, getUserType } from "../utils/jwtUtils.js";

async function loadAdminProfile() {
  try {
    const token = getToken();
    const decoded = decodeJWT(token);
    const userId = decoded.userid;

    const response = await fetch(`${config.API_BASE_URL}/users/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("Failed to fetch admin profile");
    }

    const profile = await response.json();

    document.getElementById("userId").textContent = profile.user_id || getUserId();
    document.getElementById("userName").textContent = profile.user_name || getUserName();
    document.getElementById("userType").textContent = profile.user_type || getUserType();
  } catch (error) {
    console.error("Error loading admin profile:", error);
    const main = document.getElementById("main-content") || document.body;
    main.innerHTML += `<div class='alert alert-danger mt-3'>Failed to load admin profile.</div>`;
  }
}

export function init() {
  loadAdminProfile();
}
