import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';

const AllFridgesPage = () => {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  useAxiosInterceptors(navigate);

  const [fridges, setFridges] = useState([]);

  useEffect(() => {
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/fridge/all', {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => setFridges(res.data))
    .catch((err) => console.error(err));
  }, []);

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-xl p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-4">{t("all_fridges_button_label")}</h2>

        {fridges && 
          fridges.map((fridge, i) => (
            <div className="mt-2 w-full max-w-xl mb-4 p-6 bg-white shadow-lg rounded-lg">
              <p><b>{t("fridge_serial_number_desc")}: </b>{fridge.serialNumber}</p>
              <p><b>{t("fridge_address")}: </b>{fridge.address}</p>
              <p><b>{t("fridge_notes")}: </b>{fridge.notes}</p>
              <p><b>{t("fridge_allowed_temperatures")}</b>{fridge.tempCelsiusMin} - {fridge.tempCelsiusMax}</p>
              <p><b>{t("fridge_allowed_humidity")}</b>{fridge.humidityPercentMin} - {fridge.humidityPercentMax}</p>
              <p><b>{t("fridge_enabled")}: </b>{fridge.enabled === true ? t("generic_yes_label") : t("generic_no_label")}</p>

              <div className="flex items-center space-x-4 mt-2">
                <button
                  onClick={ () => navigate('/fridge-metrics/' + fridge.id)}
                  className="w-full mt-2 px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
                >
                  {t("fridge_metrics_label")}
                </button>
                {/* <button
                  onClick={ () => navigate('/edit-fridge/' + fridge.id)}
                  className="w-full mt-2 px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
                >
                  {t("generic_edit_button")}
                </button> */}
              </div>
            </div>
          ))
        }
      </div>
    </div>
  );
};

export default AllFridgesPage;