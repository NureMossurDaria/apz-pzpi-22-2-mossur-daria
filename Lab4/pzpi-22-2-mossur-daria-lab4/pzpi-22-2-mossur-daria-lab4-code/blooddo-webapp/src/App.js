import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import BloodNeedsPage from './pages/BloodNeedsPage';
import CreateDonorPage from './pages/CreateDonorPage';
import CreateEventPage from './pages/CreateEventPage';
import CreateFridgePage from './pages/CreateFridgePage';
import CreateDonationPage from './pages/CreateDonationPage';
import BloodSupplyPage from './pages/BloodSupplyPage';
import EditBloodPage from './pages/EditBloodPage';
import AllFridgesPage from './pages/AllFridgesPage';
import FridgeMetricsPage from './pages/FridgeMetricsPage';
import Header from './components/Header';
import useLocalizeDocumentAttributes from './misc/useLocalizeDocumentAttributes';
import './tailwind.css';

const App = () => {
  useLocalizeDocumentAttributes();

  return (
    <Router>
      <Header/>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/blood-needs" element={<BloodNeedsPage />} />
        <Route path="/create-donor" element={<CreateDonorPage />} />
        <Route path="/create-event" element={<CreateEventPage />} />
        <Route path="/create-fridge" element={<CreateFridgePage />} />
        <Route path="/create-donation" element={<CreateDonationPage />} />
        <Route path='/blood-supply' element={<BloodSupplyPage />} />
        <Route path='/edit-blood/:id' element={<EditBloodPage />} />
        <Route path='/all-fridges' element={<AllFridgesPage />} />
        <Route path='/fridge-metrics/:id' element={<FridgeMetricsPage />} />
        <Route path="*" element={<Navigate to="/home" replace />} />
      </Routes>
    </Router>
  );
};

export default App;
