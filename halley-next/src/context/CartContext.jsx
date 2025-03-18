"use client";

import {createContext, useState, useContext, useEffect} from "react";
import {getCart} from "@/services/cartService";
import {useAuth} from "@/context/AuthContext";

const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cartItemCount, setCartItemCount] = useState(0);
    const [cart, setCart] = useState({ items: [] });
    const {user} = useAuth();


    const updateCartCount = (count) => {
        setCartItemCount(count);
    }

    const updateCart = (newCart) => {
        setCart(newCart);
        setCartItemCount(newCart.items.length);
    };

    useEffect(() => {
        const fetchCart = async () => {
            try {
                const cartData = await getCart();
                setCart(cartData);
                setCartItemCount(cartData.items.length);
            } catch (error) {
                console.log('Error fetching cart data:', error);
            }
        };
        if (user) {
            fetchCart();
        } else {
            setCart({items: []});
            setCartItemCount(0);
            fetchCart();

        }

    }, [user]);

    return (
        <CartContext.Provider value={{cart ,  cartItemCount , updateCart , updateCartCount }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => useContext(CartContext);
