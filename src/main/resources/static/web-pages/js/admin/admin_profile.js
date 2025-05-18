// admin_profile.js
import { getStoredPayload } from '../utils/jwtUtils.js';

export function init() {
    // Simulate loading admin data (in real app, fetch from server)

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

    document.getElementById('editProfileBtn').addEventListener('click', () => {
        alert('Edit Profile clicked');
    });
}
