import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from "./pages/HomePage.jsx";
import CancelBookingPage from "./pages/CancelBookingPage.jsx";
import CanceledBookingPage from "./pages/CanceledBookingPage.jsx";

const App = () => {
    return (
        <BrowserRouter>
        <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/bookings/:id" element={<CancelBookingPage />} />
            <Route path="/bookings/canceled" element={<CanceledBookingPage />} />
        </Routes>
    </BrowserRouter>);
};

export default App;