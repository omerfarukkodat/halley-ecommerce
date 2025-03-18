"use client";

import React, {useState, useEffect} from 'react';
import {
    Box,
    TextField,
    Button,
    Tabs,
    Tab,
    createTheme,
    ThemeProvider,
    FormGroup,
    FormControlLabel,
    Checkbox,
    FormHelperText,
    Typography,
    Link,
    InputAdornment, CircularProgress,
} from '@mui/material';
import {useRouter} from 'next/navigation';
import {useAuth} from "@/context/AuthContext";
import TurkishFlagIcon from "@/components/auth/TurkishFlagIcon";

const theme = createTheme({
    typography: {
        fontFamily: 'Quicksand, sans-serif',
    },
    components: {
        MuiTabs: {
            styleOverrides: {
                indicator: {
                    backgroundColor: 'black',
                },
            },
        },
        MuiTab: {
            styleOverrides: {
                root: {
                    fontFamily: 'Quicksand, sans-serif',
                    '&.Mui-selected': {
                        color: 'black',
                        fontWeight: 600,
                    },
                },
            },
        },
        MuiOutlinedInput: {
            styleOverrides: {
                root: {
                    fontFamily: 'Quicksand, sans-serif',
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#0F3460',
                        labelColor: 'gray',
                        fontWeight: 700,
                    },
                },
            },
        },
        MuiInputLabel: {
            styleOverrides: {

                root: {
                    fontFamily: 'Quicksand, sans-serif',
                    fontWeight: 700,

                    '&.Mui-focused': {
                        color: 'black',
                        fontWeight: 700,

                    },
                },
            },
        },
        MuiInputBase: {
            styleOverrides: {
                root: {
                    fontFamily: 'Quicksand, sans-serif',
                    fontWeight: 700,

                    '&.Mui-focused': {
                        color: 'black',
                        fontWeight: 700,

                    },
                },
            },
        },
        MuiButton: {
            styleOverrides: {
                root: {
                    fontFamily: 'Quicksand, sans-serif',
                    backgroundColor: 'black',
                    color: 'white',
                },
            },
        },
        MuiTypography: {
            styleOverrides: {
                root: {
                    fontFamily: 'Quicksand, sans-serif',
                },
            },
        },
        MuiFormControlLabel: {
            styleOverrides: {
                root: {
                    fontWeight: 200,
                },
                label: {
                    fontWeight: 200,
                },
            },
        },
    },
});


const CustomTextField = ({
                             name,
                             label,
                             value,
                             onChange,
                             error,
                             helperText,
                             type = 'text',
                             inputProps = {},
                             InputProps = {}
                         }) => (
    <TextField
        label={label}
        fullWidth
        margin="normal"
        name={name}
        value={value}
        onChange={onChange}
        error={!!error}
        helperText={error || helperText}
        type={type}
        inputProps={inputProps}
        InputProps={InputProps}
    />
);


const CustomButton = ({onClick, children, sx = {}}) => (
    <Button
        onClick={onClick}
        variant="contained"
        fullWidth
        sx={{mt: 2, mb: 5, backgroundColor: '#0F3460', ...sx}}
    >
        {children}
    </Button>
);


const CustomFormControlLabel = ({name, checked, onChange, label, error}) => (
    <>
        <FormControlLabel
            control={
                <Checkbox
                    name={name}
                    checked={checked}
                    onChange={onChange}
                    sx={{
                        width: 44,
                        height: 44,
                        color: '#0F3460',
                        transform: 'scale(1.3)',
                        cursor: 'pointer'
                    }}
                />
            }
            label={
                <Typography sx={{
                    fontSize: '15px',
                    whiteSpace: 'normal',
                    wordWrap: 'break-word',
                    fontWeight: 600,
                    lineHeight: 2.5,
                    ml: 1,
                    cursor: 'default'
                }}>
                    {label}
                </Typography>
            }
            sx={{
                display: 'flex',
                flexDirection: 'row',
                alignItems: 'flex-start',
                width: '100%'
            }}
        />
        {error && <FormHelperText error>{error}</FormHelperText>}
    </>
);


