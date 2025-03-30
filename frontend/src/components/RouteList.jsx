import React from 'react';
import {Box} from '@mui/material';
import RouteCard from "./RouteCard.jsx";

const RouteList = ({ routes, onUpdateRoutes }) => {
    if (!Array.isArray(routes)) {
        return <div>Error: Tickets data is not an array.</div>;
    }

    return (
        <Box display="flex" flexDirection="column" gap={2}>
            {routes.map(route => (
                <RouteCard route={route} onUpdateRoutes={onUpdateRoutes}/>
            ))}
        </Box>
    );
};

export default RouteList;