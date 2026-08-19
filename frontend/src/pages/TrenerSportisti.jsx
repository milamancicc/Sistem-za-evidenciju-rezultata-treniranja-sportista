import React, { useState, useEffect } from "react";
import NavBarTrener from "../components/NavBarTrener";
import './TrenerSportisti.css';

export default function TrenerSportisti() {
    
    const [korisnik, setKorisnik] = useState(null);
    const [idKorisnika, setIdKorisnika] = useState("");
    const[sportisti, setSportisti] = useState([]);
    const [poruka, setPoruka] = useState("");

    const[ime, setIme] = useState("");
    const[prezime, setPrezime] = useState('');
    const [korisnickoIme, setKorisnickoIme] = useState('');
    const [sifra, setSifra] = useState('');
    const [email, setEmail] = useState('');
    const [kontakt, setKontakt] = useState('');
    const [tipKorisnika, setTipKorisnika] = useState('SPORTISTA');
    const [pol, setPol] = useState("MUSKI");
    const [tezina, setTezina] = useState("");
    const [visina, setVisina] = useState("");
    const [klub, setKlub] = useState("");
    const [mestoPorekla, setMestoPorekla] = useState("");
    const [datumRodjenja, setDatumRodjenja] = useState('');


    const [prikaziModal, setPrikaziModal] = useState(false);

    const [klubovi, setKlubovi] = useState([]);
    const [mesta, setMesta] = useState([]);


    const ucitajSportiste = async (trenerId) => {
        try{
            const res = await fetch(`http://localhost:8080/api/sportisti`);
            if(!res.ok){
                throw new Error("Greška pri komunikaciji sa serverom.");
                
            }
            const data = await res.json();
            setSportisti(data);
            
        }catch(err){
            setPoruka("Nije moguće učitati listu sportista.");
        }
    }

    const ucitajKlubove = async () => {
        try{
            const res = await fetch('http://localhost:8080/api/klubovi');
            if(res.ok){
                const data = await res.json();
                setKlubovi(data);
            }
        }catch(err){
            console.error(err);
            
        }
    }

    const ucitajMesta = async () => {
        try{
            const res = await fetch('http://localhost:8080/api/mesta');
            if(res.ok){
                const data = await res.json();
                setMesta(data);
            }
        }catch(err){
            console.error(err);
            
        }
    }

    const getNazivKluba = (id) => {
        const nadjeniKlub = klubovi.find((k) => k.idKluba === Number(id));
        return nadjeniKlub ? nadjeniKlub.naziv : id;
    }

    const getNazivMesta = (id) => {
        const nadjenoMesto = mesta.find((m) => m.idMesta === Number(id));
        return nadjenoMesto ? nadjenoMesto.naziv : id;
    }

    useEffect(() => {
        const sacuvani = localStorage.getItem('korisnik');
        if(!sacuvani){
            console.alert('Nema sacuvanih podataka u localStorage-u');
            return;
        }

        try{
            const parsed = JSON.parse(sacuvani);
            
            setKorisnik(parsed);
            const id = parsed.id;
            if(id !== null && id !== undefined){
                setIdKorisnika(id);
            }else{
                console.alert('Korisnik nema definisan id.');
            }
            ucitajSportiste();
            ucitajMesta();
            ucitajKlubove();
            
        }catch(e){

        }
        
    }, []);

    const imaDovoljnoGodina = (datumStr) => {
        if(!datumStr) 
            return false;
        const rodjenje = new Date(datumStr);
        const danas = new Date();

        let godine = danas.getFullYear() - rodjenje.getFullYear();
        
        return godine >= 11;

    }

    const handleObrisiSportistu = async (id) => {
        try{
            const res = await fetch(`http://localhost:8080/api/sportisti/${id}`, {
                method: 'DELETE',
            });
            if(res.ok){
                ucitajSportiste();
                ucitajProfil();
            }else{
                alert('Došlo je do greške pri brisanju sa servera.');
            }
        }catch(err){
            console.error(err);
            
        }
    }

    const handleDodajSportistu = async (e) => {
        e.preventDefault();
        setPoruka('');
        console.log(imaDovoljnoGodina(datumRodjenja));
        
        if(!imaDovoljnoGodina(datumRodjenja)){
            setPoruka('Sportista mora imati najmanje 11 godina za registraciju.');
            return;
        }

        const postojiKorisnickoIme = sportisti.some((s) => s.korisnickoIme === korisnickoIme);

        if(postojiKorisnickoIme){
            setPoruka("Korisničko ime je već zauzeto. Izaberite drugo");
            return;
        }

        const postojiEmail = sportisti.some((s) => s.email === email);

        if(postojiEmail){
            setPoruka('Nalog sa ovim email-om već postoji');
            return;
        }

        const noviSportista = {
            ime,
            prezime,
            korisnickoIme,
            sifra,
            email,
            kontakt,
            datumRodjenja,
            tipKorisnika: 'SPORTISTA',
            pol,
            tezina: tezina ? Number(tezina) : null,
            visina: visina? Number(visina) : null,
            idKluba: klub,
            idMestoPorekla: mestoPorekla
        };
        try{
            const res = await fetch('http://localhost:8080/api/sportisti', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(noviSportista)
            });
            if(!res.ok){
                const greska = await res.text();
                throw new Error(greska);
                
            }
            setPoruka('Sportista uspešno kreiran.');
            zatvoriModal();
            ucitajSportiste();
        }catch(err){
            setPoruka(err.message);
        }
    }

    const otvoriModal = () => {
        setPoruka('');
        setEmail('');
        setSifra('');
        setPrikaziModal(true);
    }

    const zatvoriModal = () => {
        setPrikaziModal(false);
        setIme('');
        setPrezime('');
        setKorisnickoIme('');
        setSifra('');
        setEmail('');
        setKontakt('');
        setDatumRodjenja('');
        setPol('MUSKI');
        setTezina('');
        setVisina('');
        setKlub('');
        setMestoPorekla('');
        setPoruka('');
    };

    return(
        <div class='sportisti-page'>
            <NavBarTrener korisnik={korisnik}/>
            <main class = 'trener-sportisti-container'>
                <div class='header'>
                    <h2>Sportisti u sistemu</h2>
                    {korisnik && (
                        <p class='subtitle'>
                            Pregled svih sportista i dodavanje novih
                        </p>
                    )}
                    <button class='btn-dodaj' onClick={otvoriModal}>➕Dodaj novog sportistu</button>
                </div>

                
                {sportisti.length > 0 ? (
                    <div class = 'sportisti'>
                        {sportisti.map((sportista) => (
                            <div  key={sportista.id} class='sportista-card'>
                                <div>
                                    <h3>{sportista.ime} {sportista.prezime}</h3>
                                    <span class='username'>@{sportista.korisnickoIme}</span>
                                </div>
                                <div class = 'sportista-detalji'>
                                    <p><strong>Klub:</strong> {getNazivKluba(sportista.idKluba)}</p>
                                    <p><strong>Mesto porekla:</strong> {getNazivMesta(sportista.idMestoPorekla)}</p>
                                    <p><strong>Pol:</strong> {sportista.pol === 'MUSKI' ? 'Muški' : (sportista.pol === 'ZENSKI' ? 'Ženski' : 'Nedefinisan')}</p>
                                    <p><strong>Visina:</strong> {sportista.visina || 'Nije uneta'}</p>
                                    <p><strong>Težina:</strong> {sportista.tezina || 'Nije uneta'}</p>
                                    <p><strong>Email:</strong> {sportista.email}</p>
                                    <p><strong>Telefon:</strong> {sportista.kontakt || 'Nije unet'}</p>
                                    <p><strong>Starosna kategorija:</strong> {sportista.starosnaKategorija || 'Nije unet'}</p>
                                    
                                </div>
                                <div class='sportista-card-btn'>
                                    <button class='btn-obrisi' onClick={() => handleObrisiSportistu(sportista.id)} title='Obriši sportistu'>🗑️</button>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div class='nema-sportista'>Trenutno nema unetih sportista.</div>
                )}
            </main>


            {prikaziModal && (
                <div class='modal-dodaj-sportistu'>
                    <div class='modal-card'>
                        <div class='modal-header'>
                            <h3>Dodaj novog sportistu</h3>
                            <button class = 'btn-zatvori' onClick={zatvoriModal}>❌</button>
                        </div>
                        {poruka && (
                            <div class='poruka'>
                                {poruka}
                            </div>
                        )}

                        <form onSubmit={handleDodajSportistu} class='form' autoComplete='off'>
                            <div class='form-group'>
                                <label>Ime</label>
                                <input type='text' value={ime} onChange={(e) => setIme(e.target.value)} required/>
                            </div>
                            <div class='form-group'>
                                <label>Prezime</label>
                                <input type='text' value={prezime} onChange={(e) => setPrezime(e.target.value)} required/>
                            </div>
                            <div class='form-group'>
                                <label>Korisničko ime</label>
                                <input type='text' value={korisnickoIme} onChange={(e) => setKorisnickoIme(e.target.value)} required/>
                            </div>
                            <div class='form-group'>
                                <label>Šifra</label>
                                <input type='password' value={sifra} onChange={(e) => setSifra(e.target.value)} autoComplete="new-password" required/>
                            </div>
                            <div class='form-group'>
                                <label>Datum rođenja</label>
                                <input type='date' value={datumRodjenja} onChange={(e) => setDatumRodjenja(e.target.value)} required/>
                            </div>
                            <div class='form-group'>
                                <label>Klub</label>
                                <select value={klub} onChange={(e) => setKlub(e.target.value)} required>
                                    <option value="">Izaberi klub</option>
                                    {klubovi.map((k) => (
                                        <option key={k.idKluba} value={k.idKluba}>{k.naziv}</option>
                                    ))}
                                </select>
                            </div>
                            <div class='form-group'>
                                <label>Mesto porekla</label>
                                <select value={mestoPorekla} onChange={(e) => setMestoPorekla(e.target.value)} required>
                                    <option value="">Izaberi mesto</option>
                                    {mesta.map((m) => (
                                        <option key={m.idMesta} value={m.idMesta}>
                                            {m.naziv}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div class='form-group'>
                                <label>Visina(cm)</label>
                                <input type='number' step='0.1' value={visina} onChange={(e) => setVisina(e.target.value)}/>
                            </div>
                            <div class='form-group'>
                                <label>Težina(kg)</label>
                                <input type='number' step='0.1' value={tezina} onChange={(e) => setTezina(e.target.value)}/>
                            </div>
                            <div class='form-group'>
                                <label>Pol</label>
                                <select value={pol} onChange={(e) => setPol(e.target.value)}>
                                    <option value='MUSKI'>Muški</option>
                                    <option value='ZENSKI'>Ženski</option>
                                </select>
                            </div>
                            <div class='form-group'>
                                <label>Email</label>
                                <input type='email' value={email} onChange={(e) => setEmail(e.target.value)}/>
                            </div>
                            <div class='form-group'>
                                <label>Kontakt telefon</label>
                                <input type='text' value={kontakt} onChange={(e) => setKontakt(e.target.value)}/>
                            </div>

                            <div class='modal-btns'>
                                <button type='button' class='btn-otkazi' onClick={() => {
                                    setIme('');
                                    setPrezime('');
                                    setKorisnickoIme('');
                                    setSifra('');
                                    setEmail('');
                                    setKontakt('');
                                    setDatumRodjenja('');
                                    setPol('MUSKI');
                                    setTezina('');
                                    setVisina('');
                                    setKlub('');
                                    setMestoPorekla('');
                                    setPoruka('');
                                }}>
                                    Otkaži
                                </button>
                                <button type='submit' class='btn-sacuvaj'>
                                    Sačuvaj
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
            
        </div>
    )

}