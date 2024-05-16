import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ClipLoader } from "react-spinners";
import useCSSProperties from "../../../hooks/useCSSProperties.js";
import ApiService from "../../../services/ApiService.js";
import FlatButton from "../../general/flat-button";
import "./HotelInfo.css";

export default function HotelInfo() {
    const { id } = useParams();
    const [hotel, setHotel] = useState(null);
    const { getProperty } = useCSSProperties();
    const navigate = useNavigate();

    useEffect(() => loadHotel, []);

    if (!hotel) {
        return <ClipLoader color={getProperty('--default-color')} />
    }

    return <div>
        <h1 className="hotel-name">{hotel.name}</h1>
        <div className="hotel-address">{hotel.address}</div>
        <FlatButton onClick={() => navigate(`hotels/${id}/book`)} className="test-flat-button">Book</FlatButton>
    </div>

    function loadHotel() {
        ApiService
            .get(`hotels/${id}`)
            .then(response => setHotel(response.body));
    }
}