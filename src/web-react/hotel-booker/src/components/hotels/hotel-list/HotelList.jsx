import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { ClipLoader } from "react-spinners";
import useCSSProperties from "../../../hooks/useCSSProperties";
import useHotels from "../../../hooks/useHotels";
import "./HotelList.css";
import HotelListMenu from "./hotel-list-menu/HotelListMenu";

export default function HotelList() {
    const { getAll, search } = useHotels();
    const { getProperty } = useCSSProperties();

    const [hotels, setHotels] = useState(null);
    useEffect(loadHotels, [])

    return <div className="list-container">
        <HotelListMenu onSearch={onSearch} onClear={loadHotels} />
        <h1 className="list-header">Hotels</h1>
        <div className="list">
            {
                hotels != null
                    ? hotels.length > 0
                        ? hotels.map((hotel, index) => <HotelListItem key={index} hotel={hotel} />)
                        : 'No hotels found'
                    : <ClipLoader color={getProperty('--default-color')} />
            }
        </div>
    </div>

    function loadHotels() {
        getAll()
            .catch(_ => setHotels([]))
            .then(response => setHotels(response.data));
    }

    function onSearch(name, city) {
        search(name, city)
            .catch(_ => alert('unknown error occurred while searching'))
            .then(response => setHotels(response.data));
    }
}

function HotelListItem({ hotel }) {
    return (
        <Link to={`${hotel.id}`} className="list-item">
            <div className="list-item-title">{hotel.name}</div>
            <div className="list-item-content">{hotel.address.city}</div>
        </Link>
    )
}