import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button, MenuItem
} from '@mui/material';

const AddressDialog = ({ open, onClose, selectedAddress, onUpdate, onChange }) => {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle sx={{
                backgroundColor:"#0F3460", color:"#ffffff" ,fontWeight:600 ,  textAlign:"center", fontSize:21}}>
                Adres Detayları</DialogTitle>
            <DialogContent>
                <TextField
                    label="Şehir"
                    name="city"
                    value={selectedAddress?.city || ''}
                    onChange={(e) => onChange({ ...selectedAddress, city: e.target.value })}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    label="İlçe"
                    name="district"
                    value={selectedAddress?.district || ''}
                    onChange={(e) => onChange({ ...selectedAddress, district: e.target.value })}
                    fullWidth
                    margin="normal"
                />

                <TextField
                    label="Mahalle"
                    name="neighborhood"
                    value={selectedAddress?.neighborhood || ''}
                    onChange={(e) => onChange({ ...selectedAddress, neighborhood: e.target.value })}
                    fullWidth
                    margin="normal"
                />

                <TextField
                    label="Genel Adres"
                    name="generalAddress"
                    value={selectedAddress?.generalAddress || ''}
                    onChange={(e) => onChange({ ...selectedAddress, generalAddress: e.target.value })}
                    fullWidth
                    margin="normal"
                />


                <TextField
                    label="Posta Kodu"
                    name="zipCode"
                    value={selectedAddress?.zipCode || ''}
                    onChange={(e) => onChange({ ...selectedAddress, zipCode: e.target.value })}
                    fullWidth
                    margin="normal"
                />
                <TextField
                    select
                    label="Adres Türü"
                    name="addressType"
                    value={selectedAddress?.addressType || ''}
                    onChange={(e) => onChange({ ...selectedAddress, addressType: e.target.value })}
                    fullWidth
                    margin="normal"
                >
                    <MenuItem value="HOME">Ev</MenuItem>
                    <MenuItem value="WORK">İş</MenuItem>
                    <MenuItem value="OTHER">Diğer</MenuItem>
                </TextField>

            </DialogContent>
            <DialogActions>
                <Button sx={{ backgroundColor: "#0F3460", color: "#ffffff" }} onClick={onClose}>İptal</Button>
                <Button sx={{ backgroundColor: "#0F3460", color: "#ffffff" }} onClick={onUpdate}>Güncelle</Button>
            </DialogActions>
        </Dialog>
    );
};

export default AddressDialog;