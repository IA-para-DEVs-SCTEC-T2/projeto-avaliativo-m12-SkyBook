import MoneyValue from '../../atoms/MoneyValue';

/**
 * SeatCard — molécula que representa uma poltrona individual no mapa.
 *
 * Estados visuais:
 * - disponível → verde (clicável)
 * - indisponível → vermelho (não clicável)
 * - selecionado → azul (clicável para desselecionar)
 *
 * @param {object} props
 * @param {object} props.seat - Dados da poltrona { id, code, price, available }.
 * @param {boolean} props.selected - Se a poltrona está selecionada.
 * @param {function} props.onToggle - Callback ao clicar na poltrona.
 */
function SeatCard({ seat, selected, onToggle }) {
  const getBackground = () => {
    if (!seat.available) return '#ef4444';
    if (selected) return '#3b82f6';
    return '#22c55e';
  };

  const cursor = seat.available ? 'pointer' : 'not-allowed';

  return (
    <button
      onClick={() => onToggle(seat)}
      disabled={!seat.available}
      aria-label={`Poltrona ${seat.code} — ${seat.available ? (selected ? 'selecionada' : 'disponível') : 'indisponível'}`}
      aria-pressed={selected}
      style={{
        width: '64px',
        height: '64px',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '2px',
        backgroundColor: getBackground(),
        border: selected ? '2px solid #1d4ed8' : '2px solid transparent',
        borderRadius: '6px',
        cursor,
        color: '#fff',
        padding: '4px',
        transition: 'transform 0.1s, opacity 0.1s',
        opacity: seat.available ? 1 : 0.75,
      }}
      onMouseEnter={(e) => {
        if (seat.available) e.currentTarget.style.transform = 'scale(1.08)';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.transform = 'scale(1)';
      }}
    >
      <span style={{ fontSize: '11px', fontWeight: 700 }}>{seat.code}</span>
      <MoneyValue value={seat.price} fontSize="10px" color="#fff" />
    </button>
  );
}

export default SeatCard;
