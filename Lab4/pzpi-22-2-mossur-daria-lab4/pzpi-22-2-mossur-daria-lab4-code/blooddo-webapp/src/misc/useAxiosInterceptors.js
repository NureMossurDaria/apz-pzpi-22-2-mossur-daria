import { useEffect } from 'react';
import axiosInstance from './axiosInstance';
import CONSTANTS from './Constants';
import { handleLogout } from './logout';

export const useAxiosInterceptors = (navigate) => {
    useEffect(() => {
      const requestInterceptor = axiosInstance.interceptors.request.use(
        (config) => {
          const token = localStorage.getItem(CONSTANTS.TOKEN);
          if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
          }
          //config.headers['Access-Control-Allow-Origin'] = '*';
          //config.headers['Access-Control-Allow-Methods'] = 'POST, GET, PUT';
          //config.headers['Access-Control-Allow-Headers'] = 'Content-Type';
          return config;
        },
        (error) => {
          return Promise.reject(error);
        }
      );
  
      const responseInterceptor = axiosInstance.interceptors.response.use(
        (response) => response,
        (error) => {
          if (error.response && (error.response.status === 401)) {
            handleLogout(navigate);
          }
          return Promise.reject(error);
        }
      );
  
      return () => {
        axiosInstance.interceptors.request.eject(requestInterceptor);
        axiosInstance.interceptors.response.eject(responseInterceptor);
      };
    }, [navigate]);
  }
