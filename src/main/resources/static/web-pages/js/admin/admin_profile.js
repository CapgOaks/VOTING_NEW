import { getStoredPayload } from '../utils/jwtUtils.js';

export function init() {
    const user = getStoredPayload();
    console.log(user);

    const adminData = {
        id: user.userid,
        email: user.email,
        username: user.sub,
        role: user.usertype
    };

    document.getElementById('adminid').textContent = adminData.id;
    document.getElementById('adminEmail').textContent = adminData.email;
    document.getElementById('adminName').textContent = adminData.username;
    document.getElementById('adminRole').textContent = adminData.role;
}
