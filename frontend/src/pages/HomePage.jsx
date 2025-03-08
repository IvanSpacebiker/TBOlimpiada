import React, {useEffect, useState} from 'react';
import SearchForm from '../components/SearchForm';
import {Container, Pagination, Stack, Typography} from '@mui/material';
import {getRoutes} from "../services/RouteService.js";
import RouteList from "../components/RouteList.jsx";

const HomePage = () => {
    const [routes, setRoutes] = useState([]);
    const [searchParams, setSearchParams] = useState({transportType: "ANY"});
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(1);

    const handleSearch = async (params) => {
        setSearchParams(params);
        setCurrentPage(1);
        try {
            const response = await getRoutes({...params, page: 0, size: 10});
            setRoutes(response.data.content)
            setTotalPages(response.data.page.totalPages);
        } catch (error) {
            console.error('Error fetching routes:', error);
        }
    };

    const handlePageChange = async (event, newPage) => {
        setCurrentPage(newPage);
        try {
            const response = await getRoutes({...searchParams, page: newPage - 1, size: 10});
            setRoutes(response.data.content);
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
            {routes.length === 0 ? (
                <Typography variant="h5" align="center" color="textSecondary" mt={4}>
                    Маршруты по вашему запросу не найдены
                </Typography>
            ) : (
                <>
                    <RouteList routes={routes} />
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