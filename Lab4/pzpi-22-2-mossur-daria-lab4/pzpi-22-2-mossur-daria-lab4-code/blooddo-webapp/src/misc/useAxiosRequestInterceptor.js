import { useEffect } from 'react';
import axiosInstance from './axiosInstance';
import CONSTANTS from './Constants';

export const useAxiosRequestInterceptor = (navigate) => {
    useEffect(() => {
      const requestInterceptor = axiosInstance.interceptors.request.use(
        (config) => {
          const token = localStorage.getItem(CONSTANTS.TOKEN);
          if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
          }
          return config;
        },
        (error) => {
          return Promise.reject(error);
        }
      );
  
      return () => {
        axiosInstance.interceptors.request.eject(requestInterceptor);
      };
    }, [navigate]);
  }
