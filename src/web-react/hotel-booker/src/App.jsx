import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import "./App.css";
import HotelInfo from "./components/hotels/hotel-info";
import HotelList from "./components/hotels/hotel-list";
import Toolbar from "./components/toolbar/Toolbar";

export default function App() {

  window.addEventListener('load', preventImageDrag);

  return (
    <div className="app-container">
      <BrowserRouter>
        <Toolbar />
        <div className="content-container">
          <Routes>
            <Route path="/" element={<Navigate to="/hotels" replace={true} />} />
            <Route path="/hotels" element={<HotelList />} />
            <Route path="/hotels/:id" element={<HotelInfo />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  )

  function preventImageDrag() {
    for (let element of document.getElementsByTagName('img')) {
      element.setAttribute('draggable', 'false');
    }
  }
}