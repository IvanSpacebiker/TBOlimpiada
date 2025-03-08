import axios from 'axios';

const API_URL = '/api/bookings';

export const createBooking = (booking) => {
    return axios.post(API_URL, booking);
};

export const getBooking = (id) => {
    return axios.get(API_URL + `/${id}`);
};

export const cancelBooking = (id) => {
    return axios.delete(API_URL + `/${id}`);
};