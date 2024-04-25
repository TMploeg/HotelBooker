import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import Home from "./components/home";
import HotelInfo from "./components/hotels/hotel-info";
import HotelList from "./components/hotels/hotel-list";
import Toolbar from "./components/toolbar/Toolbar";

export default function App() {
  return (
    <div className="app-container">
      <BrowserRouter>
        <Toolbar />
        <div className="content-container">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/hotels" element={<HotelList />} />
            <Route path="/hotels/:id" element={<HotelInfo />} />
          </Routes>
        </div>
      </BrowserRouter>
    </div>
  )
}