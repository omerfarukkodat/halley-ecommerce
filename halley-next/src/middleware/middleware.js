
import { NextResponse } from "next/server";

export function middleware(req) {
    const authHeader = req.headers.get("authorization");
    const token = authHeader?.startsWith("Bearer ") ? authHeader.split(" ")[1] : null;

    const protectedRoutes = ["/uyelik", "/uyelik/adreslerim", "/uyelik/siparislerim", "/uyelik/profilim"];
    const authRoutes = ["/giris", "/uye-ol", "/reset-password", "/reset-password/request"];

    const { pathname } = req.nextUrl;

    if (!token && protectedRoutes.includes(pathname)) {
        return NextResponse.redirect(new URL("/giris", req.url));
    }

    if (token && authRoutes.includes(pathname)) {
        return NextResponse.redirect(new URL("/", req.url));
    }

    return NextResponse.next();
}
