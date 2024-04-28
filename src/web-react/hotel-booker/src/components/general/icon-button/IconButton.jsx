import "./IconButton.css";

export default function IconButton({ onClick, imgUrl, imgSize, type }) {
    if (imgSize !== undefined && imgSize !== null) {
        document.documentElement.style.setProperty('--icon-button-img-size', imgSize + 'px');
    }

    return <button onClick={onClick} type={type ?? 'button'} className="icon-button">
        <img className="icon-button-img" src={imgUrl ? imgUrl : ''} />
    </button>
}