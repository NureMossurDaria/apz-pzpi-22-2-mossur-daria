import axios from 'axios';
import CONSTANTS from './Constants';

const axiosInstance = axios.create({
  baseURL: CONSTANTS.API_PATH,
});

export default axiosInstance;