import { useState } from 'react';
import './Login.css';
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [tipKorisnika, setTipKorisnika] = useState('TRENER');

  const [korisnickoIme, setKorisnickoIme] = useState('');
  const [sifra, setSifra] = useState('');
  const [greska, setGreska] = useState('');

  const navigate = useNavigate();

  const promeniUlogu = () => {
    setTipKorisnika((prethodna) => (prethodna === 'TRENER' ? 'SPORTISTA' : 'TRENER'));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setGreska('');

    try{
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          korisnickoIme: korisnickoIme,
          sifra: sifra,
          tipKorisnika: tipKorisnika,
        }),
      });

      const data = await response.json();

      if(!response.ok){
        throw new Error(data.message || 'Neuspešna prijava.');
        
      }

      localStorage.setItem('korisnik', JSON.stringify(data));
      if(tipKorisnika === 'TRENER'){
        navigate('/trener-main');
      }else{
        navigate('/sportista-main');
      }
    }catch(err){
      setGreska(err.message);
    }
    const loginData = {
      korisnickoIme,
      sifra,
      tipKorisnika, 
    };
    console.log('Podaci za prijavu:', loginData);
  };

  const isTrener = tipKorisnika === 'TRENER';
  const temaKlasa = isTrener ? 'trener-tema' : 'sportista-tema';
  const dugmeKlasa = isTrener ? 'trener-btn' : 'sportista-btn';
  const tekstKlasa = isTrener ? 'trener-tekst' : 'sportista-tekst';


  return (
    <div class = {`login-container ${temaKlasa}`}>
      <div class = 'login-card'>
        <h2 class = {`login-title ${isTrener ? 'trener-tekst' : 'sportista-tekst' }`}>
          {isTrener ? 'Prijava za trenere' : ' Prijava za sportiste'}
        </h2>
        <p class = 'login-subtitle'>
          Prijavite se na sistem za evidencije testiranja
        </p>
        {greska && (
          <div class='greska-message'>
            {greska}
          </div>
        )}

        <form onSubmit = {handleSubmit}>
          <div class='input-group'>
            <label class = 'input-label'>Korisničko ime</label>
            <input type = 'text' class = 'login-input' value = {korisnickoIme} onChange={(e) => setKorisnickoIme(e.target.value)} placeholder = "Unesite korisničko ime" required/>
          </div>
          
          <div class = 'input-group'>
            <label class = 'input-label'>Šifra</label>
            <input type = 'password' class = 'login-input' value={sifra} onChange={(e) => setSifra(e.target.value)} placeholder='Unesite šifru' required/>
          </div>
          
          <button type='submit' class = {`submit-btn ${dugmeKlasa}`}>
            Prijavi se
          </button>

        </form>

        <div class = 'login-divider'></div>

        <button type='button' onClick={promeniUlogu} class = {`switch-btn ${dugmeKlasa}`}>
          {isTrener ? 'Prijavi se kao sportista' : 'Prijavi se kao trener'}
        </button>

      </div>
    </div>
  );
}