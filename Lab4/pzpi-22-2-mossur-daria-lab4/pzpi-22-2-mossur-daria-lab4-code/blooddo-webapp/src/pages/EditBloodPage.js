import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';
import { localizeBloodStatus, localizeBloodTypeAndRhesus } from '../misc/formatters';

const EditBloodPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  useAxiosInterceptors(navigate);

  const [blood, setBlood] = useState(null);

  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  useEffect(() => {
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/blood/' + id, {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => setBlood(res.data))
    .catch((err) => console.log(err));
  }, []);

  const handleSpoil = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    axiosInstance.put(CONSTANTS.API_PATH + '/medic/blood/' + blood.id + '/spoil', {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setBlood({ ...blood, spoiled: true });
      setSuccess(t("data_submitted"));
    })
    .catch((err) => {
      console.log(err);
      setError(t("generic_error"));
    });
  };

  const handleUpdateStatus = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    axiosInstance.put(CONSTANTS.API_PATH + '/medic/blood/' + blood.id + '/status/' + blood.status, {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setSuccess(t("data_submitted"));
    })
    .catch((err) => {
      console.log(err);
      setError(t("generic_error"));
    });
  };

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-xl p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-4">{t("edit_blood_header")}</h2>

        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        {success && <p className="text-black text-sm mb-4">{success}</p>}

        {blood && 
          <div>
            <p><b className='font-mono'>{localizeBloodTypeAndRhesus(blood.bloodType, blood.rhesus, lng)}</b> : {localizeBloodStatus(blood.status, t)}</p>
            <p>{t("blood_barcode")}: {blood.barcode}</p>
            <p>{t("blood_spoiled")}: {blood.spoiled === true ? t("generic_yes_label") : t("generic_no_label")}</p>
            <p>{blood.fridge.address}</p>

            <label className="mt-4 block text-2x1 text-black font-bold mb-2 w-50">{t("blood_status")}</label>
            <div className="flex items-center space-x-4">
              <select 
                value={blood.status} 
                onChange={(e) => setBlood({
                  ...blood,
                  status: e.target.value
                })} 
                className="w-full px-4 py-2 border-2 border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-red-400"
              >
                <option value="AVAILABLE" key="AVAILABLE">{t("blood_status_available")}</option>
                <option value="RESERVED" key="RESERVED">{t("blood_status_reserved")}</option>
                <option value="USED" key="USED">{t("blood_status_used")}</option>
                <option value="TRASHED" key="TRASHED">{t("blood_status_trashed")}</option>
              </select>
              <button
                onClick={handleUpdateStatus}
                className="w-full px-4 py-3 bg-red-500 text-white font-bold rounded-lg disabled:opacity-25 hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
              >
              {t("blood_update_status")}
              </button>
            </div>

            <button
              disabled = {blood.spoiled !== false}
              onClick={handleSpoil}
              className="w-full mt-4 px-4 py-3 bg-red-500 text-white font-bold rounded-lg disabled:opacity-25 hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("edit_blood_spoil")}
            </button>
            <button
              onClick={() => navigate('/blood-supply')}
              className="w-full mt-4 px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("generic_back_button")}
            </button>
          </div>
        }
      </div>
    </div>
  );
};

export default EditBloodPage;