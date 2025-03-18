import React from "react";
import { Breadcrumbs, Typography, Box } from "@mui/material";
import NextLink from "next/link";

const Breadcrumb = ({ basePath, pageTitle }) => {
    const paths = basePath.split("/").filter((path) => path);

    return (
        <Breadcrumbs aria-label="breadcrumb" separator=">" sx={{color: "#0f3460",mb:3,mt:3, fontSize: 14,ml:3 }}>
            <Box
                component={NextLink}
                href="/"
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
            {paths.map((path, index) => {
                const routeTo = `/${paths.slice(0, index + 1).join("/")}`;
                const isLast = index === paths.length - 1;

                return isLast ? (
                    <Typography key={path} sx={{color: "#0f3460", fontSize: 14 }}>
                        {pageTitle}
                    </Typography>
                ) : (
                    <Box
                        key={path}
                        component={NextLink}
                        href={routeTo}
                        sx={{
                            textDecoration: "none",
                            color: "#0f3460",
                            fontSize: 14,
                            cursor: "pointer",
                            "&:hover": { color: "#000" }
                        }}
                    >
                        {path}
                    </Box>
                );
            })}
        </Breadcrumbs>
    );
};

export default Breadcrumb;
