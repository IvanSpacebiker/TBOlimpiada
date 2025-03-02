import axios from 'axios';

const API_URL = 'api/bookings'; // Замените на ваш URL

export const getBookingById = (id) => {
    return axios.get(`${API_URL}/${id}`);
};

export const getAllBookings = (filters) => {
    return axios.get(API_URL, { params: filters });
};

export const createBooking = (booking) => {
    return axios.post(API_URL, booking);
};

export const updateBooking = (id, booking) => {
    return axios.put(`${API_URL}/${id}`, booking);
};

export const deleteBooking = (id) => {
    return axios.delete(`${API_URL}/${id}`);
};