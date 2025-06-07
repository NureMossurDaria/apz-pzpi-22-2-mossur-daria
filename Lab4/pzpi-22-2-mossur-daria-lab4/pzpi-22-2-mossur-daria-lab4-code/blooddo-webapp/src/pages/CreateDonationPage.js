import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';
import { localizeEventStatus, localizeBloodTypeAndRhesus, localizeDateTime } from '../misc/formatters';

const CreateDonationPage = () => {
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  useAxiosInterceptors(navigate);
  const [ donationData, setDonationData ] = useState({
    donorId: '',
    eventId: '',
    blood: {
      bloodType: '',
      rhesus: '',
      spoiled: false,
      barcode: '',
      status: 'AVAILABLE',
      fridgeId: ''
    }
  });
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const [donorSearchError, setDonorSearchError] = useState(null);

  const [events, setEvents] = useState([]);
  const [fridges, setFridges] = useState([]);
  const [donorData, setDonorData] = useState(null);
  const [username, setUsername] = useState('');

  useEffect(() => {
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/event/all', {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setEvents(res.data);
    })
    .catch((err) => console.error(err));
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/fridge/all', {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setFridges(res.data);
    })
    .catch((err) => console.error(err));
  },[]);

  const handleDonorSearch = async (e) => {
    e.preventDefault();
    setDonorSearchError(null);
    axiosInstance.get(CONSTANTS.API_PATH + '/medic/donor/username/' + username, {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => {
      setDonorData(res.data);
      setDonationData((prevState) => ({
        ...prevState,
        donorId: res.data.id,
        blood: {
          ...prevState.blood,
          bloodType: res.data.bloodType,
          rhesus: res.data.rhesus
        }
      }));
    })
    .catch((err) => {
      console.log(err);
      setDonorSearchError(t("create_donation_donor_not_found"));
    });
  };

  const handleCreateDonation = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);
    axiosInstance.post(CONSTANTS.API_PATH + '/medic/donation/new', donationData, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    })
    .then((res) => setSuccess(t("data_submitted")))
    .catch((err) => setError(t("generic_error")));
  };

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-xl p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-6">{t("create_donation_button_label")}</h2>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        {success && <p className="text-black text-sm mb-4">{success}</p>}

        <h2 className="text-lg font-bold mb-2">{t("create_donation_select_event")}</h2>

        <div className="overflow-y-auto w-full max-w-xl max-h-[80vh] mb-4 p-6 bg-white shadow-lg rounded-lg">
          {events &&
            events.map((event, i) => (
              <div>
                <input type="radio" 
                  id={`optionEvent${i}`} 
                  value="" 
                  class="hidden peer" 
                  name="event" 
                  onChange={(e) => 
                    setDonationData({
                      ...donationData,
                      eventId: event.id
                    })
                  }/>
                <label
                  for={`optionEvent${i}`}
                  className="block text-black font-medium mb-2 w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400 rounded-lg cursor-pointer peer-checked:border-red-400 peer-checked:bg-red-50"
                >
                  <p>{event.eventAddress}</p>
                  <p>{localizeDateTime(event.dateTime, lng)}</p>
                  <p>{t("event_status")}: {localizeEventStatus(event.status, t)}</p>
                  <p>{t("event_notes")}: {event.notes}</p>
                  <p>{t("event_appointments_count")}: {event.appointmentsCount}</p>
                </label>
              </div>
            ))
          }
        </div>

        <h2 className="text-lg font-bold mb-2">{t("create_donation_select_donor")}</h2>

        <div className="w-full max-w-xl mb-4 p-6 bg-white shadow-lg rounded-lg">
          {donorData ?
            <div className="flex justify-between items-center space-x-4">
              <div>
                <p>{donorData.firstName} {donorData.lastName}</p>
                <p>{localizeBloodTypeAndRhesus(donorData.bloodType, donorData.rhesus, lng)}</p>
                <p>{donorData.phone}</p>
              </div>
              <button onClick={(e) => {
                setDonorData(null);
                setDonationData((prevState) => ({
                  ...prevState,
                  donorId: '',
                  blood: {
                    ...prevState.blood,
                    bloodType: '',
                    rhesus: ''
                  }
                }));
              }}
                className="px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400">
                X
              </button>
            </div>
            : 
            <div className="flex items-center space-x-4">
              {/* <label className="block text-2x1 text-black font-bold mb-2"></label> */}
              <input 
                type="text" 
                placeholder={t("donor_data_username")}
                value={username} 
                onChange={(e) => setUsername(e.target.value)} 
                className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
              />
              <button onClick={handleDonorSearch}
                className="px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400">
                {t("generic_search_button")}
              </button>
            </div>
          }
        </div>

        <h2 className="text-lg font-bold mb-2">{t("create_donation_select_fridge")}</h2>

        <div className="overflow-y-auto w-full max-w-xl max-h-[40vh] mb-4 p-6 bg-white shadow-lg rounded-lg">
          {fridges &&
            fridges
            .filter((fridge, i) => (fridge.enabled))
            .map((fridge, i) => (
              <div>
                <input type="radio" 
                  id={`optionFridge${i}`} 
                  value="" 
                  class="hidden peer" 
                  name="fridge" 
                  onChange={(e) => 
                    setDonationData((prevState) => ({
                      ...prevState,
                      blood: {
                        ...prevState.blood,
                        fridgeId: fridge.id
                      }
                    }))
                  }
                  />
                <label
                  for={`optionFridge${i}`}
                  className="block text-black font-medium mb-2 w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400 rounded-lg cursor-pointer peer-checked:border-red-400 peer-checked:bg-red-50"
                >
                  <p>{fridge.serialNumber}</p>
                  <p>{fridge.address}</p>
                  <p>{t("fridge_notes")}: {fridge.notes}</p>
                  <p>{t("fridge_allowed_temperatures")}{fridge.tempCelsiusMin} - {fridge.tempCelsiusMax}</p>
                  <p>{t("fridge_allowed_humidity")}{fridge.humidityPercentMin} - {fridge.humidityPercentMax}</p>
                </label>
              </div>
            ))
          }
        </div>

        <h2 className="text-lg font-bold mb-2">{t("create_donation_blood_barcode")}</h2>
        <div>
          <input 
            type="text" 
            value={donationData.blood.barcode} 
            onChange={(e) => setDonationData((prevState) => ({
              ...prevState,
              blood: {
                ...prevState.blood,
                barcode: e.target.value
              }
            }))} 
            className="w-full p-3 mb-4 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
          />
        </div>

        <button
          disabled = {success !== null}
          onClick={handleCreateDonation}
          className="w-full py-3 bg-red-500 disabled:opacity-25 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
        >
          {t("submit_button")}
        </button>
      </div>
    </div>
  );
};

export default CreateDonationPage;