export default function AuthTabs({type}) {
    const router = useRouter();
    const {login, register} = useAuth();
    const [value, setValue] = useState(type === 'login' ? 0 : 1);
    const [loading , setLoading] = useState(false);
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        firstName: '',
        lastName: '',
        phone: '',
        emailConsent: false,
        smsConsent: false,
        termsAccepted: false,
    });
    const [errors, setErrors] = useState({
        email: '',
        password: '',
        phone: '',
        firstName: '',
        lastName: '',
        termsAccepted: '',

    });

    useEffect(() => {
        if (router.pathname && router.pathname.includes(type === 'login' ? 'giris' : 'uye-ol')) {
            setValue(type === 'login' ? 0 : 1);
        }
    }, [router.pathname, type]);

    const validateEmail = (email) => {
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return emailRegex.test(email);
    };

    const validatePassword = (password) => {
        const passwordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,15}$/;
        return passwordRegex.test(password);
    };

    const validatePhone = (phone) => {
        const cleanedPhone = phone.replace(/\D/g, '');
        return cleanedPhone.length === 10;
    };

    const validateName = (name) => {
        const nameRegex = /^[a-zA-ZğüşıöçĞÜŞİÖÇ\s]+$/;
        return nameRegex.test(name) && name.length >= 2 && name.length <= 50;
    };

    const sanitizeInput = (input) => {
        return input.replace(/['";\\]/g, '');
    };

    const handleSubmit = async () => {
        let valid = true;
        const newErrors = {};

        if (!validateEmail(formData.email)) {
            newErrors.email = 'Geçerli bir email adresi girin.';
            valid = false;
        }

        if (!validatePassword(formData.password)) {
            newErrors.password = 'Şifre 8-15 karakter arasında olmalı' +
                ' ve en az bir büyük harf, bir küçük harf ve bir rakam içermelidir.';
            valid = false;
        }

        if (value === 1 && !validatePhone(formData.phone)) {
            newErrors.phone = 'Telefon numarası 10 haneli olmalı.';
            valid = false;
        }

        if (value === 1 && !validateName(formData.firstName)) {
            newErrors.firstName = 'Ad sadece harf içermeli ve 2-50 karakter arasında olmalı.';
            valid = false;
        }

        if (value === 1 && !validateName(formData.lastName)) {
            newErrors.lastName = 'Soyad sadece harf içermeli ve 2-50 karakter arasında olmalı.';
            valid = false;
        }

        if (value === 1 && !formData.termsAccepted) {
            newErrors.termsAccepted = 'Üyelik koşullarını kabul etmelisiniz.';
            valid = false;
        }

        setErrors(newErrors);

        if (valid) {
            const sanitizedData = {
                ...formData,
                email: sanitizeInput(formData.email),
                password: sanitizeInput(formData.password),
                firstName: sanitizeInput(formData.firstName),
                lastName: sanitizeInput(formData.lastName),
                phone: `+90${formData.phone.replace(/\D/g, '')}`,
            };

            try {
                let result;
                if (type === 'login') {
                result = await login(sanitizedData);
            } else {
                result = await register(sanitizedData);
            }
                alert(result.error || "Bir hata oluştu. Lütfen tekrar deneyin.");
            }catch (error) {
                alert(error.response?.data?.message || "Bir hata oluştu. Lütfen tekrar deneyin.");

            }
        }
    };

    const handleChange = (event, newValue) => {
        setLoading(true);
        setValue(newValue);
        if (newValue === 0) {
            router.push('/giris');
        } else {
            router.push('/uye-ol');
        }
        setLoading(false);
    };

    const formatPhoneNumber = (phone) => {
        const cleaned = phone.replace(/\D/g, '');
        if (cleaned.length <= 3) return `(${cleaned}`;
        if (cleaned.length <= 6) return `(${cleaned.slice(0, 3)}) ${cleaned.slice(3)}`;
        return `(${cleaned.slice(0, 3)}) ${cleaned.slice(3, 6)}-${cleaned.slice(6, 10)}`;
    };

    const handleInputChange = (e) => {
        const {name, value} = e.target;

        if (name === 'phone') {
            const cleanedValue = value.replace(/\D/g, '');
            if (cleanedValue.length <= 10) {
                const formattedValue = formatPhoneNumber(cleanedValue);
                setFormData((prevData) => ({...prevData, [name]: formattedValue}));
            }
        } else {
            setFormData((prevData) => ({...prevData, [name]: value}));
        }
    };

    const handleKeyDown = (event) => {
        if (event.key === 'Enter') {
            handleSubmit();
        }
    };

    const handleCheckboxChange = (e) => {
        const {name, checked} = e.target;
        setFormData((prevData) => ({...prevData, [name]: checked}));
    };

    return (
        <ThemeProvider theme={theme}>
            <Box sx={{
                width: '100%',
                minHeight:"800px",
                typography: 'body1',
                background: "linear-gradient(165deg, #ffffff, #fff0f5)",
                backgroundSize: '200% 200%',
                animation: 'moveBackground 10s linear infinite',
                display: 'flex',
                flexDirection: 'column',
                mt:10


            }}>
                <Tabs value={value} onChange={handleChange} centered>
                    <Tab label="Giriş Yap"/>
                    <Tab label="Üye Ol"/>
                </Tabs>

                {loading ? (
                    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '300px' }}>
                        <CircularProgress />
                    </Box>
                ) : (

                <Box sx={{padding: '20px'}}>
                    {value === 0 && (
                        <Box sx={{width: '300px', margin: '0 auto', textAlign: 'center'}}>
                            <CustomTextField
                                name="email"
                                label="E-Posta Adresi"
                                value={formData.email}
                                onChange={handleInputChange}
                                error={errors.email}
                            />
                            <CustomTextField
                                name="password"
                                label="Şifre"
                                type="password"
                                value={formData.password}
                                onChange={handleInputChange}
                                error={errors.password}
                                onKeyDown={handleKeyDown}
                            />
                            <CustomButton onClick={handleSubmit}>Giriş Yap</CustomButton>
                            <Typography
                                onClick={() => router.push('/reset-password/request')}
                                sx={{
                                    display: 'inline-block',
                                    textAlign: 'right',
                                    cursor: 'pointer',
                                    color: '#0F3460',
                                    fontWeight: 600
                                }}
                            >
                                Şifremi Unuttum
                            </Typography>
                        </Box>
                    )}

                    {value === 1 && (
                        <Box sx={{maxWidth: '520px', margin: '0 auto'}}>
                            <CustomTextField
                                name="firstName"
                                label="Ad"
                                value={formData.firstName}
                                onChange={handleInputChange}
                                error={errors.firstName}
                            />
                            <CustomTextField
                                name="lastName"
                                label="Soyad"
                                value={formData.lastName}
                                onChange={handleInputChange}
                                error={errors.lastName}
                            />
                            <CustomTextField
                                name="email"
                                label="E-Posta Adresi"
                                value={formData.email}
                                onChange={handleInputChange}
                                error={errors.email}
                            />
                            <CustomTextField
                                name="phone"
                                label="Telefon Numarası"
                                value={formData.phone}
                                onChange={handleInputChange}
                                error={errors.phone}
                                inputProps={{
                                    inputMode: 'numeric',
                                    pattern: '[0-9]*',
                                    maxLength: 14,
                                }}
                                InputProps={{
                                    startAdornment: (
                                        <InputAdornment position="start">
                                            <Box sx={{display: "flex", alignItems: "center"}}>
                                                <TurkishFlagIcon sx={{color: "#0F3460", mr: 1}}/>
                                                <Typography
                                                    sx={{fontWeight: 600, mb: 0, color: "#0F3460"}}>+90</Typography>
                                            </Box>
                                        </InputAdornment>
                                    ),
                                }}
                            />
                            <CustomTextField
                                name="password"
                                label="Şifre"
                                type="password"
                                value={formData.password}
                                onChange={handleInputChange}
                                error={errors.password}
                                onKeyDown={handleKeyDown}
                            />

                            <FormGroup sx={{mb: 3, mt: 3,
                                '& .MuiFormControlLabel-label': {
                                    fontWeight: 700,
                                }
                            }}>
                                <CustomFormControlLabel

                                    name="emailConsent"
                                    checked={formData.emailConsent}
                                    onChange={handleCheckboxChange}
                                    label="Kampanya, duyuru, bilgilendirmelerden e-posta ile haberdar olmak istiyorum."
                                />
                                <CustomFormControlLabel
                                    name="smsConsent"
                                    checked={formData.smsConsent}
                                    onChange={handleCheckboxChange}
                                    label="Kampanya, duyuru, bilgilendirmelerden SMS ile haberdar olmak istiyorum."
                                />
                                <CustomFormControlLabel
                                    name="termsAccepted"
                                    checked={formData.termsAccepted}
                                    onChange={handleCheckboxChange}
                                    label={
                                        <>
                                            <Link href="/terms"
                                                  sx={{color: '#FF5733', textDecoration: 'none', fontWeight: 600}}>
                                                Üyelik koşullarını
                                            </Link>
                                            {' ve '}
                                            <Link href="/privacy-policy"
                                                  sx={{color: '#FF5733', textDecoration: 'none', fontWeight: 600}}>
                                                kişisel verilerimin korunmasını
                                            </Link>
                                            {' kabul ediyorum.'}
                                        </>
                                    }
                                    error={errors.termsAccepted}
                                />
                            </FormGroup>

                            <CustomButton onClick={handleSubmit}>Üye Ol</CustomButton>
                        </Box>
                    )}
                </Box>
                )}
            </Box>
        </ThemeProvider>
    );
}