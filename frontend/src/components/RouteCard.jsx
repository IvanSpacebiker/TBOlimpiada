import React, {useState} from 'react';
import {Card, CardContent, Typography, Button, Box, Grid} from '@mui/material';
import BookingPopup from './BookingPopup';

const transportTypeMap = {
    PLANE: "Самолет",
    TRAIN: "Поезд",
    BUS: "Автобус"
};

const RouteCard = ({route}) => {
    const [popup, setPopup] = useState(null);

    const handleSelectTicket = (ticket) => {
        setPopup([ticket]);
    };

    const handleSelectAllTickets = () => {
        setPopup(route.tickets);
    };

    const closePopup = () => {
        setPopup(null);
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
        <Card sx={{width: '100%'}}>
            <CardContent sx={{paddingBottom: '0 !important'}}>
                <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                    <Typography variant="h5">
                        {route.departure} → {route.arrival}
                    </Typography>
                    <Typography variant="h5">
                        {formatDateTime(route.departureDateTime).date + " " + formatDateTime(route.departureDateTime).time} → {formatDateTime(route.arrivalDateTime).date + " " + formatDateTime(route.arrivalDateTime).time}
                    </Typography>
                    <Typography variant="h5">
                        {route.price} ₽
                    </Typography>
                </Box>

                <Box mb={2}>
                    <Button variant="contained" color="secondary" onClick={handleSelectAllTickets}>
                        Забронировать весь маршрут
                    </Button>
                </Box>

                {/* Список билетов */}
                <Grid container spacing={2}>
                    {route.tickets.map((ticket, index) => {
                        const {date: departureDate, time: departureTime} = formatDateTime(ticket.departureDateTime);
                        const {date: arrivalDate, time: arrivalTime} = formatDateTime(ticket.arrivalDateTime);
                        const transportType = transportTypeMap[ticket.transportType] || "Неизвестный транспорт";

                        return (
                            <Grid item xs={12} key={index}>
                                <Card variant="outlined" sx={{padding: 2}}>
                                    <Grid container spacing={2}>
                                        <Grid item xs={3}>
                                            <Box display="flex" alignItems="start" flexDirection="column">
                                                <Typography variant="h6" gutterBottom>
                                                    {ticket.price} ₽
                                                </Typography>
                                                <Typography variant="subtitle1" gutterBottom>
                                                    {transportType}
                                                </Typography>
                                                <Button
                                                    variant="contained"
                                                    color="info"
                                                    onClick={() => handleSelectTicket(ticket)}
                                                >
                                                    Забронировать билет
                                                </Button>
                                            </Box>
                                        </Grid>
                                        <Grid item xs={4.5} sx={{borderLeft: "1px solid #ccc"}}>
                                            <Typography variant="subtitle1" gutterBottom>
                                                <b>Отправление</b>
                                            </Typography>
                                            <Typography variant="body1" gutterBottom>
                                                {ticket.departure}
                                            </Typography>
                                            <Typography variant="body1" gutterBottom>
                                                {departureDate}
                                            </Typography>
                                            <Typography variant="body1" gutterBottom>
                                                {departureTime}
                                            </Typography>
                                        </Grid>
                                        <Grid item xs={4.5} sx={{borderLeft: "1px solid #ccc"}}>
                                            <Typography variant="subtitle1" gutterBottom>
                                                <b>Прибытие</b>
                                            </Typography>
                                            <Typography variant="body1" gutterBottom>
                                                {ticket.arrival}
                                            </Typography>
                                            <Typography variant="body1" gutterBottom>
                                                {arrivalDate}
                                            </Typography>
                                            <Typography variant="body1" gutterBottom>
                                                {arrivalTime}
                                            </Typography>
                                        </Grid>
                                    </Grid>
                                </Card>
                            </Grid>
                        );
                    })}
                </Grid>

                <BookingPopup
                    open={!!popup}
                    onClose={closePopup}
                    tickets={popup === null ? [] : popup}
                />
            </CardContent>
        </Card>
    );
};

export default RouteCard;