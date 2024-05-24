import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import useApi from "../../../hooks/useApi";
import "./HotelList.css";

export default function HotelList() {
    const { get } = useApi();

    const [hotels, setHotels] = useState([]);
    useEffect(loadHotels, [])

    return <div className="list-container">
        <h1 className="list-header">Hotels</h1>
        <div className="list">
            {hotels.map((hotel, index) => <HotelListItem key={index} hotel={hotel} />)}
        </div>
    </div>

    function loadHotels() {
        get('hotels')
            .catch(console.error)
            .then(response => setHotels(response.data));
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