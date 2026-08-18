import React, { useState, useEffect } from "react";
import NavBarTrener from "../components/NavBarTrener";
import './TrenerProfil.css';

export default function TrenerProfil() {
    const [korisnik, setKorisnik] = useState(null);

    const [prikaziModal, setPrikaziModal] = useState(false);
    const [sveSpecijalizacije, setSveSpecijalizacije] = useState([]);
    const [nacinUnosa, setNacinUnosa] = useState("postojeca");

    const [selectedSpecId, setSelectedSpecId] = useState("");
    const [noviNazivSpec, setNoviNazivSpec] = useState("");
    const [godinaPostizanja, setGodinaPostizanja] = useState(new Date().getFullYear());
    const [opisSpecijalizacije, setOpisSpecijalizacije] = useState("");
    const[poruka, setPoruka] = useState("");


    const ucitajProfil = () => {
        const sacuvaniKorisnik = localStorage.getItem('korisnik');
        if(sacuvaniKorisnik) {
            try{
                const parsed = JSON.parse(sacuvaniKorisnik);
                const id = parsed.id;
                setKorisnik(parsed);
                if(!id)
                    return;
                fetch(`http://localhost:8080/api/treneri/${id}`)
                    .then((res) => {
                        if(!res.ok)
                            throw new Error("Neuspešno preuzimanje profila.");
                        return res.json();
                            
                    })
                    .then((data) => {
                        setKorisnik({
                            ...data,
                            id:data.idKorisnika
                        });
                    })
                    .catch((err) => console.error("Greška pri sinhronizaciji: ", err));
            }catch(e){
                console.error("Greška pri čitanju iz localStorage ", e);
            }
        }
    };

    useEffect(() => {
        ucitajProfil();
    }, []);

    const otvoriModal = () => {
        setPoruka("");
        setPrikaziModal(true);
        fetch('http://localhost:8080/api/specijalizacije')
            .then((res) => (res.ok ? res.json() : []))
            .then((data) => setSveSpecijalizacije(data))
            .catch((err) => console.error('Greška pri učitavanju liste specijalizacija: ', err));

    };

    const zatvoriModal = () => {
        setPrikaziModal(false);
        setSelectedSpecId("");
        setNoviNazivSpec("");
        setOpisSpecijalizacije("");
        setGodinaPostizanja(new Date().getFullYear());
        setNacinUnosa("postojeca");
    };

    const handleDodajSpecijalizaciju = async (e) => {
        e.preventDefault();
        setPoruka("");
        const sacuvaniKorisnik = JSON.parse(localStorage.getItem('korisnik'));
        const trenerId = korisnik?.idKorisnika || sacuvaniKorisnik.id;
        if(!trenerId){
            console.error("Nedostaje ID trenera.");
            return;
        }
        let specId = selectedSpecId;
        try{
            if(nacinUnosa === "nova"){
                if(!noviNazivSpec){
                    setPoruka("Naziv nove specijalizacije je obavezan.");
                    return;
                }
                const resSpec = await fetch("http://localhost:8080/api/specijalizacije", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify({naziv:noviNazivSpec, opis: opisSpecijalizacije})
                });
                if(!resSpec.ok)
                    throw new Error("Sistem ne može da sačuva novu specijalizaciju.");
                const sacuvanaSpec = await resSpec.json();
                specId = sacuvanaSpec.idSpecijalizacije;
                    
            }
            if(!specId){
                setPoruka("Izaberite validnu specijalizaciju.");
                return;
            }
            const dto = {
                idTrenera: Number(trenerId),
                idSpecijalizacije: Number(specId),
                godinaPostizanja: Number(godinaPostizanja),
                opisSpecijalizacije: opisSpecijalizacije
            };
            const resSP = await fetch("http://localhost:8080/api/treneri/specijalizacije", {
                method: 'POST',
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dto)
            });
            if(!resSP.ok){
                const porukaSaServera = await resSP.text();
                throw new Error(porukaSaServera || "Sistem ne moze da sacuva specijalisticki podatak.");
                
            }
            zatvoriModal();
            ucitajProfil();
        }catch(err){
            setPoruka(err.message || "Došlo je do greške.");
        }
    };

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
                                <p>@{JSON.parse(localStorage.getItem('korisnik')).korisnickoIme}</p>
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
                                <p>{JSON.parse(localStorage.getItem('korisnik')).id}</p>
                            </div>
                        </div>
                    </div>

                    
                    <div class='profil-sekcija'>
                        <h3>Stečene specijalizacije</h3>
                        {korisnik?.specijalistickiPodaci && korisnik.specijalistickiPodaci.length > 0 ? (
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
                            ) : (<p>Nemate unetih specijalizacija.</p>)}
                            <div class='dodaj-spec'>
                                <button class='dodaj-btn' onClick={otvoriModal}>
                                    📜Dodaj specijalizaciju
                                </button>
                            </div>
                        </div>
                </div>
            </main>
            
            {prikaziModal && (
                <div class='modal-container'>
                    <div class='modal-content'>
                        <div class='modal-header'>
                            <h3>Dodavanje specijalizacije</h3>
                            <button class='btn-zatvori' onClick={zatvoriModal}>❌</button>
                        </div>
                        {poruka && <div class='poruka-error'>{poruka}</div>}
                        <form onSubmit={handleDodajSpecijalizaciju}>
                            <div class='form-group nacin-unosa'>
                                <label>
                                    <input type='radio' name='nacinUnosa' value='postojeca' checked={nacinUnosa === 'postojeca'} onChange = {() => setNacinUnosa('postojeca')}/>
                                    Izaberi postojeću
                                </label>
                                <label>
                                    <input type='radio' name='nacinUnosa' value='nova' checked={nacinUnosa === 'nova'} onChange = {() => setNacinUnosa('nova')}/>
                                    Kreiraj novu
                                </label>
                            </div>
                            {nacinUnosa === 'postojeca' ? (
                                <div class='form-group'>
                                    <label>Izaberite specijalizaciju</label>
                                    <select value={selectedSpecId} onChange={(e) => setSelectedSpecId(e.target.value)} required>
                                        <option value="">Izaberite iz liste</option>
                                        {sveSpecijalizacije.map((s)=>(
                                            <option key={s.idSpecijalizacije} value={s.idSpecijalizacije}>{s.naziv}</option>
                                        ))}
                                    </select>

                                </div>
                            ) : (
                                <div class='form-group'>
                                    <label>Naziv nove specijalizacije</label>
                                    <input type='text' placeholder='Unesite naziv specijalizacije' value={noviNazivSpec} onChange={(e) => setNoviNazivSpec(e.target.value)} required/>
                                    
                                </div>
                            )}
                            <div class = 'form-group'>
                                <label>Godina sticanja specijalizacije</label>
                                <input type='number' min='1950' max={new Date().getFullYear()} value={godinaPostizanja} onChange={(e) => setGodinaPostizanja(e.target.value)} required/>
                            </div>

                            {nacinUnosa === 'nova' &&
                                <div class='form-group'>
                                    <label>Opise specijalizacije</label>
                                    <textarea rows='3' placeholder='Kratak opis specijalizacije' value={opisSpecijalizacije} onChange={(e) => setOpisSpecijalizacije(e.target.value)}/>
                                </div>
                            }

                            <div class='modal-btns'>
                                <button type='button' class='btn-otkazi' onClick={() => {
                                    setSelectedSpecId("");
                                    setNoviNazivSpec("");
                                    setOpisSpecijalizacije("");
                                    setGodinaPostizanja(new Date().getFullYear());
                                    setNacinUnosa("");
                                    setPoruka("");
                                }}>Otkaži</button>
                                <button type='submit' class='btn-sacuvaj'>Sačuvaj</button>
                            </div>
                            
                        </form>
                    </div>
                </div>
            )}
            
        </div>
    )

}