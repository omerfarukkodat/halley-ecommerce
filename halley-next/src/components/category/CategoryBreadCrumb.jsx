"use client";

import { useRouter } from "next/navigation";
import {Breadcrumbs, Box, CircularProgress} from "@mui/material";

const CategoryBreadCrumb = ({ categoryPaths }) => {
    const router = useRouter();

    if (!categoryPaths) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "21px" }}>
                <CircularProgress size={21} />
            </Box>
        );
    }

    if (categoryPaths.length === 0) {
        return null;
    }

    return (
        <Box>
            <Breadcrumbs aria-label="breadcrumb" separator=">" sx={{ ml: 3,mb:3,mt:3, fontSize: 14,color: "#0f3460"
            }}>
                <Box
                    onClick={() => router.push("/")}
                    sx={{
                        textDecoration: "none",
                        color: "#0f3460",
                        fontSize: 14,
                        cursor: "pointer",
                        "&:hover": { color: "#000" }
                    }}
                >
                    Ana Sayfa
                </Box>
                {categoryPaths.map((category) => (
                    <Box
                        key={category.id}
                        onClick={() => router.push(`/kategori/${category.slug}`)}
                        sx={{
                            textDecoration: "none",
                            color: "#0f3460",
                            fontSize: 14,
                            cursor: "pointer",
                            "&:hover": { color: "#000" }
                        }}
                    >
                        {category.name}
                    </Box>
                ))}
            </Breadcrumbs>
        </Box>
    );
};

export default CategoryBreadCrumb;