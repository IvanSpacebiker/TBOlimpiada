import React from 'react';
import {useNavigate} from 'react-router-dom';
import {Box, Button, Container, Typography} from '@mui/material';

const CancelBookingPage = () => {

    const navigate = useNavigate();

    const handleReturnToSearch = () => {
        navigate('/');
    };

    return (
        <Container maxWidth="sm">
            <Box mt={4} textAlign="center">
                <Typography variant="h4" gutterBottom>
                    Бронирование отменено
                </Typography>
                <Button
                    variant="contained"
                    color="info"
                    onClick={handleReturnToSearch}
                >
                    Вернуться к поиску билетов
                </Button>
            </Box>
        </Container>
    );
};

export default CancelBookingPage;