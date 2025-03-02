import React from 'react';
import TicketCard from './TicketCard';
import {Box, Grid} from '@mui/material';

const TicketList = ({ tickets }) => {
    if (!Array.isArray(tickets)) {
        return <div>Error: Tickets data is not an array.</div>;
    }

    return (
        <Box display="flex" flexDirection="column" gap={2}>
            {tickets.map(ticket => (
                <TicketCard ticket={ticket} />
            ))}
        </Box>
    );
};

export default TicketList;