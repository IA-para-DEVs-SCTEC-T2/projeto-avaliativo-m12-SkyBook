import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import SeatMapPage from './pages/SeatMapPage';

/**
 * App — componente raiz da aplicação SkyBook.
 *
 * Define o roteamento da aplicação:
 * - /         → redireciona para /skybook
 * - /skybook  → tela de listagem e seleção de poltronas
 */
function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/skybook" replace />} />
        <Route path="/skybook" element={<SeatMapPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
