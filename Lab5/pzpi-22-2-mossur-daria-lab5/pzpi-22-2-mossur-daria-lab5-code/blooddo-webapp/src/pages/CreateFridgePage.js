import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';

const CreateFridgePage = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  useAxiosInterceptors(navigate);
  const [ fridgeData, setFridgeData ] = useState({
    serialNumber: '',
    address: '',
    notes: '',
    tempCelsiusMin: '',
    tempCelsiusMax: '',
    humidityPercentMin: '',
    humidityPercentMax: '',
    enabled: true
  });
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleCreateFridge = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    axiosInstance.post(CONSTANTS.API_PATH + '/medic/fridge/new', fridgeData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setSuccess(t("data_submitted"));
    })
    .catch((err) => {
      setError(t("generic_error"));
    });
  };

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-md p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-6">{t("create_fridge_button_label")}</h2>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        {success && <p className="text-black text-sm mb-4">{success}</p>}

        <form onSubmit={handleCreateFridge} className="space-y-4">
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("fridge_serial_number")}</label>
            <input 
              type="text" 
              value={fridgeData.serialNumber} 
              onChange={(e) => setFridgeData({
                ...fridgeData,
                serialNumber: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("fridge_address")}</label>
            <input 
              type="text" 
              value={fridgeData.address} 
              onChange={(e) => setFridgeData({
                ...fridgeData,
                address: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("fridge_notes")}</label>
            <input 
              type="text" 
              value={fridgeData.notes} 
              onChange={(e) => setFridgeData({
                ...fridgeData,
                notes: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("fridge_temp_min")}</label>
            <input 
              type="number" 
              step="0.1" 
              lang="en"
              value={fridgeData.tempCelsiusMin} 
              onChange={(e) => setFridgeData({
                ...fridgeData,
                tempCelsiusMin: parseFloat(e.target.value)
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("fridge_temp_max")}</label>
            <input 
              type="number" 
              step="0.1" 
              lang="en"
              value={fridgeData.tempCelsiusMax} 
              onChange={(e) => setFridgeData({
                ...fridgeData,
                tempCelsiusMax: parseFloat(e.target.value)
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("fridge_humidity_min")}</label>
            <input 
              type="number" 
              step="0.1" 
              lang="en"
              value={fridgeData.humidityPercentMin} 
              onChange={(e) => setFridgeData({
                ...fridgeData,
                humidityPercentMin: parseFloat(e.target.value)
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("fridge_humidity_max")}</label>
            <input 
              type="number" 
              step="0.1" 
              lang="en"
              value={fridgeData.humidityPercentMax} 
              onChange={(e) => setFridgeData({
                ...fridgeData,
                humidityPercentMax: parseFloat(e.target.value)
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div className="flex items-center space-x-4">
            <label className="block text-2x1 text-black font-bold mb-2 w-50">{t("fridge_enabled")}</label>
            <select
              value={fridgeData.enabled}
              onChange={(e) => setFridgeData({
                ...fridgeData,
                enabled: (e.target.value === 'true')
              })}
              className="w-full px-4 py-2 border-2 border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              <option value="false" key="false">{t("generic_no_label")}</option>
              <option value="true" key="true">{t("generic_yes_label")}</option>
            </select>
          </div>

          <button 
            disabled = {success !== null}
            type="submit" 
            className="w-full py-3 bg-red-500 disabled:opacity-25 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
          >
            {t("submit_button")}
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateFridgePage;