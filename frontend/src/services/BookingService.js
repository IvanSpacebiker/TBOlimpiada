import axios from 'axios';

const API_URL = 'api/bookings';

export const createBooking = (booking) => {
    return axios.post(API_URL, booking);
};