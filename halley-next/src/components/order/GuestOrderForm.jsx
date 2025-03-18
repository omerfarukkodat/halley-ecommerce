'use client';

import { useState } from "react";
import {
    Box,
    Button,
    TextField,
    MenuItem,
    Typography,
    FormControl,
    FormLabel,
    RadioGroup,
    FormControlLabel,
    Radio,
    Alert,
} from "@mui/material";
import { createOrderFromCart } from "@/services/orderService";
import { useCart } from "@/context/CartContext";

const GuestOrderForm = ({ onSuccess, cartSummary }) => {
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        city: "",
        district: "",
        neighborhood: "",
        generalAddress: "",
        zipCode: "",
        addressType: "HOME",
        paymentMethod: "WHATSAPP", // Varsayılan ödeme yöntemi
    });
    const [errors, setErrors] = useState({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    const { updateCartCount } = useCart();

    const transformFormDataToDto = (data) => ({
        nonMemberInfoDto: {
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            phoneNumber: data.phoneNumber,
            address: {
                city: data.city,
                district: data.district,
                neighborhood: data.neighborhood,
                generalAddress: data.generalAddress,
                zipCode: data.zipCode,
                addressType: data.addressType,
            },
        },
        paymentMethod: data.paymentMethod,
    });

    const validateForm = () => {
        const newErrors = {};
        if (!formData.firstName) newErrors.firstName = "First name is mandatory.";
        if (!formData.lastName) newErrors.lastName = "Last name is mandatory.";
        if (!formData.email || !/\S+@\S+\.\S+/.test(formData.email))
            newErrors.email = "Valid email is required.";
        if (!formData.phoneNumber) newErrors.phoneNumber = "Phone number is mandatory.";
        if (!formData.city) newErrors.city = "City is mandatory.";
        if (!formData.district) newErrors.district = "District is mandatory.";
        if (!formData.neighborhood) newErrors.neighborhood = "Neighborhood is mandatory.";
        if (!formData.generalAddress) newErrors.generalAddress = "General address is mandatory.";
        if (!formData.zipCode || !/^\d{5}$/.test(formData.zipCode))
            newErrors.zipCode = "Zip code must be 5 digits.";
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const dto = transformFormDataToDto(formData);
        if (validateForm()) {
            setIsSubmitting(true);
            try {
                const response = await createOrderFromCart(dto);
                console.log("Order successfully created:", response);
                if (formData.paymentMethod === "WHATSAPP" && response.paymentDetails) {
                    window.open(response.paymentDetails, "_blank");
                }
                if (onSuccess) onSuccess(response.id);
                alert("Order submitted successfully!");
                setFormData({
                    firstName: "",
                    lastName: "",
                    email: "",
                    phoneNumber: "",
                    city: "",
                    district: "",
                    neighborhood: "",
                    generalAddress: "",
                    zipCode: "",
                    addressType: "HOME",
                    paymentMethod: "WHATSAPP",
                });
            } catch (error) {
                console.error("Error creating order:", error);
                alert("Failed to submit order. Please try again later.");
            } finally {
                setIsSubmitting(false);
                updateCartCount(0);
            }
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 600, mx: "auto", mt: 4 }}>
            <Typography variant="h5" mb={2}>
                Guest Order Form
            </Typography>
            <TextField
                label="First Name"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                error={!!errors.firstName}
                helperText={errors.firstName}
                fullWidth
                margin="normal"
            />
            <TextField
                label="Last Name"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                error={!!errors.lastName}
                helperText={errors.lastName}
                fullWidth
                margin="normal"
            />
            <TextField
                label="Email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                error={!!errors.email}
                helperText={errors.email}
                fullWidth
                margin="normal"
            />
            <TextField
                label="Phone Number"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                error={!!errors.phoneNumber}
                helperText={errors.phoneNumber}
                fullWidth
                margin="normal"
            />
            <TextField
                label="City"
                name="city"
                value={formData.city}
                onChange={handleChange}
                error={!!errors.city}
                helperText={errors.city}
                fullWidth
                margin="normal"
            />
            <TextField
                label="District"
                name="district"
                value={formData.district}
                onChange={handleChange}
                error={!!errors.district}
                helperText={errors.district}
                fullWidth
                margin="normal"
            />
            <TextField
                label="Neighborhood"
                name="neighborhood"
                value={formData.neighborhood}
                onChange={handleChange}
                error={!!errors.neighborhood}
                helperText={errors.neighborhood}
                fullWidth
                margin="normal"
            />
            <TextField
                label="General Address"
                name="generalAddress"
                value={formData.generalAddress}
                onChange={handleChange}
                error={!!errors.generalAddress}
                helperText={errors.generalAddress}
                fullWidth
                margin="normal"
            />
            <TextField
                label="Zip Code"
                name="zipCode"
                value={formData.zipCode}
                onChange={handleChange}
                error={!!errors.zipCode}
                helperText={errors.zipCode}
                fullWidth
                margin="normal"
            />
            <TextField
                select
                label="Address Type"
                name="addressType"
                value={formData.addressType}
                onChange={handleChange}
                fullWidth
                margin="normal"
            >
                <MenuItem value="HOME">Home</MenuItem>
                <MenuItem value="WORK">Work</MenuItem>
                <MenuItem value="OTHER">Other</MenuItem>
            </TextField>

            <FormControl component="fieldset" sx={{ mt: 2 }}>
                <FormLabel component="legend">Payment Method</FormLabel>
                <RadioGroup name="paymentMethod" value={formData.paymentMethod} onChange={handleChange}>
                    <FormControlLabel value="WHATSAPP" control={<Radio />} label="WhatsApp Payment" />
                    <FormControlLabel value="IBAN_TRANSFER" control={<Radio />} label="IBAN Transfer" />
                </RadioGroup>
            </FormControl>

            {formData.paymentMethod === "IBAN_TRANSFER" && (
                <Alert severity="info" sx={{ mt: 2 }}>
                    Siparişiniz oluşturulduktan sonra, IBAN bilgisi ekranda gösterilecektir.
                </Alert>
            )}
            {formData.paymentMethod === "WHATSAPP" && (
                <Alert severity="info" sx={{ mt: 2 }}>
                    Siparişiniz oluşturulduktan sonra, ödeme için WhatsApp mesajı yeni pencerede açılacaktır.
                </Alert>
            )}

            <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ mt: 2, backgroundColor: "black" }}
                disabled={isSubmitting}
            >
                {isSubmitting ? "Submitting..." : "Submit Order"}
            </Button>
        </Box>
    );
};

export default GuestOrderForm;
