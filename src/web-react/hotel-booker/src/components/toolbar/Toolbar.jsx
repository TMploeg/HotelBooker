import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../stylesheets/icons.css";
import DroppedButton from "../general/dropped-button";
import "./Toolbar.css";
import AccountMenu from "./account-menu/AccountMenu";

export default function Toolbar() {
    const navigate = useNavigate();

    const [menuVisible, setMenuVisible] = useState(false);

    return <div>
        <div className="toolbar-container">
            <div className="toolbar-left">
                <span className="title" onClick={() => navigate('/')}>Hotel Booker</span>
            </div>
            <div className="toolbar-right">
                <DroppedButton onClick={toggleAccountMenu}>
                    <img className="medium-icon" src="/images/account_icon.png" />
                    <span>Not logged in</span>
                </DroppedButton>
            </div>
        </div>
        <AccountMenu
            isVisible={menuVisible}
            isLoggedIn={false}
            onLogoutClicked={onLogoutClicked}
            onRegisterClicked={onRegisterClicked}
            onLoginClicked={onLoginClicked} />
    </div>

    function toggleAccountMenu() {
        setMenuVisible(visible => !visible);
    }

    function onLogoutClicked() {
        console.log('onLogoutClicked not implemented')
        setMenuVisible(false);
    }

    function onRegisterClicked() {
        navigate('/register');
        setMenuVisible(false);
    }

    function onLoginClicked() {
        navigate('/login');
        setMenuVisible(false);
    }
}