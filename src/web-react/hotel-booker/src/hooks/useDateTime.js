export default function useDateTime() {
    const toDate = value => {
        return new Date(`${value.date}T${value.time}`);
    }

    const compile = date => {
        const years = date.getFullYear();
        const month = toMin2LengthString(date.getMonth() + 1);
        const days = toMin2LengthString(date.getDate());
        const hours = toMin2LengthString(date.getHours());
        const minutes = toMin2LengthString(date.getMinutes());
        return {
            date: `${years}-${month}-${days}`,
            time: `${hours}:${minutes}`
        }
    }

    function toMin2LengthString(value) {
        return (value <= 9 ? '0' : '') + value;
    }

    return {
        toDate,
        compile
    }
}