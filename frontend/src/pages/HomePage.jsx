import React, { useEffect, useState } from 'react';
import SearchForm from '../components/SearchForm';
import { Container, Pagination, Stack, Typography, CircularProgress } from '@mui/material';
import { getRoutes, getDepartures, getArrivals } from "../services/RouteService.js";
import RouteList from "../components/RouteList.jsx";

const HomePage = () => {
    const [routes, setRoutes] = useState([]);
    const [searchParams, setSearchParams] = useState({ transportType: "ANY" });
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [departureOptions, setDepartureOptions] = useState([""]);
    const [arrivalOptions, setArrivalOptions] = useState([""]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        const loadPage = async () => {
            setIsLoading(true);
            try {
                const [
                    uniqueDepartures,
                    uniqueArrivals,
                    routes
                ] = await Promise.all([
                    getDepartures(),
                    getArrivals(),
                    getRoutes({ ...searchParams, page: 0, size: 10 })
                ]);
                setDepartureOptions(["", ...uniqueDepartures.data]);
                setArrivalOptions(["", ...uniqueArrivals.data]);
                setRoutes(routes.data.content);
                setTotalPages(routes.data.page.totalPages);
            } catch (error) {
                console.error('Error loading:', error);
            } finally {
                setIsLoading(false);
            }
        };
        loadPage();
    }, []);

    const handleSearch = async (params) => {
        setIsLoading(true);
        setSearchParams(params);
        setCurrentPage(1);

        try {
            const response = await getRoutes({ ...params, page: 0, size: 10 });
            setRoutes(response.data.content);
            setTotalPages(response.data.page.totalPages);
        } catch (error) {
            console.error('Error fetching routes:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const handlePageChange = async (event, newPage) => {
        setIsLoading(true);
        setCurrentPage(newPage);
        try {
            const response = await getRoutes({...searchParams, page: newPage - 1, size: 10});
            setRoutes(response.data.content);
        } catch (error) {
            console.error('Error fetching tickets:', error);
        } finally {
            setIsLoading(false);
        }
    };

    const updateRoutes = async () => {
        setIsLoading(true);
        try {
            const response = await getRoutes({ ...searchParams, page: currentPage - 1, size: 10 });
            setRoutes(response.data.content);
            setTotalPages(response.data.page.totalPages);
        } catch (error) {
            console.error('Error updating routes:', error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <Container>
            <Typography variant="h4" gutterBottom>
                Поиск билетов
            </Typography>
            <SearchForm
                onSearch={handleSearch}
                departureOptions={departureOptions}
                arrivalOptions={arrivalOptions}
            />
            {isLoading ? (
                <Stack alignItems="center" justifyContent="center" mt={4}>
                    <CircularProgress color="primary" size={60} />
                </Stack>
            ) : routes.length === 0 ? (
                <Typography variant="h5" align="center" color="textSecondary" mt={4}>
                    Маршруты по вашему запросу не найдены
                </Typography>
            ) : (
                <>
                    <RouteList routes={routes} onUpdateRoutes={updateRoutes} />
                    <Stack spacing={2} alignItems="center" mt={4}>
                        <Pagination
                            count={totalPages}
                            page={currentPage}
                            onChange={handlePageChange}
                            color="primary"
                            size="large"
                        />
                    </Stack>
                </>
            )}
        </Container>
    );
};

export default HomePage;