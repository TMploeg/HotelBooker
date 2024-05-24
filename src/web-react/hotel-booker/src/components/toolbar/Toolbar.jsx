import { useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuthentication from "../../hooks/useAuthentication.js";
import "../../stylesheets/icons.css";
import IconButton from "../general/icon-button";
import "./Toolbar.css";
import AccountMenu from "./account-menu/AccountMenu";

export default function Toolbar({ onLogout, loggedIn }) {
    const navigate = useNavigate();
    const { logout, getUsername } = useAuthentication();

    const [menuVisible, setMenuVisible] = useState(false);

    return <div>
        <div className="toolbar-container">
            <div>
                <span className="toolbar-link" onClick={() => navigate('/')}>Hotel Booker</span>
                <div className="v-seperator"></div>
                <span className="toolbar-link" onClick={() => navigate('/')}>home</span>
                <span className="toolbar-link" onClick={() => navigate('/bookings')}>bookings</span>
            </div>
            <div>
                <span>{loggedIn ? getUsername() : 'not logged in'}</span>
                <IconButton onClick={toggleAccountMenu} imgUrl="/images/account_icon_white.png" />
            </div>
        </div>
        <AccountMenu
            isVisible={menuVisible}
            isLoggedIn={loggedIn}
            onLogoutClicked={onLogoutClicked}
            onRegisterClicked={onRegisterClicked}
            onLoginClicked={onLoginClicked} />
    </div>

    function toggleAccountMenu() {
        setMenuVisible(visible => !visible);
    }

    function onLogoutClicked() {
        logout();
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