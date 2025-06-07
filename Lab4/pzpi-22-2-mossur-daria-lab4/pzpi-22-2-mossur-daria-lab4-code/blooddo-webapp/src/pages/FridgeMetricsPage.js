import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';
import { useAxiosInterceptors } from '../misc/useAxiosInterceptors';
import { localizeDateTime } from '../misc/formatters';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";


const FridgeMetricsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { t, i18n } = useTranslation();
  const lng = i18n.resolvedLanguage;
  useAxiosInterceptors(navigate);

  const [metrics, setMetrics] = useState([]);

  useEffect(() => {
    var currentDate = new Date();
    var toDate = currentDate.toISOString().split("T")[0];
    currentDate.setMonth(currentDate.getMonth() - 1);
    var fromDate = currentDate.toISOString().split("T")[0];

    axiosInstance.get(CONSTANTS.API_PATH + '/medic/fridge/' + id
      + '/metrics/from/' + fromDate + '/to/' + toDate, {
      headers: {
        'Accept': 'application/json'
      }
    })
    .then((res) => setMetrics(res.data))
    .catch((err) => console.log(err));
  }, []);

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-xl p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-4">{t("fridge_metrics_for_label")}</h2>

        {metrics && 
          <div>
            <div className="mb-6">
              <h3 className="text-2xl mb-4 font-semibold text-gray-800 mb-2">{t("fridge_temperature_chart_label")}</h3>
              <ResponsiveContainer width="100%" height={200}>
                <LineChart data={metrics}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="dateTime" tickFormatter={(date) => localizeDateTime(date, lng)} />
                  <YAxis />
                  <Tooltip labelFormatter={(date) => localizeDateTime(date, lng)} />
                  <Line type="monotone" dataKey="tempCelsius" stroke="#FF0000" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            </div>
            <div className="mb-6">
              <h3 className="text-2xl mb-4 font-semibold text-gray-800 mb-2">{t("fridge_humidity_chart_label")}</h3>
              <ResponsiveContainer width="100%" height={200}>
                <LineChart data={metrics}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="dateTime" tickFormatter={(date) => localizeDateTime(date, lng)}/>
                  <YAxis />
                  <Tooltip labelFormatter={(date) => localizeDateTime(date, lng)} />
                  <Line type="monotone" dataKey="humidityPercent" stroke="#007BFF" strokeWidth={2} />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>
        }

        <button
          onClick={() => navigate('/all-fridges')}
          className="w-full mt-4 px-4 py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
        >
          {t("generic_back_button")}
        </button>
      </div>
    </div>
  );
};

export default FridgeMetricsPage;