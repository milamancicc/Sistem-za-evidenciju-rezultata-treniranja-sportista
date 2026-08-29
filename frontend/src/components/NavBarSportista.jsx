import React from "react";
import { useNavigate } from "react-router-dom";
import './NavBarSportista.css'

export default function NavBarSportista({korisnik}) {
    
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('korisnik');
        navigate('/')
    };

    return(
        <nav class='navbar-sportista'>
            <div class='navbar-sportista-container'>
                <div class='navbar-logo' onClick={() => navigate('/sportista-main')}>
                    <span class='logo'>🏃</span>
                    <span class='title'>Sportista {korisnik?.ime} {korisnik?.prezime}</span>
                </div>

                <div class='navbar-user-section'>
                    {korisnik && (
                        <div class='user-info' /*onClick={() => navigate('/sportista-profil')}*/ title='Pogledajte informacije o profilu'>
                            <span class='username'>👤{korisnik.korisnickoIme}</span>
                        </div>
                    )}

                    <button class='logout-btn' onClick={handleLogout} title='Odjavi se'>
                        Odjavi se
                    </button>
                </div>
            </div>
        </nav>
    )

}