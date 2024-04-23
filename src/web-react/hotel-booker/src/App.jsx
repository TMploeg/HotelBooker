import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import HotelInfo from "./components/hotels/hotel-info";
import HotelList from "./components/hotels/hotel-list";

export default function App() {
  return (
    <div className="app-container">
      <BrowserRouter>
        <Routes>
          <Route path="hotels" element={<HotelList />} />
          <Route path="hotels/:id" element={<HotelInfo />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}