import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { localizeBloodTypeAndRhesus } from '../misc/formatters';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';
import { handleLogout } from '../misc/logout';

const HomePage = () => {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  const [medicData, setMedicData] = useState(null);
  const [donorData, setDonorData] = useState(null);
  useAxiosInterceptors(navigate);
  const [showNotice, setShowNotice] = useState(false);

  const handleAgree = async () => {
    localStorage.setItem(CONSTANTS.PI_AGREED, "true");
    setShowNotice(false);
    axiosInstance.post(CONSTANTS.API_PATH + '/user/pi/agree')
    .catch((err) => {
      if (err.response) {
        console.error('Error fetching data - Response Error:', err.response);
      } else if (err.request) {
        console.error('Error fetching data - Request Error:', err.request);
      } else {
        console.error('Error fetching data - General Error:', err.message);
      }
    });
  };

  const handleDeleteSelf = async () => {
    axiosInstance.delete(CONSTANTS.API_PATH + '/user')
    .then((res) => {
      handleLogout(navigate);
    })
    .catch((err) => {
      if (err.response) {
        console.error('Error fetching data - Response Error:', err.response);
      } else if (err.request) {
        console.error('Error fetching data - Request Error:', err.request);
      } else {
        console.error('Error fetching data - General Error:', err.message);
      }
    });
  }

  useEffect(() => {
    const piAgreed = localStorage.getItem(CONSTANTS.PI_AGREED);
    if (piAgreed !== null && piAgreed === "false") {
      setShowNotice(true);
    }
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/self', {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setMedicData(res.data);
    })
    .catch((err) => {
      if (err.response) {
        console.error('Error fetching data - Response Error:', err.response);
      } else if (err.request) {
        console.error('Error fetching data - Request Error:', err.request);
      } else {
        console.error('Error fetching data - General Error:', err.message);
      }
    });
    axiosInstance.get(CONSTANTS.API_PATH + '/donor/self', {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setDonorData(res.data);
    })
    .catch((err) => {
      if (err.response) {
        console.error('Error fetching data - Response Error:', err.response);
      } else if (err.request) {
        console.error('Error fetching data - Request Error:', err.request);
      } else {
        console.error('Error fetching data - General Error:', err.message);
      }
    });
  }, [])

  return (
    <div className="relative ">
      {showNotice && (
        <div className="mt-23 long-text fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-40">
          <div className="bg-white rounded-lg shadow-lg w-full max-w-xl p-6 max-h-[80vh] overflow-y-auto">
            <h2 className="text-lg font-bold mb-4">{t("pi_notice_header")}</h2>
            <p className="mb-4">{t("pi_notice_body")}</p>
            <button
              onClick={handleAgree}
              className="w-full py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("agree_button")}
            </button>
            {JSON.parse(localStorage.getItem(CONSTANTS.ROLES)).every(r => r === 'DONOR') &&
              <button
              onClick={handleDeleteSelf}
              className="mt-2 w-full py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("delete_self_button")}
            </button>}
          </div>
        </div>
      )}

      <div className={`${showNotice ? "opacity-50 pointer-events-none z-30" : ""}`}>
        <div className="py-23 flex justify-center items-center min-h-screen bg-white">
          {medicData &&<div className="w-full max-w-md p-6 bg-white shadow-lg rounded-lg">
              <h2
              className="text-2xl font-bold text-black mb-6"
              >{t('home_page_greeting', { 
                firstName: medicData.firstName, 
                lastName: medicData.lastName 
                })}</h2>
            <div className="block text-black font-medium">{medicData.phone}</div>
            <div className="block text-black font-medium">{medicData.hospital.name}</div>
            <div className="block text-black font-medium mb-6">{medicData.hospital.address}</div>

            <button 
            onClick={ (e) => {
              navigate('/blood-needs');
            }}
              className="w-full py-3 mb-2 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("blood_needs_button_label")}
            </button>
            <button 
            onClick={ (e) => {
              navigate('/create-donor');
            }}
              className="w-full py-3 mb-2 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("create_donor_button_label")}
            </button>
            <button 
            onClick={ (e) => {
              navigate('/create-event');
            }}
              className="w-full py-3 mb-2 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("create_event_button_label")}
            </button>
            <button 
            onClick={ (e) => {
              navigate('/create-fridge');
            }}
              className="w-full py-3 mb-2 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("create_fridge_button_label")}
            </button>
            <button 
            onClick={ (e) => {
              navigate('/create-donation');
            }}
              className="w-full py-3 mb-2 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("create_donation_button_label")}
            </button>
            <button 
            onClick={ (e) => {
              navigate('/blood-supply');
            }}
              className="w-full py-3 mb-2 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("blood_supplies_button_label")}
            </button>
            <button 
            onClick={ (e) => {
              navigate('/all-fridges');
            }}
              className="w-full py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("all_fridges_button_label")}
            </button>
          </div>}
          {donorData &&<div className="w-full max-w-md p-6 bg-white shadow-lg rounded-lg">
              <h2
              className="text-2xl font-bold text-black mb-6"
              >{t('home_page_greeting', { 
                firstName: donorData.firstName, 
                lastName: donorData.lastName 
                })}</h2>
            <div className="block text-black font-medium">{donorData.phone}</div>
            <div className="block text-black font-medium">{localizeBloodTypeAndRhesus(donorData.bloodType, donorData.rhesus, lng)}</div>
            {/* TODO donor list of buttons */}
          </div>}
        </div>
      </div>
    </div>
  );
};

export default HomePage;
