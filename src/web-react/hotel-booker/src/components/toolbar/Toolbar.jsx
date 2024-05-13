import { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../../services/UserService.js";
import "../../stylesheets/icons.css";
import IconButton from "../general/icon-button";
import "./Toolbar.css";
import AccountMenu from "./account-menu/AccountMenu";

export default function Toolbar({ onLogout, isLoggedIn }) {
    const navigate = useNavigate();

    const [menuVisible, setMenuVisible] = useState(false);

    return <div>
        <div className="toolbar-container">
            <div className="toolbar-left">
                <span className="title" onClick={() => navigate('/')}>Hotel Booker</span>
            </div>
            <div className="toolbar-right">
                <span>{isLoggedIn ? UserService.getUsername() : 'not logged in'}</span>
                <IconButton onClick={toggleAccountMenu} imgUrl="/images/account_icon_white.png" />
            </div>
        </div>
        <AccountMenu
            isVisible={menuVisible}
            isLoggedIn={isLoggedIn}
            onLogoutClicked={onLogoutClicked}
            onRegisterClicked={onRegisterClicked}
            onLoginClicked={onLoginClicked} />
    </div>

    function toggleAccountMenu() {
        setMenuVisible(visible => !visible);
    }

    function onLogoutClicked() {
        UserService.logout();
        navigate('/login');
        setMenuVisible(false);

        if (onLogout) {
            onLogout();
        }
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