import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { localizeBloodType, localizeRhesus } from '../misc/formatters';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';

const CreateDonorPage = () => {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  useAxiosInterceptors(navigate);
  const [ donorData, setDonorData ] = useState({
    username: '',
    firstName: '',
    lastName: '',
    bloodType: 'O',
    rhesus: false,
    phone: ''
  });
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleCreateUser = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    axiosInstance.post(CONSTANTS.API_PATH + '/medic/donor/new', donorData, {
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
        <h2 className="text-4xl font-bold text-black mb-6">{t("create_donor_button_label")}</h2>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        {success && <p className="text-black text-sm mb-4">{success}</p>}

        <form onSubmit={handleCreateUser} className="space-y-4">
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("donor_data_username")}</label>
            <input 
              type="text" 
              value={donorData.username} 
              onChange={(e) => setDonorData({
                ...donorData,
                username: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("donor_data_first_name")}</label>
            <input 
              type="text" 
              value={donorData.firstName} 
              onChange={(e) => setDonorData({
                ...donorData,
                firstName: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("donor_data_last_name")}</label>
            <input 
              type="text" 
              value={donorData.lastName} 
              onChange={(e) => setDonorData({
                ...donorData,
                lastName: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div className="flex items-center space-x-4">
            <label className="block text-2x1 text-black font-bold mb-2 w-50">{t("donor_data_blood_type")}</label>
            <select 
              value={donorData.bloodType} 
              onChange={(e) => setDonorData({
                ...donorData,
                bloodType: e.target.value
              })} 
              className="w-full px-4 py-2 border-2 border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              <option value="O" key="O">{localizeBloodType("O", lng)}</option>
              <option value="A" key="A">{localizeBloodType("A", lng)}</option>
              <option value="B" key="B">{localizeBloodType("B", lng)}</option>
              <option value="AB" key="AB">{localizeBloodType("AB", lng)}</option>
            </select>
          </div>
          <div className="flex items-center space-x-4">
            <label className="block text-2x1 text-black font-bold mb-2 w-50">{t("donor_data_rhesus")}</label>
            <select 
              value={donorData.rhesus} 
              onChange={(e) => setDonorData({
                ...donorData,
                rhesus: (e.target.value === 'true')
              })} 
              className="w-full px-4 py-2 border-2 border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              <option value="false" key="false">{localizeRhesus(false)}</option>
              <option value="true" key="true">{localizeRhesus(true)}</option>
            </select>
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("donor_data_phone")}</label>
            <input 
              type="text" 
              value={donorData.phone} 
              onChange={(e) => setDonorData({
                ...donorData,
                phone: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
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

export default CreateDonorPage;