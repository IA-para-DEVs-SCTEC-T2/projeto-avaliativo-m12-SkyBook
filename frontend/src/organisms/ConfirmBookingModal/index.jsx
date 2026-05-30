import Modal from '../../atoms/Modal';
import MoneyValue from '../../atoms/MoneyValue';

/**
 * ConfirmBookingModal — organismo que exibe o Modal 1 do fluxo de reserva.
 *
 * Apresenta a lista de poltronas selecionadas com seus valores individuais
 * e o valor total, com opções de continuar ou cancelar.
 *
 * @param {object} props
 * @param {boolean} props.open - Controla a visibilidade do modal.
 * @param {function} props.onClose - Callback para fechar/cancelar.
 * @param {function} props.onContinue - Callback para avançar ao Modal 2.
 * @param {Array} props.selectedSeats - Lista de poltronas selecionadas { id, code, price }.
 * @param {number} props.total - Valor total acumulado.
 */
function ConfirmBookingModal({ open, onClose, onContinue, selectedSeats, total }) {
  return (
    <Modal open={open} onClose={onClose} title="Confirmar Reserva?">
      {/* Lista de poltronas */}
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          gap: '8px',
          marginBottom: '20px',
          maxHeight: '240px',
          overflowY: 'auto',
        }}
      >
        {selectedSeats.map((seat) => (
          <div
            key={seat.id}
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
              Poltrona {seat.code}
            </span>
            <MoneyValue value={seat.price} fontSize="14px" color="#475569" />
          </div>
        ))}
      </div>

      {/* Divisor + Total */}
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          padding: '12px 0',
          borderTop: '2px solid #e2e8f0',
          marginBottom: '24px',
        }}
      >
        <span style={{ fontWeight: 700, fontSize: '15px', color: '#1e293b' }}>Total</span>
        <MoneyValue value={total} fontSize="1.2rem" color="#1e293b" bold />
      </div>

      {/* Botões */}
      <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
        <button
          onClick={onClose}
          style={{
            padding: '10px 20px',
            borderRadius: '8px',
            border: '1px solid #cbd5e1',
            backgroundColor: '#fff',
            color: '#475569',
            fontSize: '14px',
            cursor: 'pointer',
            fontWeight: 500,
          }}
        >
          Cancelar
        </button>
        <button
          onClick={onContinue}
          style={{
            padding: '10px 24px',
            borderRadius: '8px',
            border: 'none',
            backgroundColor: '#1e293b',
            color: '#fff',
            fontSize: '14px',
            cursor: 'pointer',
            fontWeight: 600,
          }}
        >
          Continuar
        </button>
      </div>
    </Modal>
  );
}

export default ConfirmBookingModal;
