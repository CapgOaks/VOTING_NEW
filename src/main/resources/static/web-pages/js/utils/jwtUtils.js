// js/utils/jwtUtils.js

const TOKEN_KEY = 'token';

/**
 * Save JWT to storage.
 * @param {string} token
 */
export function setToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

/**
 * Retrieve JWT from storage.
 * @returns {string|null}
 */
export function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

/**
 * Remove JWT from storage.
 */
export function clearToken() {
    localStorage.removeItem(TOKEN_KEY);
}

/**
 * Safely parse a JWT and return its decoded payload.
 *
 * @param {string} token - The JWT string.
 * @returns {object|null} The decoded payload object if valid, otherwise null.
 *
 * The function splits the JWT into its three parts,
 * decodes the payload part from base64url to a JSON string,
 * then parses and returns the JSON object.
 * If any step fails or the input is invalid, it returns null.
 */
export function parseJwt(token) {
    if (typeof token !== 'string') return null;
    const parts = token.split('.');
    if (parts.length !== 3) return null;

    try {
        const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = atob(base64);
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error('parseJwt error:', e);
        return null;
    }
}


/**
 * Get the expiration Date of the JWT.
 * @param {string} token
 * @returns {Date|null}
 */
export function getTokenExpirationDate(token) {
    const payload = parseJwt(token);
    if (!payload || typeof payload.exp !== 'number') return null;
    return new Date(payload.exp * 1000);
}

/**
 * Check if the JWT is expired (or invalid).
 * @param {string} token
 * @returns {boolean}
 */
export function isTokenExpired(token) {
    const expDate = getTokenExpirationDate(token);
    if (!expDate) return true;
    return Date.now() >= expDate.getTime();
}

/**
 * Convenience: Get stored token’s decoded payload.
 * @returns {object|null}
 */
export function getStoredPayload() {
    const token = getToken();
    return token ? parseJwt(token) : null;
}
