import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './NovaEvidencija.css'

const NovaEvidencija = () => {
    const navigate = useNavigate();

    const [korisnik, setKorisnik] = useState(null);

    const [sportisti, setSportisti] = useState([]);
    const [vezbe, setVezbe] = useState([]);
    const [trenerId, setTrenerId] = useState('');
    const [sportistaId, setSportistaId] = useState('');
    const [datum, setDatum] = useState('');
    const [stavke, setStavke] = useState([]);

    const [prikaziModalDodaj, setPrikaziModalDodaj] = useState(false);

    const [novaStavka, setNovaStavka] = useState({
        vezbaId:'',
        ostvareniRezultat:'',
        komentar:''
    });

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

    useEffect(() => {
        const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
        try{
            if(sacuvaniKorisnik){
                const ulogovani = JSON.parse(sacuvaniKorisnik);
                setKorisnik(ulogovani);
                setTrenerId(ulogovani.id);
            }
        }catch(err){
            console.error('Greska pri parsiranju korisnika:', err);
            
        }
        fetchSportisti();
        fetchVezbe();
    }, []);

    const fetchSportisti = async () => {
        try{
            const res  = await fetch('http://localhost:8080/api/sportisti', {
                headers: getAuthHeaders()
            });
            if(res.ok){
                const data = await res.json();
                setSportisti(data);
            }
        }catch(err){
            console.error('Greska pri ucitavaju sportista:', err);
            
        }
    }

    const fetchVezbe = async () => {
        try{
            const res = await fetch('http://localhost:8080/api/vezbe', {
                headers: getAuthHeaders()
            });
            if(!res.ok)
                throw new Error("Greska pri ucitavanju vezbi");
            const data = await res.json();
            setVezbe(data);
                
        }catch(err){
            console.error('Greska pri preuzimanju vezbi: ', err);
            
        }
    }

    const handleDodajStavku =async (e) => {
        e.preventDefault();
        if(!sportistaId){
            alert('Izaberite sportistu');
            return;
        }
        const zaSlanje = {
            vezbaId: parseInt(novaStavka.vezbaId),
            ostvareniRezultat:parseFloat(novaStavka.ostvareniRezultat),
            komentar:novaStavka.komentar
        };
        try{
            const res = await fetch(`http://localhost:8080/api/evidencije/izracunaj-stavku?sportistaId=${sportistaId}`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(zaSlanje)
            });
            if(res.ok){
                const obradjena = await res.json();
                const novaSaRb = {
                    ...obradjena,
                    rb: stavke.length + 1
                };

                setStavke([...stavke, novaSaRb]);
                setNovaStavka({vezbaId:'', ostvareniRezultat:'', komentar:''});
                setPrikaziModalDodaj(false);
            }else{
                const greska = await res.text();
                alert('Greska sa servera: ', greska);
            }
        }catch(err){
            console.error('Greska pri proracunu stavke:', err);
            
        }
    }


    const handleSacuvajEvidenciju = async (e) => {
        e.preventDefault();
        if(!sportistaId || !datum){
            alert('Molimo izaberite sportistu i datum testiranja.');
            return;
        }
        if(stavke.length === 0){
            alert('Morate dodati bar jednu stavku testiranja.');
            return;
        }
        const novaEvidencijaDto = {
            datum:datum,
            trenerId:parseInt(trenerId),
            sportistaId: parseInt(sportistaId),
            stavke: stavke.map(s => ({
                rb: s.rb,
                vezbaId: s.vezbaId,
                ostvareniRezultat: s.ostvareniRezultat,
                komentar: s.komentar,
                prosaoTest: s.prosaoTest
            }))
        }

        try{
            const res = await fetch('http://localhost:8080/api/evidencije', {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(novaEvidencijaDto)
            });
            if(res.ok){
                navigate('/trener-main');
            }else{
                const greska = await res.text();
                alert('Greska pri cuvanju evidencije: '+ greska);
            }
        }catch(err){
            console.error('Greska pri slanju evidencije:', err);
            console.log(err);
            
        }
    }

    

    return(
        <div class='nova-evidencija-container'>
            <p onClick={() => navigate('/trener-main')}>⬅</p><h2>Nova evidencija testiranja</h2>

            <form>
                <div class='form-group-part'>
                    <div class='form-group'>
                        <label>Sportista:</label>
                        <select value={sportistaId} onChange={(e) => setSportistaId(e.target.value)} required>
                            <option value=''>Izaberi sportistu</option>
                            {sportisti.map(s => (
                                <option key={s.id} value={s.id}>{s.ime} {s.prezime}</option>
                            ))}
                        </select>
                    </div>
                    <div class='form-group'>
                        <label>Datum testiranja:</label>
                        <input type='date' value={datum} onChange={(e) => setDatum(e.target.value)} required></input>
                    </div>
                </div>
                <h3>Stavke testiranja</h3>
                <div class='stavke-grid'>
                    {stavke.map((s, index) => (
                        <div key={s.rb} class={`stavka-card ${s.prosaoTest ? 'polozeno' : 'palo'}`}>
                            <div class='card-header'>
                                <span class='rb'>#{s.rb}</span>
                                <span class={`status ${s.prosaoTest ? 'polozeno' : 'palo'}`}>{s.prosaoTest ? 'Prošao test' : 'Pao test'}</span>
                            </div>
                            <h4 class='vezba'>{s.vezbaNaziv}</h4>

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
                                    <strong>Komentar:</strong> {s.komentar}
                                </p>
                            )}
                        </div>

                    ))}
                    <div  class='stavka-card dodaj-stavku' onClick={() => setPrikaziModalDodaj(true)}>
                            <p>➕Dodaj stavku</p> 
                    </div>
                </div>
                <div class='btns'>
                    <button type='submit' onClick={handleSacuvajEvidenciju} class='btn-sacuvaj-evidenciju'>💾 Sacuvaj evidenciju</button>
                </div>
            </form>

            {prikaziModalDodaj && (
                    <div class='modal-form'>
                        <div class='modal-content'>
                            <form class='modal-forma'>
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
                                    <button type='submit' onClick={handleDodajStavku}>💾Sačuvaj</button>
                                    <button type='button' onClick={() => setPrikaziModalDodaj(false)}>❌Otkaži</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

        </div>

        
    )
}

export default NovaEvidencija;
