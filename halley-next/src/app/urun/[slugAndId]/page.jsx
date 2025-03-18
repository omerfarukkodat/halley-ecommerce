"use client";

import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import productService from "@/services/productService";
import {ProductDetails} from "@/components/product/productDetails/ProductDetails";
import {useCart} from "@/context/CartContext";

const ProductDetailsPage = () => {
    const { slugAndId } = useParams();
    const [loading, setLoading] = useState(true);
    const [product, setProduct] = useState(null);
    const router = useRouter();
    const { cart } = useCart();

    useEffect(() => {
        if (!slugAndId) {
            setLoading(false);
            return;
        }

        const lastDashIndex = slugAndId.lastIndexOf("-");
        const slug = slugAndId.substring(0, lastDashIndex);
        const id = parseInt(slugAndId.substring(lastDashIndex + 1));

        setLoading(true);

        productService
            .getProductById(slug,id)
            .then((response) => {
                console.log(response.data);
                const content  = response.data;
                setProduct(content);
                setLoading(false);
            })
            .catch((error) => {
                setLoading(false);
            });
    }, [slugAndId]);

    if (loading) {
        return <h6></h6>;
    }

    const cartItems = cart?.items || [];
    const cartItem = cartItems.find(item => item.product.id === product.id);
    const productQuantity = cartItem ? cartItem.quantity : 0;

    return (
        <ProductDetails key={product.id} product={product} productQuantity={productQuantity} />
    );
};

export default ProductDetailsPage;
