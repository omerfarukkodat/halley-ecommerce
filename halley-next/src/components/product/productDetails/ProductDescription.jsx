import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";

const ProductDescription = ({name, brand, size, wallpaperType}) => {
    const getDescription = () => {
        switch (wallpaperType.toLowerCase()) {
            case "folyo":
                return (
                    <>
                        <Typography variant="body2" component="div" sx={{mb: 2}}>
                            <strong>{brand}</strong> markasının özenle tasarlanmış folyosu,
                            dolap kapakları,
                            mutfak dolapları ve parlak olmayan yüzeyler için ideal bir
                            seçenektir.
                            Bu <strong>{size}</strong> boyutundaki yapışkanlı folyo,
                            modern tasarımı ve dayanıklı yapısıyla
                            dikkat çeker.
                        </Typography>
                        <Typography variant="body1" component="div" sx={{mb: 2}}>
                            🔹 <strong>Malzeme</strong>: Folyo
                            <br/>
                            🔹 <strong>Boyut</strong>: {size}
                            <br/>
                            🔹 <strong>Marka</strong>: {brand}
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔧 <strong>Avantajları</strong>
                            <br/>
                            - Parlak olmayan, düz yüzeylere mükemmel yapışma özelliği.
                            <br/>
                            - Kolay temizlenebilir yüzeyiyle pratik bir kullanım sunar.
                            <br/>
                            - Nem ve buhara karşı dayanıklıdır, mutfak ve banyo gibi alanlarda
                            rahatlıkla kullanılabilir.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🏡 <strong>Nerelerde Kullanılır?</strong>
                            <br/>
                            Dolap kapakları, mutfak dolapları, banyo dolapları ve diğer parlak
                            olmayan düz yüzeyler için
                            mükemmel bir seçenektir. Modern ve şık bir görünüm elde etmek
                            isteyenler için idealdir.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            📦 <strong>Sipariş & Teslimat</strong>
                            <br/>
                            Hemen sipariş verin, {brand} kalitesiyle evinize modern bir dokunuş
                            katın!
                        </Typography>
                    </>
                );
            case "emboss":
                return (
                    <>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            <strong>{brand}</strong> markasının özel tasarımı olan emboss duvar
                            kağıdı, tırtıklı (kaba sıva) ve
                            alçı duvarlar için mükemmel bir çözümdür. Bu
                            <strong>{size}
                            </strong> boyutundaki duvar kağıdı,
                            dokulu yapısıyla duvarlarınıza derinlik ve karakter katar.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔹 <strong>Malzeme</strong>: Emboss
                            <br/>
                            🔹 <strong>Boyut</strong>: {size}
                            <br/>
                            🔹 <strong>Marka</strong>: {brand}
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔧 <strong>Avantajları</strong>
                            <br/>
                            - Tırtıklı ve sıvalı duvarlar için özel olarak tasarlanmıştır.
                            <br/>
                            - Alçı duvarlara kolayca uygulanabilir.
                            <br/>
                            - Dokulu yapısı sayesinde duvarlardaki küçük kusurları gizler.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🏡 <strong>Nerelerde Kullanılır?</strong>
                            <br/>
                            Salon, yatak odası, oturma odası ve ofis gibi alanlarda kullanıma
                            uygundur. Özellikle dokulu
                            duvarlara sahip mekanlarda şık bir görünüm sunar.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            📦 <strong>Sipariş & Teslimat</strong>
                            <br/>
                            Hemen sipariş verin, {brand} kalitesiyle duvarlarınıza estetik bir
                            dokunuş ekleyin!
                        </Typography>
                    </>
                );
            case "vinil":
                return (
                    <>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            <strong>{brand}</strong> markasının vinil duvar kağıdı, alçı
                            duvarlar için ideal bir seçenektir.
                            Bu <strong>{size}</strong> boyutundaki duvar kağıdı, suya ve neme
                            karşı dayanıklı yapısıyla öne
                            çıkar.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔹 <strong>Malzeme</strong>: Vinil
                            <br/>
                            🔹 <strong>Boyut</strong>: {size}
                            <br/>
                            🔹 <strong>Marka</strong>: {brand}
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔧 <strong>Avantajları</strong>
                            <br/>
                            - Suya ve neme karşı dayanıklıdır, banyo ve mutfak gibi alanlarda
                            kullanıma uygundur.
                            <br/>
                            - Kolay temizlenebilir yüzeyiyle pratik bir kullanım sunar.
                            <br/>
                            - Uzun ömürlü ve dayanıklıdır.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🏡 <strong>Nerelerde Kullanılır?</strong>
                            <br/>
                            Banyo, mutfak, oturma odası ve ofis gibi alanlarda kullanıma
                            uygundur. Özellikle nemli
                            ortamlarda tercih edilir.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            📦 <strong>Sipariş & Teslimat</strong>
                            <br/>
                            Hemen sipariş verin, {brand} kalitesiyle duvarlarınıza modern bir
                            dokunuş katın!
                        </Typography>
                    </>
                );
            case "non-woven":
                return (
                    <>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            <strong>{brand}</strong> markasının non-woven duvar kağıdı, alçı
                            duvarlar için mükemmel bir
                            seçenektir. Bu <strong>{size}</strong> boyutundaki duvar kağıdı,
                            nefes alabilen yapısıyla
                            sağlıklı bir ortam sunar.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔹 <strong>Malzeme</strong>: Non-Woven
                            <br/>
                            🔹 <strong>Boyut</strong>: {size}
                            <br/>
                            🔹 <strong>Marka</strong>: {brand}
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔧 <strong>Avantajları</strong>
                            <br/>
                            - Nefes alabilen yapısı sayesinde küf ve nem oluşumunu engeller.
                            <br/>
                            - Kolay uygulanabilir ve sökülebilir yapısıyla pratik bir kullanım
                            sunar.
                            <br/>
                            - Uzun ömürlü ve dayanıklıdır.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🏡 <strong>Nerelerde Kullanılır?</strong>
                            <br/>
                            Yatak odası, oturma odası, ofis ve çocuk odası gibi alanlarda
                            kullanıma uygundur. Özellikle
                            sağlıklı bir ortam isteyenler için idealdir.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            📦 <strong>Sipariş & Teslimat</strong>
                            <br/>
                            Hemen sipariş verin, {brand} kalitesiyle duvarlarınıza sağlıklı
                            ve şık bir dokunuş ekleyin!
                        </Typography>
                    </>
                );
            default:
                return (
                    <>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            <strong>{brand}</strong> markasının kaliteli duvar kağıtlarıyla
                            evinize veya iş yerinize şıklık
                            katın! Bu <strong>{size}</strong> boyutundaki duvar kağıdı,
                            dayanıklı yapısı ve modern
                            tasarımıyla dikkat çeker.
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            🔹 <strong>Malzeme</strong>: {wallpaperType}
                            <br/>
                            🔹 <strong>Boyut</strong>: {size}
                            <br/>
                            🔹 <strong>Marka</strong>: {brand}
                        </Typography>
                        <Typography variant="body2" component="div" sx={{fontWeight: 500, mb: 2}}>
                            📦 <strong>Sipariş & Teslimat</strong>
                            <br/>
                            Hemen sipariş verin, {brand} kalitesiyle duvarlarınıza modern bir
                            dokunuş katın!
                        </Typography>
                    </>
                );
        }
    };

    return (
        <Box sx={{textAlign: "center", mt: 4, mb: 4}}>
            <Typography variant="h4" component="h1" sx={{fontWeight: 700, mb: 4}}>
                Ürün Bilgileri - {name}
            </Typography>
            <Box sx={{maxWidth: "800px", margin: "0 auto", textAlign: "left"}}>
                {getDescription()}
            </Box>
        </Box>
    );
};

export default ProductDescription;