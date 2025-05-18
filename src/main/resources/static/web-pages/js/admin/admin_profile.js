export function init() {
  loadAdminProfile();
}

import config from "../config/config.js";
import { getToken } from "../utils/jwtUtils.js";

async function loadUserProfile() {
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
			throw new Error("Failed to fetch user profile");
		}

		const profile = await response.json();

		const t = decoded.usertype.charAt(0).toUpperCase() + decoded.usertype.slice(1).toLowerCase();

		document.getElementById("userName").textContent = getUserName();
		document.getElementById("userEmail").textContent = getUserEmail();
		document.getElementById("userType").textContent = getUserType();
		document.getElementById("userId").textContent = getUserId();
	} catch (error) {
		console.error("Error loading admin_profile:", error);
		document.getElementById("main-content").innerHTML =
			"<div class='alert alert-danger'>Failed to load admin profile.</div>";
	}
}

// Exporting the init function
export function init() {
	loadUserProfile();
}
