import useApi from "../hooks/useApi";

export default function useHotels() {
    const { get } = useApi();

    const getAll = () => get('hotels');
    const getById = (id) => get(`hotels/${id}`);
    const search = (search, city) => get('hotels', { search, city });

    return {
        getAll,
        getById,
        search
    }
}