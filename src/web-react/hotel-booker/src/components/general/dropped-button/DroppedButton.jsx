import "./DroppedButton.css";

export default function DroppedButton({ children, onClick }) {
    return <button className="dropped-button" onClick={onClick}>{children}</button>
}