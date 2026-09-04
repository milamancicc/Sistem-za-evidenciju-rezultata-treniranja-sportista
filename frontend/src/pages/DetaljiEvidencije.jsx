import { useState, useEffect } from "react";
import {useParams, useNavigate} from 'react-router-dom';
import NavBarTrener from '../components/NavBarTrener';
import moment from "moment";
import './DetaljiEvidencije.css';

export default function DetaljiEvidencije(){

    const {id} = useParams();
    const navigate = useNavigate();

    const [evidencija, setEvidencija] = useState(null);
    const [korisnik, setKorisnik] = useState(null);
    const [greska, setGreska] = useState('');

    const [stavke, setStavke] = useState([]);

    const [stavka, setStavka] = useState(null);

    const [vezbe, setVezbe] = useState([]);

    const [novaStavka, setNovaStavka] = useState({
        vezbaId:'',
        ostvareniRezultat:'',
        komentar:''
    });
    const [prikaiModalDodaj, setPrikaziModalDodaj] = useState(false);

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };
    
    useEffect(() => {
        const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
        if(sacuvaniKorisnik){
            try{
                setKorisnik(JSON.parse(sacuvaniKorisnik));
            }catch(e){
                console.error("Greska pri citanju korisnika: ", e);
                
            }
        }
        fetchEvidencijaDetalji();
        fetchVezbe();
        
    }, [id]);



    const fetchVezbe = async ()=> {
        try{
            const res = await fetch(`http://localhost:8080/api/vezbe`, {
                headers: getAuthHeaders(),
            });
            if(!res.ok){
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                throw new Error("Greska pri ucitavanju vezbi");
                
            }
            const data = await res.json();
            setVezbe(data);
                
        }catch(err){
            console.error("Greska pri preuzimanju vezbi: ", err);
            
        }
    }

    const obrisiStavku = async (rb) => {
        try{
            const res = await fetch(`http://localhost:8080/api/evidencije/${id}/stavke/${rb}`,{
                method: 'DELETE',
                headers: getAuthHeaders()
            });
            if(!res.ok){
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                throw new Error(await res.text());
                
            }
            fetchEvidencijaDetalji();
        }catch(err){
            console.error("Greška pri brisanju stavke: ", err);
            
        }
    }

    const fetchEvidencijaDetalji = async () => {
        try{
            const res = await fetch(`http://localhost:8080/api/evidencije/${id}`, {
                headers: getAuthHeaders()
            });
            
            
            if(!res.ok){
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                const greskaSaServera = await res.text();
                throw new Error(greskaSaServera);
                
                
            }
            const data = await res.json();
            setEvidencija(data);
            
            setStavke(data.stavke);
        }catch(err){
            console.error(err);
            setGreska(err.message);
        }
    };

    const izmeniStavku = async (e) => {
        e.preventDefault();
        try{
            const payload ={
                vezbaId: stavka.vezbaId,
                ostvareniRezultat: stavka.ostvareniRezultat === '' ? null : parseFloat(stavka.ostvareniRezultat),
                komentar: stavka.komentar
            }
            
            const res = await fetch(`http://localhost:8080/api/evidencije/${id}/stavke/${stavka.rb}`, {
                method: 'PUT',
                headers: getAuthHeaders(),
                body: JSON.stringify(payload)
            });
            if(!res.ok){
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                const greska = await res.text();
                throw new Error(greska);
                
            }
            const reloaded = await res.json();
            setEvidencija(reloaded);
            setStavke(reloaded.stavke);
            setStavka(null);
            fetchEvidencijaDetalji();
        }catch(err){
            alert(err)
            console.error("Greška pri izmeni stavke: ", err.message);
            
        }
    }

    const dodajStavku = async (e) => {
        e.preventDefault();
        try{
            const res = await fetch(`http://localhost:8080/api/evidencije/${id}/stavke`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(novaStavka)
            });
             if(!res.ok){
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                const greska = await res.text();
                throw new Error(greska);
                
             }
             const reloaded = await res.json();
             setNovaStavka({vezbaId:'', ostvareniRezultat:'', komentar:''});
             setPrikaziModalDodaj(false);
             setEvidencija(reloaded);
             setStavke(reloaded.stavke)
            //  fetchEvidencijaDetalji();
        }catch(err){
            alert(err)
            console.error("Greška pri izmeni stavke: ", err.message);
        }
    }

    

    if(greska || !evidencija){
        return(
            <div class='greska-container'>
                <p>{greska || 'Evidencija nije pronađena.'}</p>
                <button onClick={() => navigate('/trener-main')}>Nazad na glavnu stranu</button>
            </div>
        )
    }

    return(
        <div class='evidencija-container'>
            <NavBarTrener korisnik={korisnik}/>
            <main class='evidencija-content'>
                <h2><p onClick={() => navigate('/trener-main')}>⬅</p>Detalji evidencije testiranja #{evidencija.idTestiranja}</h2>

                <div class='info-card'>
                    <h3>Opšte informacije</h3>
                    <div class='info-grid'>
                        <p><strong>Sportista:</strong> {evidencija.imeIPrezimeSportiste}</p>
                        <p><strong>Datum testiranja:</strong> {moment(evidencija.datum).format('DD. MMMM YYYY')}</p>
                        <p><strong>Ukupan broj testova:</strong> {evidencija.brojTestova}</p>
                        <p><strong>Broj položenih testova:</strong> {evidencija.brojPolozenih}</p>
                        <p><strong>Broj palih testova:</strong> {evidencija.brojPalih}</p>
                        <p><strong>{evidencija.prosaoTestiranje ? 'Testiranje položeno✅' : 'Testiranje nije položeno❌'}</strong></p>
                        <p><strong>Konačan rezultat:</strong> {evidencija.rezultatTestiranja}%</p>
                    </div>
                </div>

                <div class='stavke-section'>
                    <h3>Stavke testiranja</h3>
                    {( stavke?.length === 0) ? (
                        <p class='nema-stavki'>Nema unetih stavki za ovo testiranje.<button class='stavka-card dodaj-stavku' onClick={() => setPrikaziModalDodaj(true)}>➕Dodaj stavku</button></p>
                        
                    ) : (
                        <div class='stavke-grid'>
                            {stavke.map((s) => (
                                <div key={s.rb} class={`stavka-card ${s.prosaoTest ? 'polozeno' : 'palo'}`}>
                                    <div class='card-header'>
                                        <span class='rb'>#{s.rb}</span>
                                        <span class={`status ${s.prosaoTest ? 'polozeno' : 'palo'}`}>{s.prosaoTest ? 'Prošao test' : 'Pao test'}</span>
                                    </div>
                                    <h4 class='vezba'>{s.vezbaNaziv} <p>Merna jedinica: <strong>{vezbe.find((v) => v.idVezbe === s.vezbaId).jedinicaMere === 'BROJPONAVLJANJA' ? 'broj ponavljanja' : vezbe.find((v) => v.idVezbe === s.vezbaId).jedinicaMere}</strong></p></h4>

                                    <div class='vrednosti'>
                                        <div class='vrednosti-item'>
                                            <span class='label'>Ostvareno:</span>
                                            <span class='vrednost'>{s.ostvareniRezultat}</span>
                                        </div>
                                        <div class='vrednosti-item'>
                                            <span class='label'>Norma:</span>
                                            <span class='vrednost'>{s.norma}</span>
                                        </div>
                                    </div>
                                    {(s.komentar) && (
                                        <p class='komentar'>
                                            <strong>Komentar:</strong><span class='komentar-tekst'>{s.komentar}</span>
                                        </p>
                                    )}
                                    <span><button class='btn-izmeni-stavku' onClick={() => setStavka({...s, vezbaId:s.vezbaId})}>✏️</button><button class='btn-obrisi-stavku' onClick={() => obrisiStavku(s.rb)}>🗑️</button></span>
                                </div>

                            ))}
                            <div  class='stavka-card dodaj-stavku' onClick={() => setPrikaziModalDodaj(true)}>
                                   <p>➕Dodaj stavku</p> 
                            </div>
                        </div>
                    )}
                </div>

                {prikaiModalDodaj && (
                    <div class='modal-form'>
                        <div class='modal-content'>
                            <form onSubmit={dodajStavku} class='modal-forma'>
                                <h3>Dodavanje nove stavke testiranja</h3>
                                <div class='form-group'>
                                    <label>Vežba:</label>
                                    <select value={novaStavka?.vezbaId} onChange ={(e) => setNovaStavka({...novaStavka, vezbaId: e.target.value})} required>
                                        <option value=''>Izaberite vežbu</option>
                                        {vezbe.map((v) => (
                                            <option key={v.idVezbe} value={v.idVezbe}>{v.naziv}</option>
                                        ))}
                                    </select>
                                </div>
                                <div class='form-group'>
                                    <label>Ostvareni rezultat:</label>
                                    <input type='number' step='0.01' value={novaStavka?.ostvareniRezultat} onChange={(e) => setNovaStavka({...novaStavka, ostvareniRezultat: e.target.value})} required/>
                                </div>
                                <div class='form-group'>
                                    <label>Komentar:</label>
                                    <textarea  rows='3' cols='25' value={novaStavka?.komentar} onChange={(e) => setNovaStavka({...novaStavka, komentar:e.target.value})}/>
                                </div>
                                <div>
                                    <button type='submit'>💾Sačuvaj</button>
                                    <button type='button' onClick={() => {setNovaStavka({
                                        vezbaId:'',
                                        ostvareniRezultat:'',
                                        komentar:''
                                    }); setPrikaziModalDodaj(false)}}>❌Otkaži</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                {stavka && (
                    <form class='modal-form' onSubmit = {izmeniStavku}>
                        <div class='modal-content'>
                            <h3>Izmeni stavku #{stavka.rb}</h3>
                            <div>
                                <label>Ostvareni rezultat: </label>
                                <input type='number' step='0.01' value={stavka.ostvareniRezultat} onChange={(e) => setStavka({...stavka, ostvareniRezultat:e.target.value === '' ? '' : e.target.value})}/>
                            </div>
                            <div>
                                <label>Komentar: </label><br/>
                                <textarea  rows='3' cols='25' value={stavka.komentar} onChange={(e) => setStavka({...stavka, komentar:e.target.value})}/>
                            </div>
                            <div>
                                <button type='submit'>💾Sačuvaj</button>
                                <button type='button' onClick={() => setStavka(null)}>❌Otkaži</button>
                            </div>
                        </div>
                    </form>
                )}
            </main>
        </div>
    )

}
