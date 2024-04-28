import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import ApiService from "../../../services/ApiService";
import "./HotelList.css";

export default function HotelList() {
    const [hotels, setHotels] = useState([]);
    useEffect(loadHotels, [])

    return <div className="list-container">
        <h1 className="list-header">Hotels</h1>
        <div className="list">
            {hotels.map((hotel, index) => <HotelListItem key={index} hotel={hotel} />)}
        </div>
    </div>

    function loadHotels() {
        ApiService
            .get('hotels', undefined)
            .onComplete(response => {
                if (response.succes) {
                    setHotels(response.body);
                }
                else {
                    console.error("GET HOTELS FAILED", response);
                }
            });
    }
}

function HotelListItem({ hotel }) {
    return (
        <Link to={`${hotel.id}`} className="list-item">
            <div className="list-item-title">{hotel.name}</div>
            <div className="list-item-content">{hotel.address}</div>
        </Link>
    )
}