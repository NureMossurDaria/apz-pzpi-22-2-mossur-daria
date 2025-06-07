import './LanguageDropDown.js';
import LanguageDropDown from './LanguageDropDown.js';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import { handleLogout } from '../misc/logout.js';

const Header = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    return (
        <div className="fixed z-50 w-full h-23 bg-white">
            <LanguageDropDown />
            { localStorage.getItem(CONSTANTS.TOKEN) && 
                <div className="fixed top-6 right-34">
                    <button 
                        onClick={ (e) => {
                            navigate('/home');
                        }}
                        className="w-full px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
                        >
                            {t("home_button_label")}
                        </button>
                </div>
            }
            { localStorage.getItem(CONSTANTS.TOKEN) && 
                <div className="fixed top-6 right-6">
                    <button 
                        onClick={ (e) => {
                            handleLogout(navigate);
                        }}
                        className="w-full px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
                        >
                            {t("logout_button_label")}
                        </button>
                </div>
            }
        </div>
    );
};

export default Header;