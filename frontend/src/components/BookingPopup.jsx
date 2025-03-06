import React, { useState } from 'react';
import {Dialog, DialogTitle, DialogContent, TextField, Button, Box, Typography, Grid} from '@mui/material';
import {createBooking} from "../services/BookingService.js";

const BookingPopup = ({ open, onClose, ticket }) => {
    const [phone, setPhone] = useState('');
    const [email, setEmail] = useState('');
    const [errors, setErrors] = useState({ phone: '', email: '' });

    const validate = () => {
        let isValid = true;
        let newErrors = { phone: '', email: '' };

        if (!phone) {
            newErrors.phone = 'Телефон обязателен';
            isValid = false;
        } else if (!/^\+?\d{10,15}$/.test(phone)) {
            newErrors.phone = 'Некорректный формат телефона';
            isValid = false;
        }

        if (!email) {
            newErrors.email = 'Email обязателен';
            isValid = false;
        } else if (!/\S+@\S+\.\S+/.test(email)) {
            newErrors.email = 'Некорректный формат email';
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    };

    const handleConfirmBooking = async () => {
        if (validate()) {
            await createBooking({ ticket: ticket, phone, email })
            onClose()
            window.location.reload()
        }
    };

    const departureDateTimeParts = ticket.departureDateTime ? ticket.departureDateTime.split('T') : [];
    const arrivalDateTimeParts = ticket.arrivalDateTime ? ticket.arrivalDateTime.split('T') : [];

    const departureDate = departureDateTimeParts[0];
    const departureTime = departureDateTimeParts[1] ? departureDateTimeParts[1].split('.')[0] : '';
    const arrivalDate = arrivalDateTimeParts[0];
    const arrivalTime = arrivalDateTimeParts[1] ? arrivalDateTimeParts[1].split('.')[0] : '';

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Бронирование билета</DialogTitle>
            <DialogContent dividers sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Box sx={{ mb: 2 }}>
                    <Grid container spacing={2}>
                        <Grid item xs={6} sx={{ borderRight: '1px solid #ccc', pr: 2 }}>
                            <Typography variant="subtitle1" gutterBottom>
                                <b>Отправление</b>
                            </Typography>
                            <Typography variant="subtitle1" gutterBottom>
                                {ticket.departure}
                            </Typography>
                            <Typography variant="subtitle1" gutterBottom>
                                {departureDate}
                            </Typography>
                            <Typography variant="subtitle1" gutterBottom>
                                {departureTime}
                            </Typography>
                        </Grid>
                        <Grid item xs={6} sx={{ pl: 2 }}>
                            <Typography variant="subtitle1" gutterBottom>
                                <b>Прибытие</b>
                            </Typography>
                            <Typography variant="subtitle1" gutterBottom>
                                {ticket.arrival}
                            </Typography>
                            <Typography variant="subtitle1" gutterBottom>
                                {arrivalDate}
                            </Typography>
                            <Typography variant="subtitle1" gutterBottom>
                                {arrivalTime}
                            </Typography>
                        </Grid>
                    </Grid>
                </Box>
                <TextField
                    label="Телефон"
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    error={!!errors.phone}
                    helperText={errors.phone}
                    inputProps={{ pattern: '[0-9]{10}', placeholder: '+71234567890' }}
                    fullWidth
                    sx={{ mb: 2 }}
                />
                <TextField
                    label="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    error={!!errors.email}
                    helperText={errors.email}
                    inputProps={{ type: 'email', placeholder: 'example@example.com' }}
                    fullWidth
                    sx={{ mb: 2 }}
                />
            </DialogContent>
            <Box display="flex" justifyContent="center" mt={2}>
                <Button onClick={handleConfirmBooking} color="primary">
                    Подтвердить бронирование
                </Button>
                <Button onClick={onClose}>Отмена</Button>
            </Box>
        </Dialog>
    );
};

export default BookingPopup;