"use client";

import React, { useEffect, useRef, useState, useCallback } from "react";
import { Box, Typography } from "@mui/material";
import { v4 as uuidv4 } from 'uuid';
import ProductCardForCart from "@/components/cart/ProductCardForCart";

const ProductList = ({ title, fetchFunction, initialPage = 0, size = 10 }) => {
    const [products, setProducts] = useState([]);
    const [page, setPage] = useState(initialPage);
    const [hasMore, setHasMore] = useState(true);
    const observer = useRef(null);

    const fetchProducts = useCallback(() => {
        if (!hasMore) return;

        fetchFunction(page, size)
            .then((response) => {
                const { content } = response.data;

                if (content.length > 0) {
                    setProducts((prev) => {
                        const uniqueProducts = new Map(prev.map(p => [p.id, p]));
                        content.forEach(p => uniqueProducts.set(p.id, p));
                        return Array.from(uniqueProducts.values());
                    });

                    setPage((prevPage) => prevPage + 1);
                } else {
                    setHasMore(false);
                }
            })
            .catch((error) => {
                console.error("Error fetching products:", error);
            });
    }, [page, size, hasMore, fetchFunction]);

    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    const lastProductRef = useCallback((node) => {
        if (!hasMore) return;
        if (observer.current) observer.current.disconnect();

        observer.current = new IntersectionObserver((entries) => {
            if (entries[0].isIntersecting) {
                fetchProducts();
            }
        });

        if (node) observer.current.observe(node);
    }, [fetchProducts, hasMore]);

    return (
        <Box sx={{ position: "relative" }}>
            <Box
                sx={{
                    position: "relative",
                    height: { xs: "90px", sm: "130px", md: "150px", lg: "175px" },
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    borderRadius: "8px",
                    overflow: "hidden",
                    boxShadow: "0 4px 6px rgba(0,0,0,0.1)",
                }}
            >
                <Typography
                    sx={{
                        textAlign: "center",
                        justifyContent: "center",
                        fontWeight: "bold",
                        fontSize: "24px",
                        color: "#2a2a2a",
                        zIndex: 2,
                    }}
                >
                    {title}
                </Typography>
            </Box>

            <Box
                display="grid"
                gridTemplateColumns={{
                    xs: "repeat(auto-fill, minmax(170px, 1fr))",
                    sm: "repeat(auto-fill, minmax(250px, 1fr))",
                    md: "repeat(auto-fill, minmax(300px, 1fr))",
                    lg: "repeat(auto-fill, minmax(300px, 1fr))",
                    xl: "repeat(auto-fill, minmax(350px, 1fr))",
                }}
                gap={0.5}
                sx={{ mt: 1 }}
            >
                {products.map((product, index) => {
                    const key = uuidv4();

                    if (index === products.length - 1) {
                        return <ProductCardForCart ref={lastProductRef} key={key} product={product} />;
                    }
                    return <ProductCardForCart key={key} product={product} />;
                })}
            </Box>
        </Box>
    );
};

export default ProductList;