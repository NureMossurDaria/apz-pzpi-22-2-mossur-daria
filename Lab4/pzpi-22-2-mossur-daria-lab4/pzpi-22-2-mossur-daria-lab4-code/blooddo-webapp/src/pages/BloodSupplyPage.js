import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';
import { localizeBloodStatus, localizeBloodTypeAndRhesus } from '../misc/formatters';

const BloodSupplyPage = () => {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  useAxiosInterceptors(navigate);

  const [bloods, setBloods] = useState([]);

  useEffect(() => {
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/blood/all', {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => setBloods(res.data))
    .catch((err) => console.error(err));
  }, []);

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-xl p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-4">{t("blood_supplies_button_label")}</h2>
        {bloods && 
          bloods.map((blood, i) => (
            <div className="mt-2 w-full max-w-xl mb-4 p-6 bg-white shadow-lg rounded-lg">
              <p><b className='font-mono'>{localizeBloodTypeAndRhesus(blood.bloodType, blood.rhesus, lng)}</b> : {localizeBloodStatus(blood.status, t)}</p>
              <p>{t("blood_barcode")}: {blood.barcode}</p>
              <p>{t("blood_spoiled")}: {blood.spoiled === true ? t("generic_yes_label") : t("generic_no_label")}</p>
              <p>{blood.fridge.address}</p>
              <button
                onClick={ () => navigate('/edit-blood/' + blood.id)}
                className="w-full mt-2 px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
              >
                {t("generic_edit_button")}
              </button>
            </div>
          ))
        }
      </div>
    </div>
  );
};

export default BloodSupplyPage;