import React from 'react';
import { TextField, Box } from '@mui/material';

const FilterInputs = () => {
    return (
        <Box display="flex" gap={2} mb={2}>
            <TextField label="Departure" variant="outlined" />
            <TextField label="Arrival" variant="outlined" />
            <TextField type="datetime-local" variant="outlined" />
        </Box>
    );
};

export default FilterInputs;