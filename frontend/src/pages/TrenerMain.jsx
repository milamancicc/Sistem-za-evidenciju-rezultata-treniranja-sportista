import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';
import './TrenerMain.css';
import NavBarTrener from '../components/NavBarTrener';

export default function TrenerMain(){
    const [korisnik , setKorisnik] = useState(null);
    const [evidencije, setEvidencije] = useState([]);
    const [greska, setGreska] = useState('');

    const [trenutnaStrana, setTrenutnaStrana] = useState(1);
    const [evidencijaPoStrani] = useState(5);

    const navigate = useNavigate();

    const exportExcelOfAll = async (evidencije) => {
        const workbook = new ExcelJS.Workbook();
        const worksheet = workbook.addWorksheet(`Evidencije testiranja`);
        const worksheetStavke = workbook.addWorksheet(`Stavke testiranja`);

        worksheet.columns = [
            { header: 'ID', key: 'id', width: 30},
            { header: 'Datum', key: 'datum', width: 30},
            { header: 'Sportista', key: 'sportista', width: 30},
            { header: 'Broj Testova', key: 'brojTestova', width: 30},
            { header: 'Broj polozenih', key: 'brojPolozenih', width: 30},
            { header: 'Broj palih', key: 'brojPalih', width: 30},
            { header: 'Prosao testiranje', key: 'prosaoTestiranje', width: 30},
            { header: 'Rezultat testiranja', key: 'rezultatTestiranja', width: 30}
        ];

        worksheetStavke.columns = [
            { header: 'ID testiranja', key: 'id', width: 30},
            { header: 'RB', key: 'rb', width: 30},
            { header: 'Naziv vežbe', key: 'nazivVezbe', width: 30},
            { header: 'Ostvareni rezultat', key: 'ostvareniRezultat', width: 30},
            { header: 'Norma', key: 'norma', width: 30},
            { header: 'Prošao test', key: 'prosaoTest', width: 30},
            { header: 'Komentar', key: 'komentar', width: 30},
        ];

        evidencije.forEach((item) => {
            const stavke = item.stavke;
            stavke.forEach((s) => {
                worksheetStavke.addRow({
                    id: item.idTestiranja,
                    rb: s.rb,
                    nazivVezbe: s.vezbaNaziv,
                    ostvareniRezultat: s.ostvareniRezultat,
                    norma: s.norma,
                    prosaoTest: s.prosaoTest,
                    komentar: s.komentar
                })
            })
        })

        

        evidencije.forEach((item) => {
            worksheet.addRow({
                id: item.idTestiranja,
                datum: item.datum,
                sportista: item.imeIPrezimeSportiste,
                brojTestova: item.brojTestova,
                brojPolozenih: item.brojPolozenih,
                brojPalih: item.brojPalih,
                prosaoTestiranje: item.prosaoTestiranje,
                rezultatTestiranja: item.rezultatTestiranja
            });
        });

        worksheetStavke.getRow(1).eachCell((cell) => {
            cell.font = {size: 14,
                bold: true, 
                color: {argb: 'FFFFFF'}
            };
            cell.alignment = { horizontal: 'center'};
            cell.fill = {
                type: 'pattern',
                pattern: 'solid',
                fgColor : {argb: '1e3c72'}
            };
        });

        worksheet.getRow(1).eachCell((cell) => {
            cell.font = {size: 14,
                bold: true, 
                color: {argb: 'FFFFFF'}
            };
            cell.alignment = { horizontal: 'center'};
            cell.fill = {
                type: 'pattern',
                pattern: 'solid',
                fgColor : {argb: '1e3c72'}
            };
        });

        worksheetStavke.eachRow((row, rowNum) => {
            if(rowNum === 1)
                return;
            row.eachCell((cell, colNum) => {
                if( colNum === 6 ){
                    const vrednost = cell.value;
                    if(vrednost === true){
                        row.eachCell((cell1, colNum1) => {
                            if(colNum1 <= 8){
                                cell1.fill = {type: 'pattern', pattern: 'solid', fgColor: { argb: 'D1E7DD' }}
                                cell1.border = {
                                    bottom: {style: 'thin', color: {argb: '0F5132'}}
                                }
                            }
                        })
                        cell.font = {
                            size : 10, bold: true, color: {argb: '0F5132'}
                        }
                    }else if( vrednost === false){
                        row.eachCell((cell1, colNum1) => {
                            if(colNum1 <= 8){
                                cell1.fill = {type: 'pattern', pattern: 'solid', fgColor: { argb: 'F8D7DA' }}
                                cell1.border = {
                                    bottom: {style: 'thin', color: {argb: '842029'}}
                                }
                            }
                        })
                        cell.font = {
                            size :10, bold: true, color: {argb: '842029'}
                        }
                    }
                }
                cell.alignment = {horizontal: 'center', vertical: 'middle'}
            }
            
        )
        })

        worksheet.eachRow((row, rowNum) => {
            if(rowNum === 1)
                return;
            row.eachCell((cell, colNum) => {
                if( colNum === 7){
                    const vrednost = cell.value;
                    if(vrednost === true){
                        row.eachCell((cell1, colNum1) => {
                            if(colNum1 <= 8){
                                cell1.fill = {type: 'pattern', pattern: 'solid', fgColor: { argb: 'D1E7DD' }}
                                cell1.border = {
                                    bottom: {style: 'thin', color: {argb: '0F5132'}}
                                }
                            }
                        })
                        cell.font = {
                            size : 10, bold: true, color: {argb: '0F5132'}
                        }
                        
                    }else if( vrednost === false){
                        row.eachCell((cell1, colNum1) => {
                            if(colNum1 <= 8){
                                cell1.fill = {type: 'pattern', pattern: 'solid', fgColor: { argb: 'F8D7DA' }}
                                cell1.border = {
                                    bottom: {style: 'thin', color: {argb: '842029'}}
                                }
                            }
                        })
                        cell.font = {
                            size :10, bold: true, color: {argb: '842029'}
                        }
                    }
                }
                cell.alignment = {horizontal: 'center', vertical: 'middle'}
            })
            
        })
        const buffer = await workbook.xlsx.writeBuffer();
        const blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetxml.sheet'});
        saveAs(blob, `MojeEvidencije.xlsx`)
    }

    const exportExcel = async (item) => {
        const workbook = new ExcelJS.Workbook();
        const worksheet = workbook.addWorksheet(`Evidencija testiranja #${item.idTestiranja}`);
        const worksheetStavke = workbook.addWorksheet(`Stavke testiranja #${item.idTestiranja}`);

        worksheet.columns = [
            { header: 'ID', key: 'id', width: 30},
            { header: 'Datum', key: 'datum', width: 30},
            { header: 'Sportista', key: 'sportista', width: 30},
            { header: 'Broj Testova', key: 'brojTestova', width: 30},
            { header: 'Broj polozenih', key: 'brojPolozenih', width: 30},
            { header: 'Broj palih', key: 'brojPalih', width: 30},
            { header: 'Prosao testiranje', key: 'prosaoTestiranje', width: 30},
            { header: 'Rezultat testiranja', key: 'rezultatTestiranja', width: 30}
        ];

        worksheetStavke.columns = [
            { header: 'RB', key: 'rb', width: 30},
            { header: 'Naziv vežbe', key: 'nazivVezbe', width: 30},
            { header: 'Ostvareni rezultat', key: 'ostvareniRezultat', width: 30},
            { header: 'Norma', key: 'norma', width: 30},
            { header: 'Prošao test', key: 'prosaoTest', width: 30},
            { header: 'Komentar', key: 'komentar', width: 30},
        ];

        
        worksheet.addRow({
            id: item.idTestiranja,
            datum: item.datum,
            sportista: item.imeIPrezimeSportiste,
            brojTestova: item.brojTestova,
            brojPolozenih: item.brojPolozenih,
            brojPalih: item.brojPalih,
            prosaoTestiranje: item.prosaoTestiranje,
            rezultatTestiranja: item.rezultatTestiranja
        });
        const stavke = item.stavke;
        stavke.forEach((s) => {
            worksheetStavke.addRow({
                rb: s.rb,
                nazivVezbe: s.vezbaNaziv,
                ostvareniRezultat: s.ostvareniRezultat,
                norma: s.norma,
                prosaoTest: s.prosaoTest,
                komentar: s.komentar
            })
        })
        worksheet.getRow(1).eachCell((cell) => {
            cell.font = {size: 14,
                bold: true, 
                color: {argb: 'FFFFFF'}
            };
            cell.alignment = { horizontal: 'center'};
            cell.fill = {
                type: 'pattern',
                pattern: 'solid',
                fgColor : {argb: '1e3c72'}
            };
        });

        worksheet.eachRow((row, rowNum) => {
            if(rowNum === 1)
                return;
            row.eachCell((cell, colNum) => {
                cell.font = {size: 10};
                cell.alignment = {horizontal: 'center',
                    vertical: 'middle'
                };
                if(colNum === 7){
                    const vrednost = cell.value;
                    if(vrednost === true){
                        row.eachCell((cell1, colNum1) => {
                            if(colNum1 <= 8){
                                cell1.fill = {type: 'pattern',
                                    pattern: 'solid', 
                                    fgColor: {argb: 'D1E7DD'}
                                } 
                                cell1.border = {
                                    bottom: {style: 'thin', color: {argb:'0F5132'}}
                                }
                            }
                        })
                        
                        row.font = {color: 'black'}
                        cell.font = {bold: true, color: {argb: '0F5132'}}
                    }
                    else if(vrednost === false){
                        row.eachCell((cell1, colNum1) => {
                            cell1.fill = {type: 'pattern',
                                pattern: 'solid', 
                                fgColor: {argb: 'F8D7DA'}
                            } 
                            cell1.border = {
                                bottom: {style: 'thin', color: {argb:'842029'}}
                            }
                        })
                        
                        row.font = {color: 'black'}
                        cell.font = {bold: true, color: {argb: '842029'}}
                    }
                }
            });
        worksheetStavke.eachRow((row, rowNum) => {
            if(rowNum === 1)
                return;
            row.eachCell((cell, colNum) => {
                cell.font = {size: 10};
                cell.alignment = {horizontal: 'center',
                    vertical: 'middle'
                };
                if(colNum === 5){
                    const vrednost = cell.value;
                    if(vrednost === true){
                        row.eachCell((cell1, colNum1) => {
                            cell1.fill = {type: 'pattern',
                                pattern: 'solid', 
                                fgColor: {argb: 'D1E7DD'}
                            } 
                            cell1.border = {
                                bottom: {style: 'thin', color: {argb:'0F5132'}}
                            }
                        })
                        
                        row.font = {color: 'black'}
                        cell.font = {bold: true, color: {argb: '0F5132'}}
                    }
                    else if(vrednost === false){
                        row.eachCell((cell1, colNum1) => {
                            cell1.fill = {type: 'pattern',
                                pattern: 'solid', 
                                fgColor: {argb: 'F8D7DA'}
                            } 
                            cell1.border = {
                                bottom: {style: 'thin', color: {argb:'842029'}}
                            }
                        })
                        
                        row.font = {color: 'black'}
                        cell.font = {bold: true, color: {argb: '842029'}}
                    }
                }
            })
        })
        })

        worksheetStavke.getRow(1).eachCell((cell) => {
            cell.font = {bold: true, 
                color: {argb: 'FFFFFF'}
            };
            cell.alignment = { horizontal: 'center'};
            cell.fill = {
                type: 'pattern',
                pattern: 'solid',
                fgColor : {argb: '1e3c72'}
            };
        });

        const buffer = await workbook.xlsx.writeBuffer();
        const blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetxml.sheet'});
        saveAs(blob, `evidencija_${item.idTestiranja}.xlsx`)
    }

    useEffect(() => {
        const sacuvaniKorisnik = sessionStorage.getItem('korisnik');
        if(!sacuvaniKorisnik){
            setGreska('Korisnik nije prijavljen');
            return;
        }
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

    const getAuthHeaders = () => {
        const token = sessionStorage.getItem('token');
        return {
            'Content-Type': 'application/json',
            'Authorization': token ? `Bearer ${token}` : ''
        };
    };
    const fetchEvidencije = async (idTrenera) => {
        try{
            const response = await fetch(`http://localhost:8080/api/evidencije/trener/${idTrenera}`, {
                headers: getAuthHeaders()
            });
            if(!response.ok){
                if(response.status === 404){
                    setEvidencije([]);
                    return;
                }
                throw new Error("Neuspešno preuzimanje evidencija testiranja.");
                
            }
            const data = await response.json();
            setEvidencije(data);
            setTrenutnaStrana(1);
        } catch(err){
            console.error('Greska tokom fetch-ovanja evidencija:', err);
            setGreska(err.message);
        }

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
                ['Trener', `${korisnik.ime} ${korisnik.prezime}`],
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

    const handleObrisiEvidenciju = async (idTestiranja) => {
        try{
            const res = await fetch(`http://localhost:8080/api/evidencije/${idTestiranja}`,{
                method: 'DELETE',
                headers: getAuthHeaders()
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
            const res = await fetch('http://localhost:8080/api/sportisti', {
                headers: getAuthHeaders()
            });
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
                throw new Error("Greska pri pretrazi evidencija sa servirea");
                
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

    const handleReset = () => {
        setIdSportiste('');
        setDatum('');
        setProsaoTestiranje('');
        setRezultatTestiranja('');
        if(korisnik?.id){
            fetchEvidencije(korisnik.id);
        }
    }

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
        <div class='trener-container'>
            <NavBarTrener korisnik={korisnik}/>
            <main class='trener-content'>
                <section class="welcome-section">
                    <h1>Dobrodošli, {`${korisnik?.ime} `} </h1>
                    <p>Ovde možete upravljati evidencijama testiranja Vaših sportista.</p>
                </section>

                <section class='evidencije-section'>
                    <h2>Evidencije testiranja</h2>
                    <div class='btns-gornji'>
                        <button class='btn-dodaj-evidenciju' onClick={() => navigate('/evidencija/nova')}>➕Dodaj evidenciju</button>
                        <button class='btn-excel' onClick={() => exportExcelOfAll(evidencije)} title='Preuzmite Excel svih Vaših evidencija'>📄Excel sheet svih evidencija</button>
                    </div>
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
                        <button type='submit' class='btn-pretrazi1'>🔎Pretraži</button>
                        <button type='button' onClick={handleReset} class='btn-reset1'>Prikaži sve</button>
                    </form>
                    {greska && <div class='greska-message'>{greska}</div>}

                    {evidencije.length === 0 ? (
                        <p class='nema-evidencija-tekst'>{poruka || 'Trenutno nema zabeleženih testiranja u bazi.'}</p>
                        ): (
                            <>
                            <div class='tabela'>
                                <table class='evidencije-tabela'>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Sportista</th>
                                            <th>Rezultat testiranja</th>
                                            <th>Datum testiranja</th>
                                            <th>Izmeni/Excel/Obriši</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {currentToDos.map((item) => (
                                            <tr key={item.idTestiranja}
                                            class='red-evidencije'>
                                                <td 
                                            >#{item.idTestiranja}</td>
                                                <td>{item.imeIPrezimeSportiste}</td>
                                                <td class={`rezTestiranja ${item.prosaoTestiranje ? 'polozeno' : 'palo'}`}>{item.rezultatTestiranja}%</td>
                                                <td>{item.datum}</td>
                                                <td class='btns'><button onClick={() => navigate(`/evidencija/${item.idTestiranja}`)}>✏️</button>
                                                <button onClick={() => exportExcel(item)} title='Preuzmi Excel'>📄</button>
                                                <button class='btn-obrisi-evidenciju' onClick= {() => handleObrisiEvidenciju(item.idTestiranja)} title='Obriši evidenciju'>❌</button></td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                            {evidencije.length > evidencijaPoStrani && (
                                <ul id='page-numbers'>
                                    {renderPageNumbers}
                                </ul>
                            )}
                            </>
                    )}
                </section>
            </main>
        </div>
    )
}

