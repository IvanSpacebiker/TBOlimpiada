import React, { useState } from 'react';
import {TextField, Button, Box, Typography, Grid, FormControl, InputLabel, Select, MenuItem} from '@mui/material';

const SearchForm = ({ onSearch }) => {
    const [transportType, setTransportType] = useState("ANY");
    const [departure, setDeparture] = useState('');
    const [arrival, setArrival] = useState('');
    const [datetime, setDatetime] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        const departureDateTime = datetime
            ? new Date(datetime).toISOString()
            : null;
        onSearch({ transportType, departure, arrival, departureDateTime });
    };

    return (
        <Box mb={2}>
            <form onSubmit={handleSubmit}>
                <Grid container spacing={2} alignItems="center">
                    <Grid item xs={6} sm={3}>
                        <FormControl fullWidth>
                            <InputLabel id="transport-type-label">Транспорт</InputLabel>
                            <Select
                                labelId="transport-type-label"
                                id="transport-type-select"
                                value={transportType}
                                onChange={(e) => setTransportType(e.target.value)}
                                label="Транспорт"
                            >
                                <MenuItem value="ANY">Любой</MenuItem>
                                <MenuItem value="PLANE">Самолет</MenuItem>
                                <MenuItem value="BUS">Автобус</MenuItem>
                                <MenuItem value="TRAIN">Поезд</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={6} sm={3}>
                        <TextField
                            label="Откуда"
                            value={departure}
                            onChange={(e) => setDeparture(e.target.value)}
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={6} sm={3}>
                        <TextField
                            label="Куда"
                            value={arrival}
                            onChange={(e) => setArrival(e.target.value)}
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={6} sm={3}>
                        <TextField
                            type="datetime-local"
                            value={datetime}
                            onChange={(e) => setDatetime(e.target.value)}
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={12} sm={3}>
                        <Button variant="contained" color="primary" type="submit" fullWidth>
                            Найти билеты
                        </Button>
                    </Grid>
                </Grid>
            </form>
        </Box>
    );
};

export default SearchForm;