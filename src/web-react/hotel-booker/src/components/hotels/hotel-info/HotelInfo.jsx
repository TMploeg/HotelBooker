import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ClipLoader } from "react-spinners";
import useApi from "../../../hooks/useApi.js";
import useCSSProperties from "../../../hooks/useCSSProperties.js";
import FlatButton from "../../general/flat-button";
import "./HotelInfo.css";

export default function HotelInfo() {
    const { id } = useParams();
    const [hotel, setHotel] = useState(null);
    const { getProperty } = useCSSProperties();
    const navigate = useNavigate();

    const { get } = useApi();

    useEffect(() => loadHotel, []);

    if (!hotel) {
        return <ClipLoader color={getProperty('--default-color')} />
    }

    return <div>
        <h1 className="hotel-name">{hotel.name}</h1>
        <div className="hotel-address">{hotel.address}</div>
        <FlatButton onClick={() => navigate('book')} className="test-flat-button">Book</FlatButton>
    </div>

    function loadHotel() {
        get(`hotels/${id}`).then(response => setHotel(response.data));
    }
}