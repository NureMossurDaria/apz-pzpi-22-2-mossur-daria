import CONSTANTS from './Constants';

export const handleLogout = (navigate) => {
    localStorage.removeItem(CONSTANTS.TOKEN);
    localStorage.removeItem(CONSTANTS.ROLES);
    localStorage.removeItem(CONSTANTS.PI_AGREED);
    navigate('/');
}