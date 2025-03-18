import React from "react";
import {
    Box,
    Button,
    Collapse,
    Divider
} from "@mui/material";
import KeyboardArrowRightIcon from '@mui/icons-material/KeyboardArrowRight';

const CategoryDropdown = ({
                              label,
                              items,
                              isHovered,
                              onMouseEnter,
                              onMouseLeave,
                              onClickItem,
                          }) => {
    return (
        <Box
            onMouseEnter={onMouseEnter}
            onMouseLeave={onMouseLeave}
            sx={{ position: "relative", height: "100%", display: "flex", alignItems: "center" }}
        >
            <Button
                sx={{
                    color: "#0F3460",
                    fontWeight: "bold",
                    fontSize: "16px",
                    textTransform: "none",
                    fontFamily: "Quicksand",
                    ":hover": { color: "orange" }
                }}
            >
                {label}
            </Button>

            <Collapse
                in={isHovered}
                sx={{
                    position: "absolute",
                    top: "100%",
                    left: 0,
                    zIndex: 10,
                    minWidth: 250
                }}
            >
                <Box
                    sx={{
                        bgcolor: "white",
                        boxShadow: 3,
                        borderRadius: 1,
                        mt: 1,
                        py: 1
                    }}
                >
                    {items.map((item, index) => (
                        <Box key={item.id}>
                            <Box
                                sx={{ position: "relative" }}
                            >
                                <Button
                                    onClick={() => onClickItem(item.slug)}
                                    fullWidth
                                    sx={{
                                        justifyContent: "space-between",
                                        px: 3,
                                        color: "#0F3460",
                                        fontWeight: "bold",
                                        textTransform: "none",
                                        fontFamily: "Quicksand",
                                        ":hover": {
                                            bgcolor: "#f5f5f5",
                                            color: "orange",
                                            "& .arrow-icon": {
                                                color: "orange"
                                            }
                                        }
                                    }}
                                >
                                    {item.name}
                                    <KeyboardArrowRightIcon
                                        className="arrow-icon"
                                        sx={{
                                            fontSize: 18,
                                            color: "#0F3460",
                                            ml: 1
                                        }}
                                    />
                                </Button>
                            </Box>
                            {index < items.length - 1 && (
                                <Divider sx={{ mx: 2 }} />
                            )}
                        </Box>
                    ))}
                </Box>
            </Collapse>
        </Box>
    );
};

export default CategoryDropdown;