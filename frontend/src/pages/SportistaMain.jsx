import React, {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";
import NavBarSportista from '../components/NavBarSportista'
import './SportistaMain.css';

export default function SportistaMain() {
    const navigate = useNavigate();

    const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
    const korisnik = sacuvaniKorisnik ? JSON.parse(sacuvaniKorisnik) : null;

    const [evidencije, setEvidencije] = useState([]);
    const [greska, setGreska] = useState('');

    const [imenaTrenera, setImenaTrenera] = useState({});

    const [treneri, setTreneri] = useState([]);

    const [trenutnaStrana, setTrenutnaStrana] = useState(1);
    const [evidencijaPoStrani] = useState(5);

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

    const fetchTreneri = async() => {
        try{
            const res = await fetch(`http://localhost:8080/api/treneri`, {
                headers: getAuthHeaders()
            });
            if(res.ok){
                const data = await res.json();
                console.log(data);
                
                setTreneri(data);
                return;
            }
        }catch(err){
            console.error("Greska pri ucitavanju trenera");
            
        }
    }

    const fetchTrenera = async(id) => {
        if(!id)
            return null;
        try{
            const res = await fetch(`http://localhost:8080/api/treneri/${id}`, {
                headers: getAuthHeaders()
            });
            if(res.ok){
                const trener = await res.json();
                return `${trener.ime} ${trener.prezime}`;
            }
        }catch(err){
            console.error("Greska pri ucitavanju trenera: ", err);
            
        }
        return `Trener (ID: ${id})`;
    }

    useEffect(() => {
        if(!korisnik || !korisnik.id)
            return;
        const ucitajEvidencije = async () => {
            try{
                const res = await fetch(`http://localhost:8080/api/evidencije/pretraga?idSportiste=${korisnik.id}`, {
                    headers: getAuthHeaders()
                });

                if(!res.ok){
                    if(res.status === 401){
                        sessionStorage.clear();
                        alert('Sesija je istekla.')
                        navigate('/');
                        return;
                    }
                    const tekst = await res.text();
                    throw new Error(tekst || "Greska pri ucitavanju evidencija.");
                }
                const data = await res.json();
                setEvidencije(data);
                setGreska('');
                setTrenutnaStrana(1);

                const jedinstveniId = [...new Set(data.map(e => e.trenerId).filter(Boolean))];
                const novaMapaImena = {};
                for(const id of jedinstveniId){
                    const punoIme = await fetchTrenera(id);
                    novaMapaImena[id] = punoIme;
                }
                setImenaTrenera(novaMapaImena);
            }catch(err){
                console.error('Greska pri preuzimanju evidencija:', err.message);
                setGreska('Greška prilikom učitavanja evidencija');
                
            }

        }
        ucitajEvidencije();
        fetchTreneri();
    }, [korisnik?.id]);

    const indexOfLastToDo = trenutnaStrana * evidencijaPoStrani;
    const indexOfFirstToDo = indexOfLastToDo - evidencijaPoStrani;
    const currentToDos = evidencije.slice(indexOfFirstToDo, indexOfLastToDo);
    const pageNumbers = [];
    for(let i = 1; i <= Math.ceil(evidencije.length / evidencijaPoStrani); i++){
        pageNumbers.push(i);
    }

    const renderPageNumbers = pageNumbers.map(number => {
        return(
            <li key={number} id={number} class={trenutnaStrana === number ? 'active' : ''} onClick={(e) => setTrenutnaStrana(Number(e.target.id))}>
                {number}
            </li>
        )
    })

    const [idTrenera, setIdTrenera] = useState('');
    const [datum, setDatum] = useState('');
    const [prosaoTestiranje, setProsaoTestiranje] = useState('');
    const [rezultatTestiranja, setRezultatTestiranja] = useState('');
    const [poruka, setPoruka] = useState('')
    
    const handleReset = () => {
        setIdTrenera('');
        setDatum('');
        setProsaoTestiranje('');
        setRezultatTestiranja('');
        setPoruka('');
        const ucitajEvidencije = async () => {
            try{
                const res = await fetch(`http://localhost:8080/api/evidencije/pretraga?idSportiste=${korisnik.id}`, {
                    headers: getAuthHeaders()
                });

                if(!res.ok){
                    if(res.status === 401){
                        sessionStorage.clear();
                        alert('Sesija je istekla.')
                        navigate('/');
                        return;
                    }
                    const tekst = await res.text();
                    throw new Error(tekst || "Greska pri ucitavanju evidencija.");
                }
                const data = await res.json();
                setEvidencije(data);
                setGreska('');
                setTrenutnaStrana(1);

                const jedinstveniId = [...new Set(data.map(e => e.trenerId).filter(Boolean))];
                const novaMapaImena = {};
                for(const id of jedinstveniId){
                    const punoIme = await fetchTrenera(id);
                    novaMapaImena[id] = punoIme;
                }
                setImenaTrenera(novaMapaImena);
            }catch(err){
                console.error('Greska pri preuzimanju evidencija:', err.message);
                setGreska('Greška prilikom učitavanja evidencija');
                
            }

        }
        ucitajEvidencije();
    }

    const pretraziPoKriterijumima = async (e) => {
        e.preventDefault();

        try{
            const params = new URLSearchParams();
            if(korisnik?.id) params.append('idSportiste', korisnik.id);
            if(idTrenera) params.append('idTrenera', idTrenera);
            if(datum) params.append('datum', datum);
            if(prosaoTestiranje !== '') params.append('prosaoTestiranje', prosaoTestiranje);
            if(rezultatTestiranja) params.append('rezultatTestiranja', rezultatTestiranja);

            const res = await fetch(`http://localhost:8080/api/evidencije/pretraga?${params.toString()}`, {
                headers: getAuthHeaders()
            });

            if(!res.ok){
                if(res.status === 404){
                    setEvidencije([]);
                    setPoruka('Ne postoje evidencije po traženim kriterijumima.');
                    setTrenutnaStrana(1);
                    return;
                }
                if(res.status === 401){
                    sessionStorage.clear();
                    alert('Sesija je istekla.')
                    navigate('/');
                    return;
                }
                throw new Error("Greska pri pretrazi evidencija sa servera");
                
            }
            const data = await res.json();
            setEvidencije(data);
            setPoruka('');
            setTrenutnaStrana(1);

        }catch(err){
            console.error('Greska tokom pretrage:', err);
            setGreska(err.message);
        }
    }

    return(
        <div class='sportista-main-page'>
            
        <NavBarSportista korisnik = {korisnik}/>
            <header>
                 <section class="header-section">
                    <h2>Dobrodošli, {korisnik?.ime || korisnik?.korisnickoIme}</h2>
                    <p>Pregled Vaših evidencija testiranja</p>
                </section>
            </header>
                <h3>Moje evidencije testiranja</h3>
                <form class='pretraga-forma' onSubmit={pretraziPoKriterijumima}>
                        <p>Filtriraj</p>
                        <select value={idTrenera} onChange={(e) => setIdTrenera(e.target.value)}>
                            <option value=''>Izaberi trenera</option>
                            {treneri.map((t) => (
                                <option key={t.id} value={t.idKorisnika}>{t.ime} {t.prezime}</option>
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
                <p class='info-evid'>Kliknite na željenu evidenciju da vidite detalje</p>
                {/* {greska && <div class='greska'>{greska}</div>} */}
                {(evidencije.length === 0 ? (
                    <p class='nema-evidencija'>{poruka || `Nema pronađenih evidencija za Vas.`}</p>
                ) : (
                    <>
                    <table class='tabela-evidencije'>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Trener</th>
                                <th>Rezultat testiranja</th>
                                <th>Datum testiranja</th>
                            </tr>
                        </thead>
                        <tbody>
                            {currentToDos.map((e) => (
                                <tr key={e.idTestiranja} onClick={() => navigate(`/sportista-evidencija/${e.idTestiranja}`)}>
                                    <td>{e.idTestiranja}</td>
                                    <td>{imenaTrenera[e.trenerId]}</td>
                                    <td>{e.rezultatTestiranja}%</td>
                                    <td>{e.datum}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    
                    {evidencije.length > evidencijaPoStrani && (
                                <ul id='page-numbers-s'>
                                    {renderPageNumbers}
                                </ul>
                            )}
                    </>
                ))}
        </div>
    )
}