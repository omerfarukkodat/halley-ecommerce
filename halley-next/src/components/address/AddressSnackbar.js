import React from 'react';
import { Snackbar, Alert } from '@mui/material';

const AddressSnackbar = ({ open, onClose, message, severity }) => {
    return (
        <Snackbar
            open={open}
            autoHideDuration={6000}
            onClose={onClose}
        >
            <Alert style={{ backgroundColor: "#0F3460", color: "#ffffff" }} onClose={onClose} severity={severity}
                   sx={{ width: '100%' }}>
                {message}
            </Alert>
        </Snackbar>
    );
};

export default AddressSnackbar;