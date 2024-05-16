import { useEffect, useState } from "react";
import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import "./App.css";
import Login from "./components/auth/login";
import Register from "./components/auth/register";
import HotelInfo from "./components/hotels/hotel-info";
import HotelList from "./components/hotels/hotel-list";
import Toolbar from "./components/toolbar/Toolbar";
import UserService from "./services/UserService";

export default function App() {
  const [isLoggedIn, setLoggedIn] = useState(false);
  useEffect(onAuthenticationUpdated, []);
  const navigate = useNavigate();

  window.addEventListener('load', preventImageDrag);

  return (
    <div className="app-container">
      <Toolbar onLogout={onAuthenticationUpdated} isLoggedIn={isLoggedIn} />
      <div className="content-container">{getRoutes()}</div>
    </div>
  )

  function preventImageDrag() {
    for (let element of document.getElementsByTagName('img')) {
      element.setAttribute('draggable', 'false');
    }
  }

  function onAuthenticationUpdated() {
    UserService.isLoggedIn().then(
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
        isLoggedIn
          ? <>
            <Route path="/" element={<Navigate to="hotels" replace={true} />} />
            <Route path="/hotels" element={<HotelList />} />
            <Route path="/hotels/:id" element={<HotelInfo />} />
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