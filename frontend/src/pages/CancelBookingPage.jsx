import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Typography, Button, Box, CircularProgress, Alert } from '@mui/material';
import {cancelBooking, getBooking} from '../services/BookingService.js';

const transportTypeMap = {
    PLANE: "Самолет",
    TRAIN: "Поезд",
    BUS: "Автобус"
};

const CancelBookingPage = () => {
    const { id } = useParams(); // Получаем токен из URL
    const navigate = useNavigate();
    const [bookingDetails, setBookingDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBookingDetails = async () => {
            try {
                const response = await getBooking(id);
                console.log(response.data)
                setBookingDetails(response.data);
            } catch (err) {
                setError('Недействительная ссылка или бронирование уже отменено.');
            } finally {
                setLoading(false);
            }
        };

        fetchBookingDetails();
    }, [id]);

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

    const handleConfirmCancellation = async () => {
        try {
            await cancelBooking(id);
            navigate('/bookings/canceled');
        } catch (err) {
            setError('Не удалось отменить бронирование. Попробуйте позже.');
        }
    };

    if (loading) {
        return (
            <Container maxWidth="sm">
                <Box display="flex" justifyContent="center" alignItems="center" minHeight="300px">
                    <CircularProgress color="primary" />
                </Box>
            </Container>
        );
    }

    if (error) {
        return (
            <Container maxWidth="sm">
                <Alert severity="error">{error}</Alert>
            </Container>
        );
    }

    return (
        <Container maxWidth="sm">
            <Box mt={4} textAlign="center">
                <Typography variant="h4" gutterBottom color="primary">
                    Подтверждение отмены бронирования
                </Typography>
                <Typography variant="body1" gutterBottom>
                    Вы уверены, что хотите отменить следующее бронирование?
                </Typography>
                <Box mt={2} >
                    <Typography variant="subtitle1">
                        Маршрут: {bookingDetails.ticket.departure} → {bookingDetails.ticket.arrival}
                    </Typography>
                    <Typography variant="subtitle1">
                        Дата отправления: {formatDateTime(bookingDetails.ticket.departureDateTime).date}
                    </Typography>
                    <Typography variant="subtitle1">
                        Время отправления: {formatDateTime(bookingDetails.ticket.departureDateTime).time}
                    </Typography>
                    <Typography variant="subtitle1">
                        Дата прибытия: {formatDateTime(bookingDetails.ticket.arrivalDateTime).date}
                    </Typography>
                    <Typography variant="subtitle1">
                        Время прибытия: {formatDateTime(bookingDetails.ticket.arrivalDateTime).time}
                    </Typography>
                    <Typography variant="subtitle1">
                        Вид транспорта: {transportTypeMap[bookingDetails.ticket.transportType]}
                    </Typography>
                    <Typography variant="subtitle1">
                        Цена: {bookingDetails.ticket.price} руб.
                    </Typography>
                </Box>
                <Box mt={4}>
                    <Button
                        variant="contained"
                        color="error"
                        onClick={handleConfirmCancellation}
                    >
                        Да, отменить бронирование
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default CancelBookingPage;