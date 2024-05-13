import { useState } from "react";
import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import "./App.css";
import Login from "./components/auth/login";
import Register from "./components/auth/register";
import HotelInfo from "./components/hotels/hotel-info";
import HotelList from "./components/hotels/hotel-list";
import Toolbar from "./components/toolbar/Toolbar";
import UserService from "./services/UserService";

export default function App() {
  const [isLoggedIn, setLoggedIn] = useState(UserService.isLoggedIn());
  const navigate = useNavigate();

  window.addEventListener('load', preventImageDrag);

  return (
    <div className="app-container">
      <Toolbar onLogout={onAuthenticationUpdated} isLoggedIn={isLoggedIn} />
      <div className="content-container">
        <Routes>
          <Route path="/" element={<Navigate to={`/${isLoggedIn ? 'hotels' : 'login'}`} replace={true} />} />
          <Route path="/hotels" element={<HotelList />} />
          <Route path="/hotels/:id" element={<HotelInfo />} />
          <Route path="/login" element={<Login onLogin={onAuthenticationUpdated} />} />
          <Route path="/register" element={<Register onRegister={onAuthenticationUpdated} />} />
        </Routes>
      </div>
    </div>
  )

  function preventImageDrag() {
    for (let element of document.getElementsByTagName('img')) {
      element.setAttribute('draggable', 'false');
    }
  }

  function onAuthenticationUpdated() {
    setLoggedIn(UserService.isLoggedIn());
    navigate('/');
  }
}