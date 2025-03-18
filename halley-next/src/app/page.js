
import {Box} from "@mui/material";
import DiscountedProducts from "@/components/home/DiscountedProduct";
import Brands from "@/components/brands/Brands";
import AllProductsBanner from "@/components/home/AllProductsBanner";
import LastAddedProducts from "@/components/home/NewArrival";
import FeaturedProducts from "@/components/home/FeaturedProducts";
import ShopByDesignSlider from "@/components/home/ShopByDesignSlider";
import RoomSlider from "@/components/home/ShopByRoomSlider";
import ShopByColourSlider from "@/components/home/ShopByColourSlider";
import HomeBlogSection from "@/components/blog/HomeBlogSection";


export default function Home() {
  return (
      <Box sx={{ width:'100%', p: 0 }} >
          <AllProductsBanner/>
          <RoomSlider title="Odaya Göre Alışveriş" />
          <LastAddedProducts page={0} size={15}/>
          <ShopByDesignSlider title="Desene Göre Alışveriş" />
          <DiscountedProducts page={0} size={15}/>
          <ShopByColourSlider title="Renklere Göre Ürünler" />
          <FeaturedProducts limit={5}/>
          <Brands/>
          <HomeBlogSection/>
      </Box>
  )
}
