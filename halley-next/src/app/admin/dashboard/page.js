import * as React from 'react';
import { Typography } from '@mui/material';

export default function AdminDashboardPage() {
    return (
        <div>
            <Typography variant="h4" gutterBottom>
                Dashboard
            </Typography>
            <Typography variant="body1">
                Welcome to the Admin Dashboard!
            </Typography>
        </div>
    );
}