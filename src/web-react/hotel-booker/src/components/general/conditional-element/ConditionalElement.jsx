export default function ConditionalElement({ children, condition }) {
    return condition
        ? <>{children}</>
        : null;
}