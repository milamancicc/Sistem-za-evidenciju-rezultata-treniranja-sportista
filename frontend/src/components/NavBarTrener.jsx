import React from "react";
import { useNavigate } from "react-router-dom";
import './NavBarTrener.css';

export default function NavBarTrener({korisnik}) {
    
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('korisnik');
        navigate('/');
    };

    return (
        <nav class='navbar-trener'>
            <div class='navbar-trener-container'>
                <div class='navbar-logo' onClick = {() => navigate('/trener-main')}>
                    <span class='logo'>📊</span>
                    <span class='title'>Trener {korisnik?.ime ? `${korisnik.ime[0]}. ${korisnik.prezime}` : 'Profil'}</span>
                </div>
                <div class='navbar-user-section'>
                    {korisnik && (
                        <div class='user-info' onClick={() => navigate('/trener-profil')} title='Pogledaj informacije o treneru'>
                            <span class='username'>👤 {korisnik.korisnickoIme}</span>
                        </div>
                    )}

                    <button class='logout-btn' onClick = {handleLogout} title='Odjavi se'>Odjavi se</button>
                </div>
            </div>
        </nav>
    );

}