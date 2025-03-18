import React, { useState } from "react";
import {
    Box,
    Button,
    FormControl,
    Grid,
    InputLabel,
    MenuItem,
    Select,
    Slider,
    Typography,
} from "@mui/material";
import CategoryFilter from "@/components/product/Filter/CategoryFilter";
import {wallpaperSizeOptions, wallpaperTypeOptions} from "@/components/product/Filter/wallpaperOptions";
import Divider from "@mui/material/Divider";

const FilterContent = ({
                           filters,
                           setFilters,
                           applyFilters,
                           brands,
                           loadingBrands,
                           visibleFilters,
                           categories,
                           loadingCategories,
                       }) => {
    const [priceRange, setPriceRange] = useState([filters.minPrice || 0, filters.maxPrice || 2500]);

    const handlePriceChange = (event, newValue) => {
        setPriceRange(newValue);
        setFilters({ ...filters, minPrice: newValue[0], maxPrice: newValue[1] });
    };

    const renderSelectFilter = (key, label, options) => {
        if (!visibleFilters[key]) return null;
        return (
            <Grid item xs={12} key={key}>
                <FormControl fullWidth variant="outlined" size="small">
                    <InputLabel id={`${key}-label`}>{label}</InputLabel>
                    <Select
                        labelId={`${key}-label`}
                        value={filters[key] || ""}
                        label={label}
                        onChange={(e) => setFilters({ ...filters, [key]: e.target.value })}
                    >
                        <MenuItem value="">
                            <em>Tümü</em>
                        </MenuItem>
                        {options.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Grid>
        );
    };

    return (
        <Box p={3}>
            <Typography variant="h5" mb={2} fontWeight={600} sx={{textAlign:"center", color: "#000000" }}>
                Filtreler
            </Typography>
            <Divider sx={{ mt: 1 , borderColor: "#ddd", mb:2 , borderWidth: 1  }} />
            <Grid container spacing={2}>
                {visibleFilters.price && (
                    <Grid item xs={12}>
                        <Typography variant="body1" sx={{mt:1, color: "#000000" }}>
                            Fiyat Aralığı
                        </Typography>
                        <Slider
                            value={priceRange}
                            onChange={handlePriceChange}
                            valueLabelDisplay="auto"
                            valueLabelFormat={(value) => `₺${value}`}
                            min={0}
                            max={2500}
                            step={500}
                            sx={{
                                "& .MuiSlider-rail": { backgroundColor: "#ffffff" },
                                "& .MuiSlider-track": { backgroundColor: "#5899dd" },
                                "& .MuiSlider-thumb": { backgroundColor: "#5899dd" },
                            }}
                        />
                        <Box display="flex" justifyContent="space-between" mt={1}>
                            <Typography variant="body1">{priceRange[0]} TL</Typography>
                            <Typography variant="body1">{priceRange[1]} TL</Typography>
                        </Box>
                    </Grid>
                )}

                {visibleFilters.brand &&
                    renderSelectFilter("brand", "Marka", brands.map((b) =>
                        ({ value: b.id, label: b.name })))}

                {visibleFilters.wallpaperType &&
                    renderSelectFilter("wallpaperType", "Duvar Kağıdı Tipi", wallpaperTypeOptions)}

                {visibleFilters.wallpaperSize &&
                    renderSelectFilter("wallpaperSize", "Duvar Kağıdı Boyutu", wallpaperSizeOptions)}

                {visibleFilters.category && (
                    <Grid item xs={12}>
                        <CategoryFilter
                            selectedCategory={filters.categoryId}
                            onSelect={(categoryId) => setFilters({ ...filters, categoryId })}
                            categories={categories}
                            loading={loadingCategories}
                        />
                    </Grid>
                )}

                <Grid item xs={12}>
                    <Button variant="contained" fullWidth size="large" onClick={applyFilters}
                    sx={{
                        backgroundColor: "#5899dd",
                    }}
                    >
                        Filtrele
                    </Button>
                </Grid>
            </Grid>
        </Box>
    );
};

export default FilterContent;