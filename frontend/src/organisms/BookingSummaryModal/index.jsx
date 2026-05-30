import { useState } from 'react';
import Modal from '../../atoms/Modal';
import MoneyValue from '../../atoms/MoneyValue';
import { getBookingSummary } from '../../services/bookingService';

/**
 * BookingSummaryModal — organismo que exibe o resumo consolidado das reservas de um passageiro.
 *
 * Apresenta um campo de e-mail e botão "Buscar" no topo.
 * Após a busca, exibe a lista de poltronas reservadas com código, preço individual e valor total.
 * Trata os estados de loading, erro de comunicação e e-mail não encontrado (404).
 *
 * @param {object} props
 * @param {boolean} props.open - Controla a visibilidade do modal.
 * @param {function} props.onClose - Callback para fechar o modal.
 */
function BookingSummaryModal({ open, onClose }) {
  const [email, setEmail] = useState('');
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [notFound, setNotFound] = useState(false);

  /** Reseta o estado interno ao fechar o modal. */
  function handleClose() {
    setEmail('');
    setSummary(null);
    setError(null);
    setNotFound(false);
    onClose();
  }

  /** Busca o resumo de reservas pelo e-mail informado. */
  async function handleSearch() {
    if (!email.trim()) return;

    setLoading(true);
    setError(null);
    setNotFound(false);
    setSummary(null);

    try {
      const data = await getBookingSummary(email.trim());
      setSummary(data);
    } catch (err) {
      if (err.response?.status === 404) {
        setNotFound(true);
      } else {
        setError('Não foi possível buscar as reservas. Tente novamente.');
      }
    } finally {
      setLoading(false);
    }
  }

  /** Permite buscar ao pressionar Enter no campo de e-mail. */
  function handleKeyDown(e) {
    if (e.key === 'Enter') handleSearch();
  }

  return (
    <Modal open={open} onClose={handleClose} title="Consultar Reservas">
      {/* Campo de e-mail + botão Buscar */}
      <div style={{ display: 'flex', gap: '8px', marginBottom: '20px' }}>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Digite seu e-mail"
          aria-label="E-mail do passageiro"
          style={{
            flex: 1,
            padding: '10px 14px',
            borderRadius: '8px',
            border: '1px solid #cbd5e1',
            fontSize: '14px',
            color: '#1e293b',
            outline: 'none',
          }}
        />
        <button
          onClick={handleSearch}
          disabled={loading || !email.trim()}
          aria-label="Buscar reservas"
          style={{
            padding: '10px 18px',
            borderRadius: '8px',
            border: 'none',
            backgroundColor: loading || !email.trim() ? '#94a3b8' : '#3b82f6',
            color: '#fff',
            fontSize: '14px',
            fontWeight: 600,
            cursor: loading || !email.trim() ? 'not-allowed' : 'pointer',
            whiteSpace: 'nowrap',
          }}
        >
          {loading ? 'Buscando...' : 'Buscar'}
        </button>
      </div>

      {/* Estado: e-mail não encontrado */}
      {notFound && (
        <div
          role="alert"
          style={{
            padding: '16px',
            backgroundColor: '#fef9c3',
            borderRadius: '8px',
            border: '1px solid #fde047',
            color: '#854d0e',
            fontSize: '14px',
            textAlign: 'center',
          }}
        >
          Nenhuma reserva encontrada para o e-mail informado.
        </div>
      )}

      {/* Estado: erro de comunicação */}
      {error && (
        <div
          role="alert"
          style={{
            padding: '16px',
            backgroundColor: '#fef2f2',
            borderRadius: '8px',
            border: '1px solid #fecaca',
            color: '#b91c1c',
            fontSize: '14px',
            textAlign: 'center',
          }}
        >
          ⚠ {error}
        </div>
      )}

      {/* Estado: resumo carregado */}
      {summary && (
        <div>
          {/* Cabeçalho do passageiro */}
          <div style={{ marginBottom: '16px' }}>
            <p style={{ margin: '0 0 4px', fontSize: '15px', fontWeight: 700, color: '#1e293b' }}>
              {summary.passengerName}
            </p>
            <p style={{ margin: 0, fontSize: '13px', color: '#64748b' }}>
              {summary.passengerEmail}
            </p>
          </div>

          {/* Lista de poltronas */}
          <div
            style={{
              display: 'flex',
              flexDirection: 'column',
              gap: '8px',
              marginBottom: '16px',
              maxHeight: '220px',
              overflowY: 'auto',
            }}
          >
            {summary.bookings.map((booking) => (
              <div
                key={booking.bookingId}
                style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  padding: '10px 14px',
                  backgroundColor: '#f8fafc',
                  borderRadius: '8px',
                  border: '1px solid #e2e8f0',
                }}
              >
                <span style={{ fontWeight: 600, color: '#1e293b', fontSize: '14px' }}>
                  Poltrona {booking.seatCode}
                </span>
                <MoneyValue value={booking.seatPrice} fontSize="14px" color="#475569" />
              </div>
            ))}
          </div>

          {/* Total */}
          <div
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              padding: '12px 0',
              borderTop: '2px solid #e2e8f0',
            }}
          >
            <span style={{ fontWeight: 700, fontSize: '15px', color: '#1e293b' }}>Total</span>
            <MoneyValue value={summary.totalAmount} fontSize="1.2rem" color="#1e293b" bold />
          </div>
        </div>
      )}
    </Modal>
  );
}

export default BookingSummaryModal;
