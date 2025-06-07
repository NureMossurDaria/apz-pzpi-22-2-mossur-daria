import { useTranslation } from "react-i18next";
import { supportedLngs } from "../misc/i18n";
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../misc/axiosInstance';
import CONSTANTS from '../misc/Constants';
import { useAxiosRequestInterceptor } from "../misc/useAxiosRequestInterceptor";

const LanguageDropDown = () => {
    const { i18n } = useTranslation();
    useAxiosRequestInterceptor(useNavigate());
    const token = localStorage.getItem(CONSTANTS.TOKEN);

    const handleLangChange = async (lng) => {
        try {
            axiosInstance.post(CONSTANTS.API_PATH + '/user/language/' + lng)
              .catch((err) => {
                if (err.response) {
                  console.error('Error fetching data - Response Error:', err.response);
                } else if (err.request) {
                  console.error('Error fetching data - Request Error:', err.request);
                } else {
                  console.error('Error fetching data - General Error:', err.message);
                }
              });
        } catch (err) {}
    }
  
    return (
    <div className="fixed top-6 left-6">
        <select
            value={i18n.resolvedLanguage}
            onChange={(e) => {
                i18n.changeLanguage(e.target.value);
                if (token) {
                  handleLangChange(e.target.value);
                }
            }}
            className="px-4 py-2 border-2 border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-red-400"
        >
        {Object.entries(supportedLngs).map(([code, name]) => (
            <option value={code} key={code}>
            {name}
            </option>
        ))}
        </select>
    </div>
    );
  }

export default LanguageDropDown;