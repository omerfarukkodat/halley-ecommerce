import React from 'react';
import {
    Card,
    CardContent,
    Typography,
    Box,
    IconButton,
    Divider,
    Tooltip,
    useTheme
} from '@mui/material';
import EditOutlined from "@mui/icons-material/EditOutlined";
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import { styled } from '@mui/system';

const StyledCard = styled(Card)(({ theme }) => ({
    height: '100%',
    display: 'flex',
    flexDirection: 'column',
    backgroundColor: theme.palette.background.paper,
    borderRadius: '12px',
    boxShadow: '0 4px 20px rgba(0, 0, 0, 0.1)',
    transition: 'transform 0.2s, box-shadow 0.2s',
    '&:hover': {
        transform: 'translateY(-4px)',
        boxShadow: '0 8px 24px rgba(0, 0, 0, 0.15)',
    },
}));

const HeaderBox = styled(Box)(({ theme }) => ({
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: theme.palette.primary.main,
    color: theme.palette.primary.contrastText,
    padding: theme.spacing(2),
    borderTopLeftRadius: '12px',
    borderTopRightRadius: '12px',
}));

const AddressIcon = styled(LocationOnIcon)(({ theme }) => ({
    marginRight: theme.spacing(1),
    color: theme.palette.secondary.main,
}));

const AddressCard = ({ address, addressTypeMap, onEdit, onDelete }) => {
    const theme = useTheme();

    return (
        <StyledCard>
            <HeaderBox sx={{ backgroundColor:"#0F3460" }}>
                <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1  }}>
                    <AddressIcon />
                    <Typography variant="h6" sx={{ fontWeight: 600 }}>
                        {addressTypeMap[address.addressType] || "Bilinmiyor"}
                    </Typography>
                </Box>
                <Box>
                    <Tooltip title="Adresi Düzenle" arrow>
                        <IconButton sx={{ color: theme.palette.primary.contrastText }} onClick={() => onEdit(address)}>
                            <EditOutlined />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Adresi Sil" arrow>
                        <IconButton sx={{ color: theme.palette.primary.contrastText }}
                                    onClick={() => onDelete(address.id)}>
                            <DeleteForeverIcon />
                        </IconButton>
                    </Tooltip>
                </Box>
            </HeaderBox>

            <CardContent sx={{ flexGrow: 1, padding: theme.spacing(3) }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Typography variant="body1" sx={{ fontWeight: 500, color: theme.palette.text.primary }}>
                        {address.generalAddress}
                    </Typography>
                </Box>
                <Divider sx={{ my: 2, backgroundColor: theme.palette.divider }} />

                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Typography variant="body1" sx={{ color: theme.palette.text.secondary }}>
                        {address.district}, {address.city}
                    </Typography>
                </Box>
                <Divider sx={{ my: 2, backgroundColor: theme.palette.divider }} />

                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Typography variant="body1" sx={{ color: theme.palette.text.secondary }}>
                        Posta Kodu: {address.zipCode}
                    </Typography>
                </Box>
            </CardContent>
        </StyledCard>
    );
};

export default AddressCard;