import SeatCard from '../../molecules/SeatCard';

const COLUMNS = ['A', 'B', 'C', 'D', 'E', 'F'];
const ROWS = 10;

/** Largura fixa de cada célula (poltrona ou header de coluna). */
const CELL_SIZE = 64;

/** Largura do corredor central entre colunas C e D. */
const AISLE_WIDTH = 24;

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
        {/* Espaço reservado para o número da fileira */}
        <div style={{ width: '32px', flexShrink: 0 }} />

        {COLUMNS.map((col, i) => (
          <>
            {/* Corredor entre C e D — elemento irmão, não dentro do span da coluna */}
            {i === 3 && (
              <div key="aisle-header" style={{ width: `${AISLE_WIDTH}px`, flexShrink: 0 }} />
            )}
            <div
              key={col}
              style={{
                width: `${CELL_SIZE}px`,
                flexShrink: 0,
                textAlign: 'center',
                fontWeight: 700,
                fontSize: '13px',
                color: '#64748b',
              }}
            >
              {col}
            </div>
          </>
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
            <div
              style={{
                width: '32px',
                flexShrink: 0,
                textAlign: 'right',
                fontWeight: 700,
                fontSize: '13px',
                color: '#64748b',
              }}
            >
              {rowNum}
            </div>

            {COLUMNS.map((col, colIdx) => {
              const code = `${rowNum}${col}`;
              const seat = seatByCode[code];

              return (
                <>
                  {/* Corredor entre C e D — elemento irmão, não dentro do wrapper da célula */}
                  {colIdx === 3 && (
                    <div
                      key={`aisle-${rowNum}`}
                      style={{ width: `${AISLE_WIDTH}px`, flexShrink: 0 }}
                    />
                  )}
                  <div
                    key={col}
                    style={{ width: `${CELL_SIZE}px`, height: `${CELL_SIZE}px`, flexShrink: 0 }}
                  >
                    {seat ? (
                      <SeatCard
                        seat={seat}
                        selected={selectedIds.has(seat.id)}
                        onToggle={onToggle}
                      />
                    ) : null}
                  </div>
                </>
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
