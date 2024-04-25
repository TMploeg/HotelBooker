import { useNavigate } from "react-router-dom";
import "./Toolbar.css";

export default function Toolbar() {
    const navigate = useNavigate();

    return <div className="toolbar-container">
        <div className="toolbar-left">
            <span className="title" onClick={() => navigate('/')}>Hotel Booker</span>
        </div>
        <div className="toolbar-right"></div>
    </div>
}