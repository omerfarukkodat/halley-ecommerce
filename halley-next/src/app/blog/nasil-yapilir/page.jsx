"use client";
import React from "react";
import {
    Typography,
    Paper,
    List,
    ListItem,
    ListItemText,
    Box,
    Divider,
    useTheme,
} from "@mui/material";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import Head from "next/head";

const WallpaperGuide = () => {
    const theme = useTheme();

    return (
        <>
            <Head>
                <title>Duvar Kağıdı Hesaplama ve Uygulama Rehberi | HalleyWall</title>
                <meta
                    name="description"
                    content="Duvar kağıdı hesaplama ve uygulama rehberi. Doğru ölçüm teknikleri, desen uyumu, farklı duvar tipleri için ipuçları ve daha fazlası."
                />
                <meta
                    name="keywords"
                    content="duvar kağıdı, duvar kağıdı hesaplama, duvar kağıdı uygulama, duvar kağıdı ölçümü"
                />
                <meta name="author" content="HalleyWall"/>
                <meta property="og:title" content="Duvar Kağıdı Hesaplama ve Uygulama Rehberi"/>
                <meta
                    property="og:description"
                    content="Duvar kağıdı hesaplama ve uygulama rehberi. Doğru ölçüm teknikleri, desen uyumu,
                     farklı duvar tipleri için ipuçları ve daha fazlası."
                />
                <meta property="og:image" content="/images/og-image.jpg"/>
                <meta property="og:url" content="https://www.halleywall.com/blog/nasil-yapilir"/>
                <meta name="twitter:card" content="summary_large_image"/>
                <meta name="twitter:title" content="Duvar Kağıdı Hesaplama ve Uygulama Rehberi"/>
                <meta
                    name="twitter:description"
                    content="Duvar kağıdı hesaplama ve uygulama rehberi. Doğru ölçüm teknikleri, desen uyumu,
                     farklı duvar tipleri için ipuçları ve daha fazlası."
                />
                <meta name="twitter:image" content="/images/og-image.jpg"/>
            </Head>
            <Box
                maxWidth="md"
                sx={{
                    minHeight: "100vh",
                    py: 6,
                    margin: 1
                }}
            >
                <Paper
                    elevation={0}
                    sx={{}}
                >
                    <Box>
                        {/* Başlık */}
                        <Typography
                            variant="h4"
                            gutterBottom
                            sx={{
                                fontWeight: 200,
                                color: "#000000",
                                textAlign: "center",
                                mb: 4,
                            }}
                        >
                            Duvar Kağıdı Hesaplama ve Uygulama Rehberi
                        </Typography>
                        <Divider sx={{my: 4, borderColor: "gray"}}/>

                        <img

                            src="/product-guide-measure.svg"
                            alt="Nasıl Ölçülür?"
                            style={{
                                width: "100%",
                                maxWidth: "400px",
                                height: "auto",
                                maxHeight: "300px",
                                borderRadius: "4px",
                            }}
                        />
                    </Box>
                    <Typography
                        paragraph
                        sx={{
                            lineHeight: 1.8,
                            mb: 4,
                            fontWeight: 500,
                            color: "#000000",
                        }}
                    >
                        Duvar kağıdı, mekanlara karakter katan bir dekorasyon unsuru olsa da
                        yanlış hesaplama maliyet artışı ve zaman kaybına yol açabilir. Bu
                        rehberde, doğru ölçüm tekniklerinden desen uyumuna, farklı duvar
                        tipleri için ipuçlarından ekstra malzeme hesabına kadar tüm detayları
                        bulacaksınız. İşte profesyonel bir uygulama için izlemeniz gereken
                        adımlar:
                    </Typography>
                    <Box mt={4}>
                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 600,
                                color: "#000000",
                                mb: 2,
                            }}
                        >
                            1. Neden Doğru Ölçüm ve Hesaplama Kritiktir?
                        </Typography>
                        <List>
                            {[
                                "Fazla malzeme alımını önleyerek bütçenizi koruyun.",
                                "Desen kayması veya eksik rulo gibi sorunları engelleyin.",
                                "Eğimli duvarlar veya kapı/pencere çıkıntıları için hatasız uygulama yapın.",
                            ].map((text, index) => (
                                <ListItem key={index} sx={{pl: 0}}>
                                    <CheckCircleIcon
                                        sx={{color: theme.palette.success.main, mr: 2}}
                                    />
                                    <ListItemText
                                        primary={text}
                                        primaryTypographyProps={{
                                            fontWeight: 500,
                                            color: "#000000",
                                        }}
                                    />
                                </ListItem>
                            ))}
                        </List>
                    </Box>

                    <Divider sx={{my: 4}}/>
                    <Box mt={4}>
                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 600,
                                color: "#000000",
                                mb: 2,
                            }}
                        >
                            2. Duvar Kağıdı Ölçümü: Temel Adımlar
                        </Typography>
                        <Typography
                            variant="body1"
                            paragraph
                            sx={{color: theme.palette.text.secondary}}
                        >
                            A. Genişlik ve Yükseklik Ölçümü
                        </Typography>
                        <List>
                            {[
                                "Her duvarı ayrı ayrı ölçün (Örn: 4m genişlik × 2,5m yükseklik = 10m²).",
                                "Kapı ve pencereleri hesaba katın: Küçük alanları çıkarmak yerine, güvenlik payı için" +
                                " dahil edin (özellikle desenli kağıtlar için)." +
                                "\n",
                            ].map((text, index) => (
                                <ListItem key={index} sx={{pl: 0}}>
                                    <CheckCircleIcon
                                        sx={{color: theme.palette.success.main, mr: 2}}
                                    />
                                    <ListItemText
                                        primary={text}
                                        primaryTypographyProps={{
                                            fontWeight: 500,
                                            color: "#000000",
                                        }}
                                    />
                                </ListItem>
                            ))}
                        </List>
                    </Box>

                    <Divider sx={{my: 4}}/>

                    <Box mt={4}>
                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 600,
                                color: "#000000",
                                mb: 2,
                            }}
                        >
                            3. Duvar Kağıdı Hesaplama Formülü
                        </Typography>
                        <Typography
                            variant="body1"
                            paragraph
                            sx={{color: theme.palette.text.secondary}}
                        >
                            Örnek hesaplama:
                        </Typography>
                        <List>
                            {[
                                "Tüm duvarların alanı - Kapı/pencere alanları = Net alan",
                                "Net alan / 5.3 = Rulo sayısı",
                                "Örnek: 54m² ÷ 5,3 ≈ 11 rulo.",
                            ].map((text, index) => (
                                <ListItem key={index} sx={{pl: 0}}>
                                    <CheckCircleIcon
                                        sx={{color: theme.palette.success.main, mr: 2}}
                                    />
                                    <ListItemText
                                        primary={text}
                                        primaryTypographyProps={{
                                            fontWeight: 500,
                                            color: "#000000",
                                        }}
                                    />
                                </ListItem>
                            ))}
                        </List>
                    </Box>

                    <Divider sx={{my: 4}}/>

                    <Box mt={4}>
                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 600,
                                color: "#000000",
                                mb: 2,
                            }}
                        >
                            4. Farklı Duvar Tipleri İçin Ölçüm Teknikleri
                        </Typography>
                        <Typography
                            variant="body1"
                            paragraph
                            sx={{
                                color: theme.palette.text.secondary,
                            }}
                        >
                            Her duvar tipi özel bir yaklaşım gerektirir:
                        </Typography>
                        <List>
                            {[
                                {
                                    image: "/measuring-1.svg",
                                    text:
                                        "Standart duvarlar: Tüm duvarın genişliğini ve yüksekliğini ölçün. Lütfen doğruluğundan " +
                                        "emin olmak için çalışmanızı iki kez kontrol edin. Hafifçe engebeli duvarlar için her " +
                                        "dört tarafa 2 santim daha ekleyeceğiz.",
                                },
                                {
                                    text:
                                        "Eğimli duvarlar: Duvarı en yüksek ve en geniş noktadan ölçün, eğimleri göz ardı edin." +
                                        " Duvar kağıdı, montaj sırasında eğimli kısmı çıkarılacak şekilde, bütün bir dikdörtgen" +
                                        " veya kare olarak basılacak ve teslim edilecektir. Hafifçe engebeli duvarlar için her " +
                                        "dört tarafa 2 santim ekleyeceğiz.",
                                    image: "/measuring-2.svg", // Resim dosyasının yolu
                                },
                                {
                                    text:
                                        "Birden fazla eğimli duvarlar: Duvarı hem genişlik hem de yükseklik açısından en uzun " +
                                        "kısmı boyunca ölçün. Duvar kağıdı, montaj sırasında eğimli kısmı çıkarılmış bir " +
                                        "dikdörtgen veya kare olarak şekillenecektir. Hafifçe engebeli duvarlar için her dört" +
                                        " tarafa 2 santim daha ekleyeceğiz.",
                                    image: "/measuring-3.svg", // Resim dosyasının yolu
                                },
                                {
                                    text:
                                        "Kapı/Pencereli duvarlar: Tüm duvarın genişliğini ve yüksekliğini alın. Montaj sırasında" +
                                        " yapıma engel olan tüm engeller kaldırılacaktır. Hafifçe engebeli duvarlara uyum sağlamak" +
                                        " için dört tarafa da 2 santim ekleyeceğiz.",
                                    image: "/measuring-4.svg", // Resim dosyasının yolu
                                },
                                {
                                    text:
                                        "Birden fazla duvar için ölçüm: Sipariş verirken, duvarlarınızın her birinin genişliğini" +
                                        " ölçün , yüksekliğiyle çarpın.Aynı yüksekliğe sahipse duvarlarınız , her bir duvarın " +
                                        "genişliğini toplayın ve yüksekliğiyle çarpın.",
                                    image: "/measuring-5.svg", // Resim dosyasının yolu
                                },
                                {
                                    text:
                                        "Merdiven içeren duvarlar için ölçüm: Merdiven içeren duvarın yüksekliğini ölçerken " +
                                        "lütfen duvarın en yüksek ve en alçak noktasını belirleyin. Aynısı genişlik için de" +
                                        " geçerlidir. Montaj sırasında tüm eğimli parçalar kaldırılacaktır. Hafifçe engebeli" +
                                        " duvarlara uyum sağlamak için dört tarafa da 2 santim ekleyeceğiz.",
                                    image: "/measuring-6.svg", // Resim dosyasının yolu
                                },
                            ].map((item, index) => (
                                <React.Fragment key={index}>

                                    <Box sx={{display: "flex", justifyContent: "center", my: 2}}>
                                        <img
                                            src={item.image} // Resim dosyasının yolu
                                            alt={`Ölçü adım  ${index + 1}`}
                                            style={{
                                                maxWidth: "100%",
                                                height: "auto",
                                                borderRadius: "8px",
                                                boxShadow: theme.shadows[3],
                                            }}
                                        />
                                    </Box>
                                    <ListItem sx={{pl: 0}}>
                                        <CheckCircleIcon
                                            sx={{color: theme.palette.success.main, mr: 2, mb: 8}}
                                        />
                                        <ListItemText
                                            primary={item.text}
                                            primaryTypographyProps={{
                                                fontWeight: 500,
                                                color: "#000000",
                                                mb: 8
                                            }}
                                        />
                                    </ListItem>
                                </React.Fragment>
                            ))}
                        </List>
                    </Box>

                    <Divider sx={{my: 4}}/>

                    <Box mt={4}>
                        <Typography
                            variant="h5"
                            sx={{
                                fontWeight: 600,
                                color: "#000000",
                                mb: 2,
                            }}
                        >
                            5. Son Adım: Uygulama Öncesi Kontrol Listesi
                        </Typography>
                        <List>
                            {[
                                "Ölçümlerinizi iki kez kontrol edin.",
                                "Desen raporunu ve rulo sayısını teyit edin.",
                                "Duvar yüzeyini temizleyin.",
                            ].map((text, index) => (
                                <ListItem key={index} sx={{pl: 0}}>
                                    <CheckCircleIcon
                                        sx={{color: theme.palette.success.main, mr: 2}}
                                    />
                                    <ListItemText
                                        primary={text}
                                        primaryTypographyProps={{
                                            fontWeight: 500,
                                            color: "#000000",
                                        }}
                                    />
                                </ListItem>
                            ))}
                        </List>
                    </Box>
                </Paper>
            </Box>
        </>

    );
};

export default WallpaperGuide;