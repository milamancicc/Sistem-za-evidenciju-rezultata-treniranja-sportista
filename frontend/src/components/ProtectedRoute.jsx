import { Navigate } from "react-router-dom";

export default function ProtectedRoute({children, dozvoljenTipKorisnika}) {
    const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
    const korisnik = sacuvaniKorisnik ? JSON.parse(sacuvaniKorisnik) : null;

    if(!korisnik){
        return <Navigate to='/' replace/>
    }

    if(dozvoljenTipKorisnika && korisnik.tipKorisnika !== dozvoljenTipKorisnika){
        return <Navigate to='/' replace/>
    }

    return children;
}