import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import NavBarSportista from "../components/NavBarSportista";
import './SportistaEvidencija.css'
import jsPDF from "jspdf";
import autoTable from 'jspdf-autotable';

export default function SportistaEvidencija() {
    const {id} = useParams();
    const navigate = useNavigate();

    const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
    const korisnik = sacuvaniKorisnik ? JSON.parse(sacuvaniKorisnik) : null;

    const [evidencija, setEvidencija] = useState(null);

    const [stavke, setStavke] = useState([]);

    
    const [imeTrenera, setImeTrenera] = useState('');

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };

    const generisiPDF = (item) => {        
        const doc = new jsPDF();

        doc.setFontSize(18);
        doc.setTextColor(3, 79, 58);
        doc.text("Izveštaj o testiranju sportiste", 14, 20);

        doc.setFontSize(11);
        doc.setTextColor(100,100,100);
        doc.text(`Datum generisanja: ${new Date().toLocaleDateString()}`, 14, 28);

        const tableData = [
                ['ID Testiranja', `#${item.idTestiranja}`],
                ['Ime i prezime sportiste', `${item.imeIPrezimeSportiste}`],
                ['Trener', `${imeTrenera}`],
                ['Rezultat testiranja', `${item.rezultatTestiranja}%`],
                ['Status', item.prosaoTestiranje ? 'Položio' : 'Nije položio'],
                ['Datum testiranja', item.datum ],
                ['Broj testova', item.brojTestova],
                ['Broj položenih testova', item.brojPolozenih],
                ['Broj palih testova', item.brojPalih]
            ]

        autoTable(doc, {
            startY:35,
            head: [['Polje', 'Detalji']],
            body: tableData,
            headStyles: {fillColor: [3, 79, 58]},
            theme: 'grid'
        });
        const stavke = item.stavke;
        if(stavke.length > 0){
            const poslednjaY = doc.lastAutoTable ? doc.lastAutoTable.finalY : 85;
            doc.setFontSize(14);
            doc.setTextColor(3, 79, 58);
            doc.text("Stavke testiranja", 14, poslednjaY + 12);

            const stavkeRedovi = stavke.map((s) => [
                s.rb,
                s.vezbaNaziv,
                s.ostvareniRezultat,
                s.norma,
                s.prosaoTest ? 'Prošao' : 'Pao',
                s.komentar ?? '',
            ]);

            autoTable(doc, {
                startY: poslednjaY + 18,
                head: [['RB', 'Naziv vežbe','Ostvareni rezultat','Norma', 'Prošao testiranje', 'Komentar']],
                body: stavkeRedovi,
                headStyles: { fillColor: [3, 79, 58] },
                theme: 'grid'
            })


        }
        
        doc.save(`evidencija_${item.idTestiranja}.pdf`);
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
                setImeTrenera(`${trener.ime} ${trener.prezime}`);
            }
        }catch(err){
            console.error("Greska pri ucitavanju trenera: ", err);
            
        }
    }

    useEffect(() => {
        const fetchDetalje = async () => {
            try{
                const res= await fetch(`http://localhost:8080/api/evidencije/${id}`, {
                    headers: getAuthHeaders()
                })
                if(!res.ok){
                    throw new Error("Neuspesno preuzimanje detalja evidencije");
                    
                }
                const data = await res.json();
                setEvidencija(data);
                setStavke(data.stavke || []);

                if(data.trenerId){
                    fetchTrenera(data.trenerId)
                }

            }catch(err){
                console.error('Greska: ', err);
                
            }
        }

        fetchDetalje();
    }, [id]);

    if(!evidencija){
        return(
            <div>
                <p class='nema-evidencije'>Evidencija nije pronadjena</p>
            </div>
        )
    }

    return(
        <div class='detalji-page-container'>
            <NavBarSportista korisnik={korisnik}/>

            <main class='detalji-content'>
                <h2>
                <p class='strelica' onClick={() => navigate('/sportista-main')}>⬅</p>
                Detalji evidencije testiranja #{evidencija.idTestiranja}
                </h2>
                <div class='info-card'>
                    <h3>Opšte informacije</h3>
                    <div class='info-grid'>
                        <p><strong>Trener:</strong> {imeTrenera}</p>
                        <p><strong>Datum testiranja:</strong> {evidencija.datum}</p>
                        <p><strong>Ukupan broj testova:</strong> {evidencija.brojTestova}</p>
                        <p><strong>Broj položenih testova:</strong> {evidencija.brojPolozenih}</p>
                        <p><strong>Broj palih testova:</strong> {evidencija.brojPalih}</p>
                        <p><strong>{evidencija.prosaoTestiranje ? 'Testiranje položeno✅' : 'Testiranje nije položeno❌'}</strong></p>
                        <p><strong>Konačan rezultat: </strong>{evidencija.rezultatTestiranja}%</p>
                    </div>
                </div>
                <div class='stavke-section'>
                    <h3>Stavke testiranja</h3>
                    {stavke.length === 0 ? (
                        <p class='nema-stavki'>Nema unetih stavki za ovo testiranje</p>
                    ) : (
                        <div class='stavke-grid'>
                            {stavke.map((s) => (
                                <div key={s.rb} class={`stavke-card ${s.prosaoTest ? 'polozeno' : 'palo'}`}>
                                    <div class='card-header'>
                                        <span class='rb'>#{s.rb}</span>
                                        <span class={`status ${s.prosaoTest ? 'polozeno': 'palo'}`}>{s.prosaoTest ? 'Prošao test' : 'Pao test'}</span>
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
                                    {s.komentar && (
                                        <p class='komentar'>
                                            <strong>Komentar:</strong><span class='komentar-tekst'>{s.komentar}</span>
                                        </p>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
                <div class='pdf-dugme-container'>
                    <button class='btn-generisi-pdf' onClick={() => generisiPDF(evidencija)}>
                        📄Generiši PDF izveštaj
                    </button>
                </div>
            </main>
        </div>
    )
}