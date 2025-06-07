import i18n from 'i18next';
import LanguageDetector from "i18next-browser-languagedetector";
import HttpApi from "i18next-http-backend";
import { initReactI18next } from 'react-i18next';
import { localizeBloodType } from './formatters';

export const supportedLngs = {
  uk: "Українська",
  en: "English",
};

i18n
  .use(HttpApi)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'uk',
    supportedLngs: Object.keys(supportedLngs),
    interpolation: {
      escapeValue: false,
    },
  });

i18n.services.formatter?.add("bloodType", localizeBloodType);

export default i18n;
