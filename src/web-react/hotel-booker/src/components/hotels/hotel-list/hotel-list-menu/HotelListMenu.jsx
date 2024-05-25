import { useState } from "react";
import IconButton from "../../../general/icon-button";
import InputField from "../../../general/input-field";
import "./HotelListMenu.css";

export default function HotelListMenu({ onSearch, onClear }) {
    const [nameSearch, setNameSearch] = useState('');
    const [citySearch, setCitySearch] = useState('');

    return <>
        <div className="hotel-list-menu-container">
            <InputField label="search" value={nameSearch} onValueChanged={setNameSearch} />
            <InputField label="city" value={citySearch} onValueChanged={setCitySearch} />
            <IconButton imgUrl="/images/search_icon.png" onClick={() => onSearch?.(nameSearch, citySearch)} />
            <IconButton imgUrl="/images/clear_icon.webp" onClick={clear} />
        </div>
        <div className="h-seperator"></div>
    </>

    function clear() {
        setNameSearch('');
        setCitySearch('');
        onClear?.();
    }
}