import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { localizeBloodTypeAndRhesus } from '../misc/formatters';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';

const BloodNeedsPage = () => {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  useAxiosInterceptors(navigate);
  const [ bloodNeeds, setBloodNeeds ] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/hospital/needs')
    .then((res) => {
      setBloodNeeds(res.data);
    })
    .catch((err) => {
      setError(t("generic_error"));
    });
  }, [t]);

  const handleBloodNeedsSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    try {
      axiosInstance.put(CONSTANTS.API_PATH + '/medic/hospital/needs/update', bloodNeeds, { 
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
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-md p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-6">{t("blood_needs_header")}</h2>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        {success && <p className="text-black text-sm mb-4">{success}</p>}
        
        { bloodNeeds &&
          <form onSubmit={handleBloodNeedsSubmit} className="space-y-4">
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("O", false, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.oNegative} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  oNegative: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("O", true, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.oPositive} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  oPositive: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("A", false, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.aNegative} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  aNegative: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("A", true, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.aPositive} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  aPositive: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("B", false, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.bNegative} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  bNegative: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("B", true, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.bPositive} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  bPositive: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("AB", false, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.abNegative} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  abNegative: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>
            <div className="flex items-center space-x-4">
              <label className="block text-2x1 font-mono text-black font-bold mb-2 w-18">{localizeBloodTypeAndRhesus("AB", true, lng)}</label>
              <input 
                type="number" 
                step="0.1" 
                lang="en"
                value={bloodNeeds.abPositive} 
                onChange={(e) => setBloodNeeds({
                  ...bloodNeeds,
                  abPositive: parseFloat(e.target.value)
                })} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
            </div>

            <button 
              type="submit" 
              className="w-full py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              {t("submit_button")}
            </button>
          </form>
        }
      </div>
    </div>
  );
}

export default BloodNeedsPage;