import MoneyValue from '../../atoms/MoneyValue';

/**
 * TotalPanel — organismo que exibe o painel lateral com o valor total acumulado.
 *
 * Mostra o total em destaque e a contagem de poltronas selecionadas.
 *
 * @param {object} props
 * @param {number} props.total - Valor total acumulado das poltronas selecionadas.
 * @param {number} props.count - Quantidade de poltronas selecionadas.
 */
function TotalPanel({ total, count }) {
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
    </aside>
  );
}

export default TotalPanel;
