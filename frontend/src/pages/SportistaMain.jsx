import React, {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";
import NavBarSportista from '../components/NavBarSportista'
import './SportistaMain.css';

export default function SportistaMain() {
    const navigate = useNavigate();

    const sacuvaniKorisnik = localStorage.getItem('korisnik');
    const korisnik = sacuvaniKorisnik ? JSON.parse(sacuvaniKorisnik) : null;

    const [evidencije, setEvidencije] = useState([]);
    const [greska, setGreska] = useState('');

    useEffect(() => {
        if(!korisnik || !korisnik.id)
            return;
        const ucitajEvidencije = async () => {
            try{
                const res = await fetch(`http://localhost:8080/api/evidencije/pretraga?idSportiste=${korisnik.id}`);

                if(!res.ok){
                    const tekst = await res.text();
                    throw new Error(tekst || "Greska pri ucitavanju evidencija.");
                }
                const data = await res.json();
                setEvidencije(data);
                setGreska('');
            }catch(err){
                console.error('Greska pri preuzimanju evidencija:', err.message);
                setGreska('Greška prilikom učitavanja evidencija');
                
            }

        }
        ucitajEvidencije();
    }, [korisnik?.id]);

    return(
        <div>
            
        <NavBarSportista korisnik = {korisnik}/>
            <header>
                 <section class="header-section">
                    <h2>Dobrodošli, {korisnik?.ime || korisnik?.korisnickoIme}</h2>
                    <p>Pregled Vaših evidencija testiranja</p>
                </section>
            </header>
                <h3>Moje evidencije testiranja</h3>
                {greska && <div class='greska'>{greska}</div>}
                {!greska && (evidencije.length === 0 ? (
                    <p class='nema-evidencija'>Nema pronađenih evidencija za Vas.</p>
                ) : (
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
                            {evidencije.map((e) => (
                                <tr key={e.idTestiranja} onClick={() => navigate(`/sportista-evidencija/${e.idTestiranja}`)}>
                                    <td>{e.idTestiranja}</td>
                                    <td>{e.trenerId}</td>
                                    <td>{e.datum}</td>
                                    <td>{e.rezultatTestiranja}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ))}
        </div>
    )
}