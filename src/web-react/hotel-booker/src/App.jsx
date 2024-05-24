import { useEffect, useState } from "react";
import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import "./App.css";
import Login from "./components/auth/login";
import Register from "./components/auth/register";
import BookingInfo from "./components/bookings/booking-info";
import BookingList from "./components/bookings/booking-list";
import HotelBookingForm from "./components/hotels/hotel-booking-form/HotelBookingForm";
import HotelInfo from "./components/hotels/hotel-info";
import HotelList from "./components/hotels/hotel-list";
import Toolbar from "./components/toolbar/Toolbar";
import useAuthentication from "./hooks/useAuthentication";

export default function App() {
  const [loggedIn, setLoggedIn] = useState(false);
  useEffect(onAuthenticationUpdated, []);
  const navigate = useNavigate();

  const { isLoggedIn } = useAuthentication();

  window.addEventListener('load', preventImageDrag);

  return (
    <div className="app-container">
      <Toolbar onLogout={onAuthenticationUpdated} loggedIn={loggedIn} />
      <div className="content-container">{getRoutes()}</div>
    </div>
  )

  function preventImageDrag() {
    for (let element of document.getElementsByTagName('img')) {
      element.setAttribute('draggable', 'false');
    }
  }

  function onAuthenticationUpdated() {
    isLoggedIn().then(
      loggedIn => {
        setLoggedIn(loggedIn);
        navigate('/');
      },
      console.error
    );
  }

  function getRoutes() {
    return <Routes>
      {
        loggedIn
          ? <>
            <Route path="/" element={<Navigate to="hotels" replace={true} />} />
            <Route path="/hotels" element={<HotelList />} />
            <Route path="/hotels/:id" element={<HotelInfo />} />
            <Route path="/hotels/:id/book" element={<HotelBookingForm />} />
            <Route path="/bookings" element={<BookingList />} />
            <Route path="/bookings/:id" element={<BookingInfo />} />
          </>
          : <>
            <Route path="/" element={<Navigate to="login" replace={true} />} />
            <Route path="/login" element={<Login onLogin={onAuthenticationUpdated} />} />
            <Route path="/register" element={<Register onRegister={onAuthenticationUpdated} />} />
          </>
      }
      <Route path="/*" element={<Navigate to="/" replace={true} />} />
    </Routes>
  }
}