import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';

const CreateEventPage = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  useAxiosInterceptors(navigate);
  const [ eventData, setEventData ] = useState({
    dateTime: '',
    eventAddress: '',
    status: 'PLANNED',
    notes: ''
  });
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const handleCreateEvent = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    axiosInstance.post(CONSTANTS.API_PATH + '/medic/event/new', eventData, {
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
        <h2 className="text-4xl font-bold text-black mb-6">{t("create_event_button_label")}</h2>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        {success && <p className="text-black text-sm mb-4">{success}</p>}

        <form onSubmit={handleCreateEvent} className="space-y-4">
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("event_date_time")}</label>
            <input
              type="datetime-local"
              value={eventData.dateTime} 
              onChange={(e) => setEventData({
                ...eventData,
                dateTime: `${e.target.value}:00`
              })}
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("event_address")}</label>
            <input 
              type="text" 
              value={eventData.eventAddress} 
              onChange={(e) => setEventData({
                ...eventData,
                eventAddress: e.target.value
              })} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          <div className="flex items-center space-x-4">
            <label className="block text-2x1 text-black font-bold mb-2 w-50">{t("event_status")}</label>
            <select 
              value={eventData.status} 
              onChange={(e) => setEventData({
                ...eventData,
                status: e.target.value
              })} 
              className="w-full px-4 py-2 border-2 border-gray-300 rounded-md text-black focus:outline-none focus:ring-2 focus:ring-red-400"
            >
              <option value="PLANNED" key="PLANNED">{t("event_status_planned")}</option>
              <option value="ONGOING" key="ONGOING">{t("event_status_ongoing")}</option>
              <option value="CANCELED" key="CANCELED">{t("event_status_canceled")}</option>
              <option value="FINISHED" key="FINISHED">{t("event_status_finished")}</option>
            </select>
          </div>
          <div>
            <label className="block text-2x1 text-black font-bold mb-2">{t("event_notes")}</label>
            <input 
              type="text"
              value={eventData.notes} 
              onChange={(e) => setEventData({
                ...eventData,
                notes: e.target.value
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

export default CreateEventPage;