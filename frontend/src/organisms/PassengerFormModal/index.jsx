import { useState } from 'react';
import Modal from '../../atoms/Modal';

/**
 * PassengerFormModal — organismo que exibe o Modal 2 do fluxo de reserva.
 *
 * Coleta nome e e-mail do passageiro para confirmar a reserva.
 * Exibe mensagem de erro caso a API retorne falha.
 *
 * @param {object} props
 * @param {boolean} props.open - Controla a visibilidade do modal.
 * @param {function} props.onClose - Callback para fechar/cancelar.
 * @param {function} props.onConfirm - Callback com (name, email) ao confirmar.
 * @param {boolean} props.loading - Indica se a requisição está em andamento.
 * @param {string|null} props.error - Mensagem de erro da API, se houver.
 */
function PassengerFormModal({ open, onClose, onConfirm, loading, error }) {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    if (name.trim() && email.trim()) {
      onConfirm(name.trim(), email.trim());
    }
  }

  function handleClose() {
    setName('');
    setEmail('');
    onClose();
  }

  const inputStyle = {
    width: '100%',
    padding: '10px 12px',
    borderRadius: '8px',
    border: '1px solid #cbd5e1',
    fontSize: '14px',
    color: '#1e293b',
    outline: 'none',
    boxSizing: 'border-box',
  };

  const labelStyle = {
    display: 'block',
    fontSize: '13px',
    fontWeight: 600,
    color: '#475569',
    marginBottom: '6px',
  };

  return (
    <Modal open={open} onClose={handleClose} title="Confirmar Reserva?">
      <form onSubmit={handleSubmit}>
        {/* Campo Nome */}
        <div style={{ marginBottom: '16px' }}>
          <label htmlFor="passenger-name" style={labelStyle}>
            Nome completo
          </label>
          <input
            id="passenger-name"
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Ex: João Silva"
            required
            style={inputStyle}
            autoFocus
          />
        </div>

        {/* Campo E-mail */}
        <div style={{ marginBottom: '24px' }}>
          <label htmlFor="passenger-email" style={labelStyle}>
            E-mail
          </label>
          <input
            id="passenger-email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Ex: joao@email.com"
            required
            style={inputStyle}
          />
        </div>

        {/* Mensagem de erro */}
        {error && (
          <div
            role="alert"
            style={{
              padding: '10px 14px',
              backgroundColor: '#fef2f2',
              border: '1px solid #fecaca',
              borderRadius: '8px',
              color: '#ef4444',
              fontSize: '13px',
              marginBottom: '16px',
            }}
          >
            ⚠ {error}
          </div>
        )}

        {/* Botões */}
        <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
          <button
            type="button"
            onClick={handleClose}
            disabled={loading}
            style={{
              padding: '10px 20px',
              borderRadius: '8px',
              border: '1px solid #cbd5e1',
              backgroundColor: '#fff',
              color: '#475569',
              fontSize: '14px',
              cursor: loading ? 'not-allowed' : 'pointer',
              fontWeight: 500,
            }}
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={loading || !name.trim() || !email.trim()}
            style={{
              padding: '10px 24px',
              borderRadius: '8px',
              border: 'none',
              backgroundColor: loading ? '#94a3b8' : '#1e293b',
              color: '#fff',
              fontSize: '14px',
              cursor: loading || !name.trim() || !email.trim() ? 'not-allowed' : 'pointer',
              fontWeight: 600,
            }}
          >
            {loading ? 'Confirmando...' : 'Confirmar Reserva'}
          </button>
        </div>
      </form>
    </Modal>
  );
}

export default PassengerFormModal;
