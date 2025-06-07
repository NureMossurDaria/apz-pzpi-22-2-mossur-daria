import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CONSTANTS from '../misc/Constants';
import axiosInstance from '../misc/axiosInstance';
import { useTranslation } from 'react-i18next';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const { t } = useTranslation();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const payload = {
          username: username,
          password: password,
          };
      const response = await axiosInstance.post(CONSTANTS.API_PATH + '/auth/login', payload, { 
        headers: {
            'Content-Type': 'application/json',
          }
      });
      if (response.status === 200) {
        localStorage.setItem(CONSTANTS.TOKEN, response.data.token);
        localStorage.setItem(CONSTANTS.ROLES, JSON.stringify(response.data.roles));
        localStorage.setItem(CONSTANTS.PI_AGREED, response.data.piAgreed);
        navigate('/home');
      }
    } catch (err) {
      setError(t("login_failed"));
    }
  };

  useEffect(() => {
    if (localStorage.getItem(CONSTANTS.TOKEN)) {
      navigate("/home");
    }
  });

  return (
    <div className="py-23 flex justify-center items-center min-h-screen bg-white">
      <div className="w-full max-w-md p-6 bg-white shadow-lg rounded-lg">
        <h2 className="text-4xl font-bold text-black mb-6">{t("welcome")}</h2>
        {error && <p className="text-red-500 text-sm mb-4">{error}</p>}
        
        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label className="block text-black font-medium mb-2">{t("username_label")}</label>
            <input 
              type="text" 
              value={username} 
              onChange={(e) => setUsername(e.target.value)} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          
          <div>
            <label className="block text-black font-medium mb-2">{t("password_label")}</label>
            <input 
              type="password" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              className="w-full p-3 border-2 border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-400"
            />
          </div>
          
          <button 
            type="submit" 
            className="w-full py-3 bg-red-500 text-white font-bold rounded-lg hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400"
          >
            {t("login_button_label")}
          </button>
        </form>
      </div>
    </div>
  );
};

export default LoginPage;
