import SeatCard from '../../molecules/SeatCard';

const COLUMNS = ['A', 'B', 'C', 'D', 'E', 'F'];
const ROWS = 10;

/**
 * SeatMap — organismo que renderiza a silhueta do avião com o grid de poltronas.
 *
 * Organiza 60 poltronas em 10 fileiras × 6 colunas (A–F).
 * Exibe separador visual entre colunas C e D simulando o corredor central.
 *
 * @param {object} props
 * @param {Array} props.seats - Lista de poltronas da API.
 * @param {Set<number>} props.selectedIds - IDs das poltronas selecionadas.
 * @param {function} props.onToggle - Callback ao clicar em uma poltrona.
 */
function SeatMap({ seats, selectedIds, onToggle }) {
  // Indexa poltronas por código para acesso O(1)
  const seatByCode = {};
  seats.forEach((s) => {
    seatByCode[s.code] = s;
  });

  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '8px',
        padding: '24px',
        backgroundColor: '#f8fafc',
        borderRadius: '16px',
        border: '2px solid #e2e8f0',
        width: 'fit-content',
      }}
    >
      {/* Cabeçalho das colunas */}
      <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
        <span style={{ width: '32px' }} />
        {COLUMNS.map((col, i) => (
          <span key={col}>
            {i === 3 && <span style={{ width: '24px', display: 'inline-block' }} />}
            <span
              style={{
                width: '64px',
                textAlign: 'center',
                fontWeight: 700,
                fontSize: '13px',
                color: '#64748b',
                display: 'inline-block',
              }}
            >
              {col}
            </span>
          </span>
        ))}
      </div>

      {/* Fileiras */}
      {Array.from({ length: ROWS }, (_, rowIdx) => {
        const rowNum = rowIdx + 1;
        return (
          <div
            key={rowNum}
            style={{ display: 'flex', gap: '8px', alignItems: 'center' }}
          >
            {/* Número da fileira */}
            <span
              style={{
                width: '32px',
                textAlign: 'right',
                fontWeight: 700,
                fontSize: '13px',
                color: '#64748b',
              }}
            >
              {rowNum}
            </span>

            {COLUMNS.map((col, colIdx) => {
              const code = `${rowNum}${col}`;
              const seat = seatByCode[code];

              return (
                <span key={col}>
                  {/* Corredor entre C e D */}
                  {colIdx === 3 && (
                    <span style={{ width: '24px', display: 'inline-block' }} />
                  )}
                  {seat ? (
                    <SeatCard
                      seat={seat}
                      selected={selectedIds.has(seat.id)}
                      onToggle={onToggle}
                    />
                  ) : (
                    <span style={{ width: '64px', height: '64px', display: 'inline-block' }} />
                  )}
                </span>
              );
            })}
          </div>
        );
      })}

      {/* Legenda */}
      <div
        style={{
          display: 'flex',
          gap: '16px',
          marginTop: '12px',
          fontSize: '12px',
          color: '#475569',
        }}
      >
        <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
          <span style={{ width: '14px', height: '14px', borderRadius: '3px', backgroundColor: '#22c55e', display: 'inline-block' }} />
          Disponível
        </span>
        <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
          <span style={{ width: '14px', height: '14px', borderRadius: '3px', backgroundColor: '#ef4444', display: 'inline-block' }} />
          Indisponível
        </span>
        <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
          <span style={{ width: '14px', height: '14px', borderRadius: '3px', backgroundColor: '#3b82f6', display: 'inline-block' }} />
          Selecionado
        </span>
      </div>
    </div>
  );
}

export default SeatMap;
