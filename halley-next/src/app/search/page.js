"use client";

import productService from "@/services/productService";
import ProductListingPage from "@/components/product/ProductListingPage";
import {useSearchParams} from "next/navigation";


const SearchProductsPage = () => {
    const searchParams = useSearchParams();
    const searchTerm = searchParams.get("term") || "";

    return (

        <ProductListingPage
            fetchDataFunction={(filters) => productService.getProductsBySearch(searchTerm, filters)}
            basePath={`/search`}
            pageTitle="Ürün Arama"
            visibleFilters={{
                price: true,
                brand: true,
                wallpaperType: true,
                wallpaperSize: true,
                category:true
            }}
            additionalFilters={{}}
        />
    );

};

export default SearchProductsPage;