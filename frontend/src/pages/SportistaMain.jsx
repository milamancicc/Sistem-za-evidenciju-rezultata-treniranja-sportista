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

    const [trenutnaStrana, setTrenutnaStrana] = useState(1);
    const [evidencijaPoStrani] = useState(5);

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

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
                <p class='info-evid'>Kliknite na željenu evidenciju da vidite detalje</p>
                {greska && <div class='greska'>{greska}</div>}
                {!greska && (evidencije.length === 0 ? (
                    <p class='nema-evidencija'>Nema pronađenih evidencija za Vas.</p>
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