import "../../../stylesheets/icons.css";
import DroppedButton from "../../general/dropped-button";
import "./AccountMenu.css";

export default function AccountMenu({ isVisible }) {
    return <div className="menu-container">
        <div className={`menu-content${isVisible ? ' visible' : ''}`}>
            <DroppedButton>
                <img className="medium-icon margin-left" src="/images/register_icon.png" />
                <span>Register</span>
            </DroppedButton>
            <DroppedButton className="test">
                <img className="medium-icon margin-left" src="/images/login_icon.png" />
                <span>Login</span>
            </DroppedButton>
        </div>
    </div >
}