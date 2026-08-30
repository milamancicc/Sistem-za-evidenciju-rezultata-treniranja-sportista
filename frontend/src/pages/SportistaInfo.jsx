import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import NavBarSportista from "../components/NavBarSportista";
import './SportistaInfo.css'

export default function SportistaInfo() {
    const navigate = useNavigate();
    const [korisnik, setKorisnik] = useState(null);
    const [detalji, setDetalji] = useState(null);
    const [greska, setGreska] = useState('');
    const [mesto, setMesto] = useState('');
    const [klub, setKlub] = useState('');

    const ucitajNazivMesta = async (id) => {
        if(!id) return;
        try{
            const res = await fetch(`http://localhost:8080/api/mesta/${id}`);
            if(!res.ok){
                throw new Error("Greska pri preuzimanju imena mesta");
            }
            const data = await res.json();
            setMesto(data.naziv);
        }catch(err){
            console.error('Greska: ', err);
            
        }
    }

    const ucitajKlub = async (id) => {
        if(!id) return;
        try{
            const res = await fetch(`http://localhost:8080/api/klubovi/${id}`);
            if(!res.ok){
                throw new Error("Greska pri preuzimanju imena mesta");
            }
            const data = await res.json();
            setKlub(data.naziv);
        }catch(err){
            console.error('Greska: ', err);
            
        }
    }

    const ucitajProfil = async () => {
        const sacuvaniKorisnik = localStorage.getItem('korisnik');
        if(!sacuvaniKorisnik)
            return;
        if (sacuvaniKorisnik) {
            try {
                const parsed = JSON.parse(sacuvaniKorisnik);
                const id = parsed.id;
                setKorisnik(parsed);
                if (!id) return;

                fetch(`http://localhost:8080/api/sportisti/${id}`)
                    .then((res) => {
                        if (!res.ok) throw new Error("Neuspešno preuzimanje profila sportiste.");
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
                console.error("Greška pri čitanju iz localStorage ", e);
            }
        }
    };

    useEffect(() => {
        ucitajProfil();
        
    }, []);


    useEffect(() => {
        if(korisnik?.idMestoPorekla){
            ucitajNazivMesta(korisnik.idMestoPorekla)
        }
        if(korisnik?.idKluba){
            ucitajKlub(korisnik?.idKluba)
        }
    }, [korisnik])


    const podaci = detalji || korisnik;
    return(
        <div class='sportista-profil-container'>
            <NavBarSportista korisnik={korisnik}/>
            <main class='sportista-profil-content'>
                <div class='profil-card'>
                    <div class='profil-header'>
                        <h2>{korisnik?.ime} {korisnik?.prezime}</h2>
                        <span class='tip-korisnika-sp'>{korisnik?.tipKorisnika}</span>
                    </div>
                    <div class='profil-sekcija'>
                        <h3>Osnovne i kontakt informacije</h3>
                        <div class='info'>
                            <div class='info-item'>
                                <label>Korisničko ime</label>
                                <p>@{korisnik?.korisnickoIme}</p>
                            </div>
                            <div class='info-item'>
                                <label>Email adresa</label>
                                <p>{korisnik?.email}</p>
                            </div>
                            <div class='info-item'>
                                <label>Kontakt telefon</label>
                                <p>{korisnik?.kontakt}</p>
                            </div>
                            <div class='info-item'>
                                <label>ID korisnika</label>
                                <p>{JSON.parse(localStorage.getItem('korisnik'))?.id}</p>
                            </div>
                            <div class='info-item'>
                                <label>Datum rođenja</label>
                                <p>{korisnik?.datumRodjenja}</p>
                            </div>
                            <div class='info-item'>
                                <label>Pol</label>
                                <p>{korisnik?.pol ? (korisnik?.pol==='MUSKI' ? 'Muški' : 'Ženski') : 'Nije unet'}</p>
                            </div>
                            <div class='info-item'>
                                <label>Starosna kategorija</label>
                                <p>{korisnik?.starosnaKategorija}</p>
                            </div>
                            <div class='info-item'>
                                <label>Težina</label>
                                <p>{korisnik?.tezina ? `${korisnik.tezina}kg` : 'Nije uneta'}</p>
                            </div>
                            <div class='info-item'>
                                <label>Visina</label>
                                <p>{korisnik?.visina ? `${korisnik.visina}cm` : 'Nije uneta'}</p>
                            </div>
                            <div class='info-item'>
                                <label>Mesto porekla</label>
                                <p>{mesto}</p>
                            </div>
                            <div class='info-item'>
                                <label>Klub</label>
                                <p>{klub}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    )
}
