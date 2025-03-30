import React, { useState } from 'react';
import { TextField, Button, Box, Grid, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { ru } from "date-fns/locale";

const SearchForm = ({ onSearch, departureOptions, arrivalOptions }) => {
    const [transportType, setTransportType] = useState("ANY");
    const [departure, setDeparture] = useState('');
    const [arrival, setArrival] = useState('');
    const [datetime, setDatetime] = useState(null);

    const handleSubmit = (event) => {
        event.preventDefault();
        const desiredDateTime = datetime ? new Date(datetime).toISOString() : null;
        onSearch({ transportType, departure, arrival, desiredDateTime });
    };

    return (
        <Box mb={2}>
            <form onSubmit={handleSubmit}>
                <Grid container spacing={2} alignItems="center">
                    <Grid item xs={6} sm={3}>
                        <FormControl fullWidth>
                            <InputLabel id="transport-type-label">Вид транспорта</InputLabel>
                            <Select
                                labelId="transport-type-label"
                                id="transport-type-select"
                                value={transportType}
                                onChange={(e) => setTransportType(e.target.value)}
                                label="Вид транспорта"
                            >
                                <MenuItem value="ANY">Любой</MenuItem>
                                <MenuItem value="PLANE">Самолет</MenuItem>
                                <MenuItem value="BUS">Автобус</MenuItem>
                                <MenuItem value="TRAIN">Поезд</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={6} sm={3}>
                        <FormControl fullWidth>
                            <InputLabel id="departure-label">Пункт отправления</InputLabel>
                            <Select
                                labelId="departure-label"
                                id="departure-select"
                                value={departure}
                                onChange={(e) => setDeparture(e.target.value)}
                                label="Пункт отправления"
                            >
                                {departureOptions.map((option, index) => (
                                    <MenuItem key={index} value={option}>
                                        {option === "" ? "ㅤ" : option}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={6} sm={3}>
                        <FormControl fullWidth>
                            <InputLabel id="arrival-label">Пункт прибытия</InputLabel>
                            <Select
                                labelId="arrival-label"
                                id="arrival-select"
                                value={arrival}
                                onChange={(e) => setArrival(e.target.value)}
                                label="Пункт прибытия"
                            >
                                {arrivalOptions.map((option, index) => (
                                    <MenuItem key={index} value={option}>
                                        {option === "" ? "ㅤ" : option}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Grid>
                    <Grid item xs={6} sm={3}>
                        <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={ru}>
                            <DateTimePicker
                                label="Выберите дату и время"
                                value={datetime}
                                onChange={(newValue) => setDatetime(newValue)}
                                renderInput={(params) => <TextField {...params} fullWidth />}
                            />
                        </LocalizationProvider>
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