
module.exports = {
    images: {
        domains: ['res.cloudinary.com' , 'superdekor.com.tr', 'www.adawall.com.tr','www.grahambrown.com' ],
    },
    reactStrictMode: true,
    async redirects() {
        return [
            {
                source: '/error',
                destination: '/',
                permanent: false,
            },
        ]
    },
}
