import React from "react";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";

const SliderArrow = ({ direction, onClick }) => {
    const isNext = direction === "next";

    return isNext ? (
        <ArrowForwardIosIcon
            onClick={onClick}
            sx={{
                position: "absolute",
                top: "60%",
                right: "0px",
                height: "36px",
                width: "36px",
                transform: "translateY(-50%)",
                cursor: "pointer",
                backgroundColor: "#0F3460",
                borderRadius: "50%",
                boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
                zIndex: 10,
                color: "#FFFFFF",
            }}
        />
    ) : (
        <ArrowBackIosNewIcon
            onClick={onClick}
            sx={{
                position: "absolute",
                top: "60%",
                left: "0px",
                height: "36px",
                width: "36px",
                transform: "translateY(-50%)",
                cursor: "pointer",
                backgroundColor: "#0F3460",
                borderRadius: "50%",
                boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
                zIndex: 10,
                color: "#FFFFFF",

            }}
        />
    );
};

export default SliderArrow;
