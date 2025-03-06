import React, { useEffect, useState } from 'react';
import SearchForm from './components/SearchForm';
import TicketList from './components/TicketList';
import { Container, Typography } from '@mui/material';
import { getTickets } from './services/TicketService';

const App = () => {
    const [tickets, setTickets] = useState([]);
    const [searchParams, setSearchParams] = useState({transportType: "ANY"});

    const handleSearch = async (params) => {
        setSearchParams(params);
        try {
            await getTickets(params).then(response => setTickets(response.data));
        } catch (error) {
            console.error('Error fetching tickets:', error);
        }
    };

    useEffect(() => {
        handleSearch(searchParams);
    }, [searchParams]);

    return (
        <Container>
            <Typography variant="h4" gutterBottom>
                Поиск билетов
            </Typography>
            <SearchForm onSearch={handleSearch} />
            <TicketList tickets={tickets} />
        </Container>
    );
};

export default App;