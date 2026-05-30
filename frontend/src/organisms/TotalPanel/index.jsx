import MoneyValue from '../../atoms/MoneyValue';

/**
 * TotalPanel — organismo que exibe o painel lateral com o valor total acumulado.
 *
 * Mostra o total em destaque, a contagem de poltronas selecionadas
 * e o botão "Realizar Reserva" (habilitado somente com ao menos uma poltrona selecionada).
 *
 * @param {object} props
 * @param {number} props.total - Valor total acumulado das poltronas selecionadas.
 * @param {number} props.count - Quantidade de poltronas selecionadas.
 * @param {function} props.onBook - Callback ao clicar em "Realizar Reserva".
 * @param {function} props.onConsult - Callback ao clicar em "Consultar Reservas".
 */
function TotalPanel({ total, count, onBook, onConsult }) {
  return (
    <aside
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: '12px',
        padding: '32px 24px',
        backgroundColor: '#1e293b',
        borderRadius: '16px',
        minWidth: '200px',
        color: '#fff',
        position: 'sticky',
        top: '24px',
      }}
    >
      <span style={{ fontSize: '13px', color: '#94a3b8', textTransform: 'uppercase', letterSpacing: '1px' }}>
        Total
      </span>

      <MoneyValue value={total} fontSize="2.4rem" color="#f8fafc" bold />

      <div
        style={{
          width: '100%',
          height: '1px',
          backgroundColor: '#334155',
          margin: '4px 0',
        }}
      />

      <span style={{ fontSize: '13px', color: '#94a3b8' }}>
        {count === 0
          ? 'Nenhuma poltrona selecionada'
          : `${count} poltrona${count > 1 ? 's' : ''} selecionada${count > 1 ? 's' : ''}`}
      </span>

      {count > 0 && (
        <span style={{ fontSize: '11px', color: '#64748b' }}>
          Clique novamente para desselecionar
        </span>
      )}

      {/* Botão Realizar Reserva */}
      <button
        onClick={onBook}
        disabled={count === 0}
        aria-label="Realizar reserva das poltronas selecionadas"
        style={{
          marginTop: '8px',
          width: '100%',
          padding: '12px 16px',
          borderRadius: '10px',
          border: 'none',
          backgroundColor: count === 0 ? '#334155' : '#3b82f6',
          color: count === 0 ? '#64748b' : '#fff',
          fontSize: '14px',
          fontWeight: 700,
          cursor: count === 0 ? 'not-allowed' : 'pointer',
          transition: 'background-color 0.2s',
          letterSpacing: '0.3px',
        }}
      >
        Realizar Reserva
      </button>

      {/* Botão Consultar Reservas */}
      <button
        onClick={onConsult}
        aria-label="Consultar reservas realizadas"
        style={{
          width: '100%',
          padding: '10px 16px',
          borderRadius: '10px',
          border: '1px solid #475569',
          backgroundColor: 'transparent',
          color: '#94a3b8',
          fontSize: '13px',
          fontWeight: 600,
          cursor: 'pointer',
          transition: 'background-color 0.2s, color 0.2s',
          letterSpacing: '0.3px',
        }}
        onMouseEnter={(e) => {
          e.currentTarget.style.backgroundColor = '#334155';
          e.currentTarget.style.color = '#f1f5f9';
        }}
        onMouseLeave={(e) => {
          e.currentTarget.style.backgroundColor = 'transparent';
          e.currentTarget.style.color = '#94a3b8';
        }}
      >
        Consultar Reservas
      </button>
    </aside>
  );
}

export default TotalPanel;
