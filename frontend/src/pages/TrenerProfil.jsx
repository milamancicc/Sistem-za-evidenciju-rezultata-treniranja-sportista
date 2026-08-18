import React, { useState, useEffect } from "react";
import NavBarTrener from "../components/NavBarTrener";
import './TrenerProfil.css';

export default function TrenerProfil() {
    const [korisnik, setKorisnik] = useState(null);

    useEffect(() => {
        const sacuvaniKorisnik = localStorage.getItem('korisnik');
        if(sacuvaniKorisnik){
            try{
                const parsed = JSON.parse(sacuvaniKorisnik);
                const id = parsed.id;
                setKorisnik(parsed);
                fetch(`http://localhost:8080/api/treneri/${id}`)
                    .then((res) => {
                        if(!res.ok)
                            throw new Error("Neuspešno preuzimanje podataka.");
                        return res.json();
                    })
                    .then((data) => {
                        const proveraKorisnickogImena = data.korisnickoIme !== null && data.korisnickoIme !== undefined ? data.korisnickoIme : parsed.korisnickoIme;
                        
                        setKorisnik({...data, korisnickoIme: proveraKorisnickogImena});
                    })
                    .catch((err) => {
                        console.error("Greška pri sinhronizaciji profila sa serverom.", err);
                        setKorisnik(parsed);
                    });
            }catch(e){
                console.error("Greška pri učitavanju korisnika iz skladišta.", e);
            }
        }
    }, []);

    return(
        <div class='profil-container'>
            <NavBarTrener korisnik={korisnik}/>
            <main class='profil-content'>
                <div class='profil-card'>
                    <div class='profil-header'>
                        <h2>{korisnik?.ime} {korisnik?.prezime}</h2>
                        <span class='tip-korisnika'>{korisnik?.tipKorisnika}</span>
                    </div>
                    <div class='profil-sekcija'>
                        <h3>Osnovne i kontakt informacije</h3>
                        <div class = 'info'>
                            <div class='info-item'>
                                <label>Korisničko ime</label>
                                <p>{korisnik?.korisnickoIme}</p>
                            </div>
                            <div class='info-item'>
                                <label>Email adresa</label>
                                <p>{korisnik?.email}</p>
                            </div>
                            <div class='info-item'>
                                <label>Kontakt telefon</label>
                                <p>{korisnik?.kontakt || 'Nije unet'}</p>
                            </div>
                            <div class='info-item'>
                                <label>ID korisnika</label>
                                <p>{korisnik?.idKorisnika}</p>
                            </div>
                        </div>
                    </div>
                    {korisnik?.specijalistickiPodaci && korisnik.specijalistickiPodaci.length > 0 &&(
                        <div class='profil-sekcija'>
                            <h3>Stečene specijalizacije</h3>
                            <div class='specijalizacije-cards'>
                                {korisnik.specijalistickiPodaci.map((spec,index) => (
                                    <div key={spec.idSpecijalizacije} class='specijalizacija-card'>
                                        <div class='spec-card-header'>
                                            <h4>{spec.nazivSpecijalizacije}</h4>
                                            {spec.godinaPostizanja && (
                                                <span class='godina'>
                                                    {spec.godinaPostizanja}. god
                                                </span>
                                            )}
                                        </div>
                                        <p class='spec-opis'>
                                            {spec.opisSpecijalizacije || 'Nema opisa za ovu specijalizaciju.'}
                                        </p>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                    
                </div>
            </main>
        </div>
    )

}