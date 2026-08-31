import React, {useState, useEffect} from 'react';
import NavBarTrener from '../components/NavBarTrener';
import './TrenerVezbe.css';

export default function TrenerVezbe() {
    
    const [korisnik, setKorisnik] = useState(null);
    const [vezbe, setVezbe] = useState([]);
    const [norme, setNorme] = useState({});
    const [poruka, setPoruka] = useState('');

    const [nazivVezbe, setNazivVezbe] = useState('');
    const [jedinicaMere, setJedinicaMere] = useState('SEKUNDA');
    const [opisVezbe, setOpisVezbe] = useState('');

    const [izabranaVezbaZaNormaModal, setIzabranaVezbaZaNormaModal] = useState(null);
    const [vrednostNorme, setVrednostNorme] = useState('');
    const [polNorme, setPolNorme] = useState('MUSKI');
    const [starosnaKategorija, setStarosnaKategorija] = useState('PIONIR');

    const [prikaziModalVezba, setPrikaziModalVezba] = useState(false);
    const [prikaziModalNorma, setPrikaziModalNorma] = useState(false);

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

    useEffect(() => {
        const sacuvani = sessionStorage.getItem('korisnik');
        console.log(sessionStorage.getItem('korisnik'));
        
        if(sacuvani){
            setKorisnik(JSON.parse(sacuvani));
        }

        ucitajVezbe();

    }, []);

    useEffect(() => {
        
        if(vezbe.length > 0){
            vezbe.forEach(v => ucitajNormeZaVezbu(v.idVezbe));
        }
    }, [vezbe]);

    const handleDodajVezbu = async (e) => {
        e.preventDefault();
        setPoruka('');
        const novaVezba ={
            naziv: nazivVezbe,
            opis: opisVezbe,
            jedinicaMere: jedinicaMere
        };
        try{
            const res = await fetch('http://localhost:8080/api/vezbe', {
                method:'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(novaVezba)
            });
            if(res.ok){
                zatvoriModalVezba();
                ucitajVezbe();
            }else{
                setPoruka('Greška pri čuvanju vežbe.');
            }
        }catch(err){
            setPoruka('Serverska greška.');
        }
    }

    const handleDodajNormu = async (e) => {
        e.preventDefault();
        setPoruka('');
        if(!izabranaVezbaZaNormaModal)
            return;
        const novaNorma = {
            norma: Number(vrednostNorme),
            pol: polNorme,
            starosnaKategorija: starosnaKategorija,
            idVezbe: izabranaVezbaZaNormaModal.idVezbe
        };

        try{
            const res = await fetch(`http://localhost:8080/api/norme`, {
                method: 'POST',
                headers: getAuthHeaders(),
                body: JSON.stringify(novaNorma)
            });
            if(res.ok){
                zatvoriModalNorma();
                vezbe.forEach(v => ucitajNormeZaVezbu(v.idVezbe));
            }else{
                setPoruka('Greška pri čuvanju norme.');
            }
        }catch(err){
            setPoruka('Serverska greška.');
        }
    }

    const otvoriModalVezba = () => {
        setPrikaziModalNorma(false);
        setPrikaziModalVezba(true);
    }

    const zatvoriModalVezba = () =>{
        setPrikaziModalVezba(false);
        
        setNazivVezbe('');
        setOpisVezbe('');
        setJedinicaMere('SEKUNDA');
    }

    const otvoriModalNorma = (vezba) => {
        setPrikaziModalVezba(false);
        setPrikaziModalNorma(true);
        setIzabranaVezbaZaNormaModal(vezba);
    }

    const zatvoriModalNorma = () => {
        setIzabranaVezbaZaNormaModal(null);
        setPrikaziModalNorma(false);
        setPolNorme('MUSKI');
        setStarosnaKategorija('PIONIR');
        setVrednostNorme('');
    }

    const ucitajVezbe = async () => {
        try{
            const res= await fetch('http://localhost:8080/api/vezbe', {
                headers: getAuthHeaders()
            });
            if(res.ok){
                const data = await res.json();
                setVezbe(data);
            }
        }catch(err){
            console.error('Greška pri učitavanju vežbi: ', err);
            
        }
    }

    const ucitajNormeZaVezbu = async (id) => {
        try{
            const res = await fetch(`http://localhost:8080/api/norme/vezba/${id}`, {
                headers: getAuthHeaders()
            });
            if(res.ok){
                const data = await res.json();
                setNorme(prev => ({...prev, [id]: data}));
            }
        }catch(err){
            console.error('Greska pri ucitavanju normi');
            
        }
    }

    const obrisiVezbu = async (id) => {
        try{
            const res = await fetch(`http://localhost:8080/api/vezbe/${id}`, {
                method: 'DELETE',
                headers: getAuthHeaders()
            });
            if(res.ok){
                ucitajVezbe();
            }
            else{
                alert('Greška pri brisanju vežbe.');
            }
        }catch(err){
            console.error('Greska  pri brisanju vezbe: ', err);
            
        }
    }

    const obrisiNormu = async (idNorme, idVezbe) => {
        console.log(idNorme);
        
        try{
            const res = await fetch(`http://localhost:8080/api/norme/${Number(idNorme)}`, {
                method: 'DELETE',
                headers: getAuthHeaders()
            });
            if(res.ok){
                ucitajNormeZaVezbu(idVezbe);
            }
            else{
                alert('Greška pri brisanju norme.');
            }
        }catch(err){
            console.error('Greska pri brisanju norme: ', err);
            
        }
    }

    return(
        <div class = 'vezbe-page'>
            <NavBarTrener korisnik={korisnik}/>
            <main class='vezbe-container'>
                <div class='header'>
                    <div>
                        <h2>Katalog vežbi i njihovih normi</h2>
                        <p class = 'subtitle'>Pregled svih vežbi i odgovarajućih normi po kategorijama</p>
                    </div>
                    <button class='btn-dodaj' onClick={otvoriModalVezba}>➕Dodaj novu vežbu</button>
                </div>
                <div class='vezbe'>
                    {vezbe.map((vezba) => {
                        const njeneNorme = norme[vezba.idVezbe] || [];
                    return (
                        <div key={vezba.idVezbe} class='vezba-card'>
                            <div class='vezba-card-header'>
                                <h3>{vezba.naziv}</h3>
                                <div class='header-stavke'>
                                    <span class='jedinica-mere'>{vezba.jedinicaMere === 'SEKUNDA' ? 'sekunde' : (vezba.jedinicaMere === 'MINUT' ? 'minuti' : (vezba.jedinicaMere === 'METAR' ? 'metar' : (vezba.jedinicaMere === 'KILOGRAM' ? 'kilogram' : 'broj ponavljanja')))}</span>
                                    <button class='btn-obrisi-vezbu' onClick = {()=>obrisiVezbu(vezba.idVezbe)}>🗑️</button>
                                </div>
                            </div>
                            <p class = 'vezba-opis'>{vezba.opis || 'Nema opisa za ovu vežbu.'}</p>

                            <div class='norme'>
                                <h4>Norme:</h4>
                                {njeneNorme.length > 0 ? (
                                    <table class='norme-tabela'>
                                        <thead>
                                            <tr>
                                                <th>Starosna kategorija</th>
                                                <th>Pol</th>
                                                <th>Norma</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {njeneNorme.map((norma) => (
                                                <tr key={norma.idNorme}>
                                                    <td>{norma.starosnaKategorija}</td>
                                                    <td>{norma.pol === 'MUSKI' ? 'Muški' : 'Ženski'}</td>
                                                    <td><strong>{norma.norma}</strong> {vezba.jedinicaMere === 'SEKUNDA' ? 'sekundi' : (vezba.jedinicaMere === 'MINUT' ? 'minuta' : (vezba.jedinicaMere === 'METAR' ? 'metara' : (vezba.jedinicaMere === 'KILOGRAM' ? 'kilograma' : 'ponavljanja')))}</td>
                                                    <td><button class='btn-obrisi-normu' onClick = {() => obrisiNormu(norma.idNorme,vezba.idVezbe)}>🗑️</button></td>
                                                </tr>
                                               
                                            ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <p class='nema-normi'>Trenutno nema unetih normi za ovu vežbu.</p>
                                )}
                            </div>

                            <button class='btn-dodaj-normu' onClick={() => otvoriModalNorma(vezba)}>🎯Dodaj normu za ovu vežbu</button>
                        </div>
                    )
                    })}
                </div>
            </main>

            {prikaziModalVezba && (
                <div class='modal'>
                    <div class='modal-card'>
                        <div class='modal-header'>
                            <h3>Nova vežba</h3>
                            <button class='btn-zatvori' onClick={zatvoriModalVezba}>❌</button>
                        </div>
                        {poruka && <div class='poruka'>{poruka}</div>}
                        <form onSubmit = {handleDodajVezbu}>
                            <div class='form-group'>
                                <label>Naziv vežbe</label>
                                <input type='text' value={nazivVezbe} onChange={(e) => setNazivVezbe(e.target.value)} required/>
                            </div>
                            <div class='form-group'>
                                <label>Opis vežbe</label>
                                <textarea value={opisVezbe} onChange={(e) => setOpisVezbe(e.target.value)} rows='3'/>
                            </div>

                            <div class='form-group'>
                                <label>Jedinica mere</label>
                                <select value={jedinicaMere} onChange={(e) => setJedinicaMere(e.target.value)}>
                                    <option value='SEKUNDA'>Sekunde</option>
                                    <option value='MINUT'>Minuti</option>
                                    <option value='METAR'>Metri</option>
                                    <option value='KILOGRAM'>Kilogrami</option>
                                    <option value='BROJPONAVLJANJA'>Broj ponavljanja</option>
                                </select>
                            </div>
                            <div class='modal-btns' >
                                <button type='button' class='btn-otkazi' onClick={() => {setNazivVezbe(''); setOpisVezbe(''); setJedinicaMere('SEKUNDA')}}>Otkaži</button>
                                <button type='submit' class='btn-sacuvaj'>Sačuvaj vežbu</button>
                            </div>
                        </form>
                    </div>

                </div>
            )}

            {prikaziModalNorma && (
                <div class='modal'>
                    <div class='modal-card'>
                        <div class='modal-header'>
                            <h3>Nova norma za vežbu: {izabranaVezbaZaNormaModal?.naziv}</h3>
                            <button class='btn-zatvori' onClick={zatvoriModalNorma}>❌</button>
                        </div>
                        {poruka && <div class='poruka'>{poruka}</div>}
                        <form onSubmit={handleDodajNormu}>
                            <div class='form-group'>
                                <label>Ciljna norma ({izabranaVezbaZaNormaModal?.jedinicaMere === 'SEKUNDA' ? 'sekunde' : (izabranaVezbaZaNormaModal?.jedinicaMere === 'MINUT' ? 'minuti' : (izabranaVezbaZaNormaModal?.jedinicaMere === 'METAR' ? 'metar' : (izabranaVezbaZaNormaModal?.jedinicaMere === 'KILOGRAM' ? 'kilogram' : 'broj ponavljanja')))})</label>
                                <input type='number' step='0.01' value={vrednostNorme} onChange={(e) => setVrednostNorme(e.target.value)} required/>
                            </div>
                            <div class='form-group'>
                                <label>Pol</label>
                                <select value={polNorme} onChange={(e) => setPolNorme(e.target.value)}>
                                    <option value='MUSKI'>Muški</option>
                                    <option value='ZENSKI'>Ženski</option>
                                </select>
                            </div>
                            <div class='form-group'>
                                <label>Starosna kategorija</label>
                                <select value={starosnaKategorija} onChange={(e) => setStarosnaKategorija(e.target.value)}>
                                    <option value='PIONIR'>Pioniri</option>
                                    <option value='KADET'>Kadeti</option>
                                    <option value='JUNIOR'>Juniori</option>
                                    <option value='SENIOR'>Seniori</option>
                                    <option value='VETERAN'>Veterani</option>
                                </select>
                            </div>
                            <div class='modal-btns'>
                                <button type='button' class='btn-otkazi' onClick={() => {setVrednostNorme(''); setPolNorme('MUSKI'); setStarosnaKategorija('PIONIR');}}>Otkaži</button>
                                <button type='submit' class='btn-sacuvaj'>Sačuvaj normu</button>
                           </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    )
}