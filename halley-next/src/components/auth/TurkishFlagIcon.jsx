"use client";

import { SvgIcon } from "@mui/material";

const TurkishFlagIcon = (props) => (
    <SvgIcon {...props} viewBox="0 0 48 32">
        <rect width="48" height="32" fill="#E30A17" />
        <circle cx="16" cy="16" r="6" fill="white" />
        <circle cx="18" cy="16" r="5" fill="#E30A17" />
        <path
            fill="white"
            d="M22.5 16l3 1.2-1.8-2.6 1.8-2.6-3 1.2-1-2.8-1 2.8-3-1.2 1.8 2.6-1.8 2.6 3-1.2 1 2.8 1-2.8z"
        />
    </SvgIcon>
);

export default TurkishFlagIcon;
