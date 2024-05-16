export default function useCSSProperties() {
    const computedStyles = window.getComputedStyle(document.body);
    const getProperty = name => {
        return computedStyles.getPropertyValue(name);
    }

    return {
        getProperty
    }
}