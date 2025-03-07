import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, TextField, Button, Box, Typography, Grid } from '@mui/material';
import { createBooking } from "../services/BookingService.js";

const BookingPopup = ({ open, onClose, tickets }) => {
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
            tickets.map(async (ticket) => {
                await createBooking({ticket, phone, email});
            })
            onClose();
            window.location.reload();
        }
    };

    const formatDateTime = (dateTime) => {
        if (!dateTime) return '';
        const date = new Date(dateTime)
        const formattedDate = new Intl.DateTimeFormat('ru-RU').format(date);
        const formattedTime = new Intl.DateTimeFormat('ru-RU', {
            hour: '2-digit',
            minute: '2-digit',
            hour12: false,
        }).format(date);

        return {date: formattedDate, time: formattedTime};
    };

    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>{'Бронирование маршрута'}</DialogTitle>
            <DialogContent dividers sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                {tickets.map((t, index) => {
                    const { date: departureDate, time: departureTime } = formatDateTime(t.departureDateTime);
                    const { date: arrivalDate, time: arrivalTime } = formatDateTime(t.arrivalDateTime);

                    return (
                        <Box key={index} sx={{ mb: 2 }}>
                            <Typography variant="h6" gutterBottom>
                                {tickets.length > 1 ? `Билет ${index + 1}` : `Билет`}
                            </Typography>
                            <Grid container spacing={2}>
                                <Grid item xs={6} sx={{ borderRight: '1px solid #ccc', pr: 2 }}>
                                    <Typography variant="subtitle1" gutterBottom>
                                        <b>Отправление</b>
                                    </Typography>
                                    <Typography variant="subtitle1" gutterBottom>
                                        {t.departure}
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
                                        {t.arrival}
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
                    );
                })}
                {/* Поля для ввода данных пользователя */}
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
                <Button onClick={handleConfirmBooking} color="success">
                    Подтвердить бронирование
                </Button>
                <Button onClick={onClose} color="error">Отмена</Button>
            </Box>
        </Dialog>
    );
};

export default BookingPopup;