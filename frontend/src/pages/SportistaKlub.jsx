import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import NavBarSportista from "../components/NavBarSportista";
import './SportistaKlub.css'

export default function SportistaKlub(){

    const [korisnik, setKorisnik] = useState(null);
    const [klub, setKlub] = useState(null);

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

    const ucitajKlub = async (id) => {
        if(!id) return;
        try{
            const res = await fetch(`http://localhost:8080/api/klubovi/${id}`, {
                headers: getAuthHeaders()
            });
            if(!res.ok){
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                throw new Error("Greska pri preuzimanju imena mesta");
            }
            const data = await res.json();
            setKlub(data);
        }catch(err){
            console.error('Greska: ', err);
            
        }
    }

    const ucitajProfil = async () => {
        const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
        if (sacuvaniKorisnik) {
            try {
                const parsed = JSON.parse(sacuvaniKorisnik);
                const id = parsed.id;
                setKorisnik(parsed);
                if (!id) return;

                fetch(`http://localhost:8080/api/sportisti/${id}`, {
                    headers: getAuthHeaders()
                })
                    .then((res) => {
                        if (!res.ok){ 
                            if(res.status === 401){
                                sessionStorage.clear();
                                alert('Sesija je istekla.')
                                navigate('/');
                                return;
                            }
                            throw new Error("Neuspešno preuzimanje profila sportiste.");
                        }
                        return res.json();
                    })
                    .then((data) => {
                        setKorisnik({
                            ...data,
                            id: data.idKorisnika
                        });
                    })
                    .catch((err) => console.error("Greška pri sinhronizaciji: ", err));
            } catch (e) {
                console.error("Greška pri čitanju iz sessionStorage-a ", e);
            }
        }
    };

    useEffect(() => {
        ucitajProfil();
        
    }, []);

    useEffect(() => {
        if(korisnik?.idKluba){
            ucitajKlub(korisnik?.idKluba)
        }
        
    }, [korisnik])


    return(
        <div class='klub-page'>
            <NavBarSportista korisnik={korisnik}/>
            {klub ? (
                <div class='klub-container'>
                    <div class='profil-card'>
                        <div class='klub-header-card'>
                            <h2>{klub?.naziv}</h2>
                        </div>
                        <div class='profil-sekcija'>
                            <h3>Podaci o klubu</h3>
                            <div class='info'>
                                <div class='info-item'>
                                    <label>ID kluba</label>
                                    <p>{klub?.idKluba}</p>
                                </div>
                                <div class='info-item'>
                                    <label>PIB</label>
                                    <p>{klub?.pib}</p>
                                </div>
                                <div class='info-item'>
                                    <label>Email</label>
                                    <p>{klub?.email || 'Ne postoji'}</p>
                                </div>
                                <div class='info-item'>
                                    <label>Kontakt</label>
                                    <p>{klub?.kontakt || 'Ne postoji'}</p>
                                </div>
                                <div class='info-item'>
                                    <label>Sedište kluba</label>
                                    <p>{klub?.mesto?.naziv}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ) : (<div class='nema-klub'>Sportista nema klub</div>)}
            
        </div>
    )
}