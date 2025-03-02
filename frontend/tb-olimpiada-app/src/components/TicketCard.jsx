import React, {useState} from 'react';
import {Card, CardContent, Typography, Button, Box, Grid} from '@mui/material';
import BookingPopup from './BookingPopup';

const transportTypeMap = {
    PLANE: "Самолет",
    TRAIN: "Поезд",
    BUS: "Автобус"
};

const TicketCard = ({ticket}) => {
    const [isPopupOpen, setIsPopupOpen] = useState(false);

    const handleSelectTicket = () => {
        setIsPopupOpen(true);
    };

    const departureDateTimeParts = ticket.departureDateTime ? ticket.departureDateTime.split('T') : [];
    const departureTimeParts = departureDateTimeParts ? departureDateTimeParts[1].split('.')[0].split(":") : [];
    const arrivalDateTimeParts = ticket.arrivalDateTime ? ticket.arrivalDateTime.split('T') : [];
    const arrivalTimeParts = arrivalDateTimeParts ? departureDateTimeParts[1].split('.')[0].split(":") : [];


    const departureDate = departureDateTimeParts[0];
    const departureTime = departureTimeParts[0] + ":" + departureTimeParts[1]
    const arrivalDate = arrivalDateTimeParts[0];
    const arrivalTime = arrivalTimeParts[0] + ":" + arrivalTimeParts[1]

    const transportType = transportTypeMap[ticket.transportType] || "Неизвестный транспорт";

    return (
        <Card sx={{width: '100%'}}>
            <CardContent sx={{paddingBottom: '0 !important'}}>
                <Grid container spacing={2}>
                    <Grid item xs={3}>
                        <Box display="flex" alignItems="start" flexDirection="column">
                            <Typography variant="h4" gutterBottom>
                                {ticket.price} ₽
                            </Typography>
                            <Typography variant="h5" gutterBottom>
                                {transportType}
                            </Typography>
                            <Button variant="contained" color="primary" onClick={handleSelectTicket}>
                                Забронировать билет
                            </Button>
                        </Box>
                    </Grid>
                    <Grid item xs={4.5} sx={{borderLeft: "1px solid #ccc"}}>
                        <Typography variant="h6" gutterBottom>
                            <b>Отправление</b>
                        </Typography>
                        <Typography variant="h6" gutterBottom>
                            {ticket.departure}
                        </Typography>
                        <Typography variant="h6" gutterBottom>
                            {departureDate}
                        </Typography>
                        <Typography variant="h6" gutterBottom>
                            {departureTime}
                        </Typography>
                    </Grid>
                    <Grid item xs={4.5} sx={{borderLeft: "1px solid #ccc"}}>
                        <Typography variant="h6" gutterBottom>
                            <b>Прибытие</b>
                        </Typography>
                        <Typography variant="h6" gutterBottom>
                            {ticket.arrival}
                        </Typography>
                        <Typography variant="h6" gutterBottom>
                            {arrivalDate}
                        </Typography>
                        <Typography variant="h6" gutterBottom>
                            {arrivalTime}
                        </Typography>
                    </Grid>
                </Grid>
                <BookingPopup open={isPopupOpen} onClose={() => setIsPopupOpen(false)} ticket={ticket}/>
            </CardContent>
        </Card>
    );
};

export default TicketCard;