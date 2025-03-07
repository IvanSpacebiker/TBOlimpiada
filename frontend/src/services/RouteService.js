import axios from 'axios';

const API_URL = 'api/routes';

export const getRoutes = (filters) => {
    return axios.get(API_URL, { params: filters });
};

export const getArrivals = (filters) => {
    return axios.get(API_URL + "/arrivals");
};

export const getDepartures = (filters) => {
    return axios.get(API_URL + "/departures");
};
