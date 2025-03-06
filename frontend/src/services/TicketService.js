import axios from 'axios';

const API_URL = 'api/tickets';

export const getTicketById = (id) => {
    return axios.get(`${API_URL}/${id}`);
};

export const getTickets = (filters) => {
    return axios.get(API_URL, { params: filters });
};

export const createTicket = (ticket) => {
    return axios.post(API_URL, ticket);
};

export const updateTicket = (id, ticket) => {
    return axios.put(`${API_URL}/${id}`, ticket);
};

export const deleteTicket = (id) => {
    return axios.delete(`${API_URL}/${id}`);
};