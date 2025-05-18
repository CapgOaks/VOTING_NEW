// js/services/api.js

import config from '../config/config.js';
import { getToken } from '../utils/jwtUtils.js';

/**
 * Perform an HTTP request and return JSON.
 * Adds Authorization header if token exists.
 * @param {string} method
 * @param {string} endpoint
 * @param {object} [data]
 */
async function apiRequest(method, endpoint, data) {
	console.log('api service called')
    const url = `${config.API_BASE_URL}/${endpoint}`;
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const response = await fetch(url, {
        method,
        headers,
        body: data ? JSON.stringify(data) : undefined,
    });

    if (!response.ok) throw new Error(`Error ${response.status}`);
    return response.json();
}

/**
 * usage: api.post('auth/login', { username, password });
 * @param {string} endpoint
 */
export const api = {
    get: (endpoint) => apiRequest('GET', endpoint),
    post: (endpoint, data) => apiRequest('POST', endpoint, data),
    put: (endpoint, data) => apiRequest('PUT', endpoint, data),
    delete: (endpoint) => apiRequest('DELETE', endpoint),
};
