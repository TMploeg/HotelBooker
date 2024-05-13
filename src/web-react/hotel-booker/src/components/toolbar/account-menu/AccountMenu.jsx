import "../../../stylesheets/icons.css";
import DroppedButton from "../../general/dropped-button";
import "./AccountMenu.css";

export default function AccountMenu({ isVisible, isLoggedIn, onLoginClicked, onRegisterClicked, onLogoutClicked }) {
    return isLoggedIn
        ? <LoggedInAccountMenu isVisible={isVisible} onLogoutClicked={onLogoutClicked} />
        : <NotLoggedInAccountMenu isVisible={isVisible} onRegisterClicked={onRegisterClicked} onLoginClicked={onLoginClicked} />
}

function LoggedInAccountMenu({ isVisible, onLogoutClicked }) {
    return <div className="menu-container">
        <div className={`menu-content${isVisible ? ' visible' : ''}`}>
            <DroppedButton onClick={onLogoutClicked}>
                <img className="medium-icon margin-left" src="/images/logout_icon_white.png" />
                <span>Logout</span>
            </DroppedButton>
        </div>
    </div >
}

function NotLoggedInAccountMenu({ isVisible, onRegisterClicked, onLoginClicked }) {
    return <div className="menu-container">
        <div className={`menu-content${isVisible ? ' visible' : ''}`}>
            <DroppedButton onClick={onRegisterClicked}>
                <img className="medium-icon margin-left" src="/images/register_icon_white.png" />
                <span>Register</span>
            </DroppedButton>
            <DroppedButton onClick={onLoginClicked}>
                <img className="medium-icon margin-left" src="/images/login_icon_white.png" />
                <span>Login</span>
            </DroppedButton>
        </div>
    </div>
}