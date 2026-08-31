import React, {useState, useEffect, useRef} from "react";
import { useNavigate } from "react-router-dom";
import './NavBarTrener.css';

export default function NavBarTrener({korisnik}) {
    
    const navigate = useNavigate();

    const [aktivniOtvoren, setAktivniOtvoren] = useState(false);
    const[aktivni, setAktivni] = useState([]);
    const dropdownRef = useRef(null);


    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

    useEffect(() => {
        const fetchAktivni = async () => {
            try{
                const res = await fetch('http://localhost:8080/api/auth/aktivni', {
                    headers: getAuthHeaders()
                });
                if(res.ok){
                    const data = await res.json();
                    setAktivni(data);
                }
            }catch(err){
                console.error('Greska pri ucitavanju aktivnih korisnika: ', err);
                
            };
        }
        fetchAktivni();
        const interval = setInterval(fetchAktivni, 5000);
        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if(dropdownRef.current && !dropdownRef.current.contains(event.target)){
                setAktivniOtvoren(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleLogout = async () => {
        try{
            const korisnickiPodaci = JSON.parse(sessionStorage.getItem('korisnik'));
            if(korisnickiPodaci && korisnickiPodaci.korisnickoIme){
                await fetch('http://localhost:8080/api/auth/logout', {
                    method:'POST',
                    headers: getAuthHeaders(),
                    body: JSON.stringify({korisnickoIme: korisnickiPodaci.korisnickoIme})
                });
            }
        }catch(err){
            console.error("Greska pri odjavi: ", err);
            
        }finally{
            sessionStorage.removeItem('korisnik');
            sessionStorage.removeItem('token');
            navigate('/');
        }
    };

    return (
        <nav class='navbar-trener'>
            <div class='navbar-trener-container'>
                <div class='navbar-logo' onClick = {() => navigate('/trener-main')}>
                    <span class='logo'>📊</span>
                    <span class='title'>Trener {korisnik?.ime ? `${korisnik.ime[0]}. ${korisnik.prezime}` : 'Profil'}</span>
                </div>
                <div class='navbar-user-section'>
                    <div class='nav-item' onClick = {() => navigate('/trener-sportisti')} title='Pregled svih i dodavanje novih sportista'>
                        <span class='nav-label'>🏃Sportisti</span>
                    </div>
                    <div class='nav-item' onClick = {() => navigate('/trener-vezbe')} title='Pregled vežbi i dodavanje novih'>
                        <span class='nav-label'>🔩Vežbe</span>
                    </div>
                    {korisnik && (
                        <div class='user-info' onClick={() => navigate('/trener-profil')} title='Pogledajte informacije o treneru'>
                            <span class='username'>👤{JSON.parse(sessionStorage.getItem('korisnik')).korisnickoIme}</span>
                        </div>
                    )}

                    <div class='navbar-dropdown-container' ref={dropdownRef}>
                        <button class='navbar-dropdown-btn' onClick={() => setAktivniOtvoren(!aktivniOtvoren)}>🟢Aktivni ({aktivni.length})</button>
                        {aktivniOtvoren && (
                            <div class='navbar-dropdown-content'>
                                <h4>Trenutno u sistemu:</h4>
                                {aktivni.length === 0 ? (
                                    <p class='nema-aktivnih-tekst'>Nema drugih aktivnih korisnika</p>
                                ) : (
                                    <ul>
                                        {aktivni.map((username, index) => (
                                            <li key={index}>
                                                <span>🟢</span>{(username === korisnik.korisnickoIme) ? `${username} (Vi)` : `${username}`}
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        )}
                    </div>

                    <button class='logout-btn' onClick = {handleLogout} title='Odjavi se'>Odjavi se</button>
                </div>
            </div>
        </nav>
    );

}