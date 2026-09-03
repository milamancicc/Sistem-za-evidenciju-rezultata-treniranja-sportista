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

    const [klubovi, setKlubovi] = useState([]);

    const ucitajSveKlubove = async () => {
        try {
            const res = await fetch(`http://localhost:8080/api/klubovi`, {
                headers: getAuthHeaders()
            });
            if (res.ok) {
                const data = await res.json();
                setKlubovi(data);
            }else{
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
            }
        } catch (err) {
            console.error("Greška pri učitavanju klubova: ", err);
        }
    };

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

    const ucitajNazivMesta = async (id) => {
        if(!id) return;
        try{
            const res = await fetch(`http://localhost:8080/api/mesta/${id}`, {
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
            setMesto(data.naziv);
        }catch(err){
            console.error('Greska: ', err);
            
        }
    }

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
            setKlub(data.naziv);
        }catch(err){
            console.error('Greska: ', err);
            
        }
    }

    const ucitajProfil = async () => {
        const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
        if(!sacuvaniKorisnik)
            return;
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
                        const aktivanKorisnik = {
                            ...data,
                            id: data.idKorisnika
                        };
                        setKorisnik(aktivanKorisnik);
                        setFormaData({
                            kontakt: '',
                            tezina: '',
                            visina: '',
                            idKluba: ''
                        });
                    })
                    .catch((err) => console.error("Greška pri sinhronizaciji: ", err));
            } catch (e) {
                console.error("Greška pri čitanju iz sessionStorage-a ", e);
            }
        }
    };

    const handleSacuvajIzmene = async (e) => {
        e.preventDefault();
        try {
            const id = JSON.parse(sessionStorage.getItem('korisnik')).id;
            const payload = {
                ...korisnik,
                kontakt: formaData.kontakt,
                tezina: formaData.tezina ? parseFloat(formaData.tezina) : null,
                visina: formaData.visina ? parseFloat(formaData.visina) : null,
                idKluba: formaData.idKluba ? parseInt(formaData.idKluba) : null
            };
            
            const res = await fetch(`http://localhost:8080/api/sportisti/${id}`, {
                method: 'PUT',
                headers: getAuthHeaders(),
                body: JSON.stringify(payload)
            });
            if(!res.ok) {
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                const poruka = await res.text();
                throw new Error(poruka || "Greška prilikom izmene sportiste.");
            }
            const azuriraniSportista = await res.json();
            setKorisnik({
                ...azuriraniSportista,
                id: azuriraniSportista.idKorisnika
            });
            setPrikaziModal(false);
            } catch (err) {
            console.error(err);
            alert("Došlo je do greške: " + err.message);
        }
    };

    useEffect(() => {
        ucitajProfil();
        ucitajSveKlubove();
    }, []);


    useEffect(() => {
        if(korisnik?.idMestoPorekla){
            ucitajNazivMesta(korisnik.idMestoPorekla)
        }
        if(korisnik?.idKluba){
            ucitajKlub(korisnik?.idKluba)
        }
    }, [korisnik])
    

    const [prikaziModal, setPrikaziModal] = useState(false);
    const [formaData, setFormaData] =useState({
        kontakt: korisnik?.kontakt || '',
        tezina: korisnik?.tezina || '',
        visina: korisnik?.visina || '',
        idKluba: korisnik?.idKluba || ''
    })


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
                                <p>{JSON.parse(sessionStorage.getItem('korisnik'))?.id}</p>
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
                                <p>{klub ? klub : 'Nije unet'}</p>
                            </div>
                            <div class='info-item'>
                                <button class='izmeni' onClick={() => {
                                    console.log(korisnik);
                                    
                                    setFormaData({
                                        kontakt: korisnik?.kontakt || '',
                                        tezina: korisnik?.tezina || '',
                                        visina: korisnik?.visina || '',
                                        idKluba: korisnik?.idKluba || ''
                                    })
                                    setPrikaziModal(true)}}>✏️</button>
                            </div>
                        </div>
                    </div>
                </div>

                {prikaziModal && (
                    <form class='modal-form' onSubmit={handleSacuvajIzmene}>
                        <div class='modal-content'>
                            <h3>Izmeni informacije</h3>
                            <div>
                                <label>Kontakt telefon: </label>
                                <input type='text' value={formaData.kontakt} onChange={(e) => setFormaData({...formaData, kontakt:e.target.value})}/>
                            </div>
                            <div>
                                <label>Težina (kg): </label><br/>
                                <input type='number' step='0.1' min='0' value={formaData.tezina} onChange={(e) => setFormaData({...formaData, tezina: e.target.value})}/>
                            </div>
                            <div>
                                <label>Visina (cm): </label><br/>
                                <input type='number' step='0.1' min='0' value={formaData.visina} onChange={(e) => setFormaData({...formaData, visina:e.target.value})}/>
                            </div>
                            <div>
                                <label>Klub: </label>
                                <select 
                                    value={formaData.idKluba} 
                                    onChange={(e) => setFormaData({...formaData, idKluba: e.target.value})}
                                >
                                    <option value="">Izaberi klub</option>
                                    {klubovi.map((k) => (
                                        <option key={k.idKluba} value={k.idKluba}>
                                            {k.naziv}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div>
                                <button type='submit'>💾Sačuvaj</button>
                                <button type='button' onClick={() => {
                                    setPrikaziModal(false);
                                    
                                }}>❌Otkaži</button>
                            </div>
                        </div>
                    </form>
                )}
            </main>
        </div>
    )
}
