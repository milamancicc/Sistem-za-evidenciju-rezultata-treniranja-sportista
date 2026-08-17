import {useState, useEffect} from 'react';
// import {useNavigate} from 'react-router-dom';
import './TrenerMain.css';

export default function TrenerMain(){
    const [korisnik , setKorisnik] = useState(null);
    const [evidencije, setEvidencije] = useState([]);
    const [greska, setGreska] = useState('');


    useEffect(() => {
        const sacuvaniKorisnik = localStorage.getItem('korisnik');
        try{
            const ulogovani = JSON.parse(sacuvaniKorisnik);
            setKorisnik(ulogovani);
            const idTrenera = ulogovani.id;

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
    
    return(
        <div class='trener-container'>
            <main class='trener-content'>
                <section class="welcome-section">
                    <h1>Dobrodošli, {`${korisnik?.ime} ${korisnik?.prezime}`} </h1>
                    <p>Ovde možete upravljati evidencijama testiranja Vaših sportista.</p>
                </section>

                <section class='evidencije-section'>
                    <h2>Evidencije testiranja</h2>
                    {greska && <div class='greska-message'>{greska}</div>}

                    {evidencije.length === 0 ? (
                        <p class='nema-evidencija-tekst'>Trenutno nema zabeleženih testiranja u bazi</p>
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
                                            <tr key={item.idTestiranja}>
                                                <td>#{item.idTestiranja}</td>
                                                <td>{item.imeIPrezimeSportiste}</td>
                                                <td>{item.rezultatTestiranja}%</td>
                                                <td>{item.datum}</td>
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

