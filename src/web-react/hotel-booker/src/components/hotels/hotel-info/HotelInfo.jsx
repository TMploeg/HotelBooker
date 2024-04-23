import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ApiService from "../../../services/ApiService.js";

export default function HotelInfo() {
    const { id } = useParams();
    const apiService = new ApiService();
    const [hotel, setHotel] = useState(null);

    useEffect(() => loadHotel, []);

    if (hotel) {
        return <div>
            <h1 className="hotel-name">{hotel.name}</h1>
            <div className="hotel-address">{hotel.address}</div>
        </div>
    }

    return <div>
        Loading...
    </div>

    function loadHotel() {
        apiService
            .get(`hotels/${id}`)
            .onComplete(response => setHotel(response.body))
    }
}