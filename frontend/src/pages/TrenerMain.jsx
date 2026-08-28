import {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './TrenerMain.css';
import NavBarTrener from '../components/NavBarTrener';

export default function TrenerMain(){
    const [korisnik , setKorisnik] = useState(null);
    const [evidencije, setEvidencije] = useState([]);
    const [greska, setGreska] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        const sacuvaniKorisnik = localStorage.getItem('korisnik');
        try{
            const ulogovani = JSON.parse(sacuvaniKorisnik);
            setKorisnik(ulogovani);
            const idTrenera = ulogovani.id;
            fetchSportisti();
            if(idTrenera){
                fetchEvidencije(idTrenera);
            }else{
                setGreska('ID trenera nije pronađen u podacima o sesiji.');
            }
        }catch(e){
            console.error('Greška pri parsiranju korisnika.',e);
        }
    }, []);
    const fetchEvidencije = async (idTrenera) => {
        try{
            const response = await fetch(`http://localhost:8080/api/evidencije/trener/${idTrenera}`);
            if(!response.ok){
                if(response.status === 404){
                    setEvidencije([]);
                    return;
                }
                throw new Error("Neuspešno preuzimanje evidencija testiranja.");
                
            }
            const data = await response.json();
            setEvidencije(data);
        } catch(err){
            console.error('Greska tokom fetch-ovanja evidencija:', err);
            setGreska(err.message);
        }

    };

    const handleObrisiEvidenciju = async (idTestiranja) => {
        try{
            const res = await fetch(`http://localhost:8080/api/evidencije/${idTestiranja}`,{
                method: 'DELETE'
            });
            if(!res.ok){
                alert('Greška pri brisanju evidencije.');
            }
            fetchEvidencije(korisnik.id);
        }catch(err){
            alert(err.message);
        }
    };

    
    const [sportisti, setSportisti] = useState([]);

    const fetchSportisti = async () => {
        try{
            const res = await fetch('http://localhost:8080/api/sportisti');
            if(res.ok){
                const data = await res.json();
                setSportisti(data);
            }
        }catch(err){
            console.error('Greska pri ucitavanju sportista:', err);
            
        }
    }

    const [idSportiste, setIdSportiste] = useState('');
    const [datum, setDatum] = useState('');
    const [prosaoTestiranje, setProsaoTestiranje] = useState('');
    const [rezultatTestiranja, setRezultatTestiranja] = useState('');

    const [poruka, setPoruka] = useState('')

    const pretraziPoKriterijumima = async (e) => {
        e.preventDefault();

        try{
            const params = new URLSearchParams();
            if(korisnik?.id) params.append('idTrenera', korisnik.id);
            if(idSportiste) params.append('idSportiste', idSportiste);
            if(datum) params.append('datum', datum);
            if(prosaoTestiranje !== '') params.append('prosaoTestiranje', prosaoTestiranje);
            if(rezultatTestiranja) params.append('rezultatTestiranja', rezultatTestiranja);

            const res = await fetch(`http://localhost:8080/api/evidencije/pretraga?${params.toString()}`);

            if(!res.ok){
                if(res.status === 404){
                    setEvidencije([]);
                    setPoruka('Ne postoje evidencije po traženim kriterijumima.');
                    return;
                }
                throw new Error("Greska pri pretrazi evidencija sa servirea");
                
            }
            const data = await res.json();
            setEvidencije(data);

        }catch(err){
            console.error('Greska tokom pretrage:', err);
            setGreska(err.message);
        }
    }

    const handleReset = () => {
        setIdSportiste('');
        setDatum('');
        setProsaoTestiranje('');
        setRezultatTestiranja('');
        if(korisnik?.id){
            fetchEvidencije(korisnik.id);
        }
    }
    
    return(
        <div class='trener-container'>
            <NavBarTrener korisnik={korisnik}/>
            <main class='trener-content'>
                <section class="welcome-section">
                    <h1>Dobrodošli, {`${korisnik?.ime} `} </h1>
                    <p>Ovde možete upravljati evidencijama testiranja Vaših sportista.</p>
                </section>

                <section class='evidencije-section'>
                    <h2>Evidencije testiranja</h2>
                    <button class='btn-dodaj-evidenciju' onClick={() => navigate('/evidencija/nova')}>➕Dodaj evidenciju</button>
                    <form class='pretraga-forma' onSubmit={pretraziPoKriterijumima}>
                        <p>Filtriraj</p>
                        <select value={idSportiste} onChange={(e) => setIdSportiste(e.target.value)}>
                            <option value=''>Izaberi sportistu</option>
                            {sportisti.map((s) => (
                                <option key={s.id} value={s.id}>{s.ime} {s.prezime}</option>
                            ))}
                        </select>
                        <label>Od:</label>
                        <input type='date' value={datum} onChange={(e) => setDatum(e.target.value)}/>
                        <select value={prosaoTestiranje} onChange={(e) => {setProsaoTestiranje(e.target.value)}}>
                            <option value=''>Prošao test(Da/Ne)</option>
                            <option value='true'>Da</option>
                            <option value='false'>Ne</option>
                        </select>
                        <input type='number' step='0.1' placeholder='Min rezultat(%)' max='100' value={rezultatTestiranja} onChange={(e) => {setRezultatTestiranja(e.target.value); if(e.target.value >= 70) setProsaoTestiranje(true); if(e.target.value < 70) setProsaoTestiranje(false); if(!e.target.value)setProsaoTestiranje('')}}></input>
                        <button type='submit' class='btn-pretrazi'>🔎Pretraži</button>
                        <button type='button' onClick={handleReset} class='btn-reset'>Prikaži sve</button>
                    </form>
                    {greska && <div class='greska-message'>{greska}</div>}

                    {evidencije.length === 0 ? (
                        <p class='nema-evidencija-tekst'>{poruka || 'Trenutno nema zabeleženih testiranja u bazi.'}</p>
                        ): (
                            <div class='tabela'>
                                <table class='evidencije-tabela'>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Sportista</th>
                                            <th>Rezultat testiranja</th>
                                            <th>Datum testiranja</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {evidencije.map((item) => (
                                            <tr key={item.idTestiranja}
                                            class='red-evidencije'>
                                                <td 
                                            >#{item.idTestiranja}</td>
                                                <td>{item.imeIPrezimeSportiste}</td>
                                                <td>{item.rezultatTestiranja}%</td>
                                                <td>{item.datum}</td>
                                                <td class='btns'><button onClick={() => navigate(`/evidencija/${item.idTestiranja}`)}>✏️</button><button class='btn-obrisi-evidenciju' onClick= {() => handleObrisiEvidenciju(item.idTestiranja)} title='Obriši evidenciju'>❌</button></td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        
                    )}
                </section>
            </main>
        </div>
    )
}

