"use client";
import { useEffect } from 'react';

const useScrollPosition = () => {
    useEffect(() => {
        const savedPosition = localStorage.getItem('scrollPosition');
        if (savedPosition) {
            window.scrollTo({
                top: parseInt(savedPosition, 10),
                behavior: 'smooth',
            });
        }

        const handleScroll = () => {
            localStorage.setItem('scrollPosition', window.scrollY);
        };

        window.addEventListener('scroll', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);
};

export default useScrollPosition;