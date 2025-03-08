import axios from 'axios';

const API_URL = '/api/routes';

export const getRoutes = (filters) => {
    return axios.get(API_URL, { params: filters });
};

export const getArrivals = () => {
    return axios.get(API_URL + "/arrivals");
};

export const getDepartures = () => {
    return axios.get(API_URL + "/departures");
};
