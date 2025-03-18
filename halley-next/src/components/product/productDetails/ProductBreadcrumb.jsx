"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Breadcrumbs, Box } from "@mui/material";
import { getCategoryPaths } from "@/services/productService";
import { useCategory } from "@/context/CategoryContext";

const ProductBreadcrumb = ({ product }) => {
    const [categoryPaths, setCategoryPaths] = useState([]);
    const router = useRouter();
    const { category } = useCategory();

    useEffect(() => {
        if (product?.id) {
            getCategoryPaths(product.id, category?.id || null)
                .then(response => setCategoryPaths(response.data))
                .catch(error => console.error("Hata:", error));
        }
    }, [product]);

    if (!product || !categoryPaths || categoryPaths.length === 0) {
        return null;
    }

    return (
        <Breadcrumbs aria-label="breadcrumb" separator=">" sx={{ mb: 6, fontSize: 14 , color: "#0f3460" }}>
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
    );
};

export default ProductBreadcrumb;
