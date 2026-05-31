/**
 * StatusBadge — átomo que exibe um badge visual de status de poltrona.
 *
 * @param {object} props
 * @param {'available'|'unavailable'|'selected'} props.status - Status da poltrona.
 */
function StatusBadge({ status }) {
  const labels = {
    available: 'Disponível',
    unavailable: 'Indisponível',
    selected: 'Selecionado',
  };

  const colors = {
    available: '#22c55e',
    unavailable: '#ef4444',
    selected: '#3b82f6',
  };

  return (
    <span
      style={{
        display: 'inline-block',
        padding: '2px 8px',
        borderRadius: '12px',
        fontSize: '11px',
        fontWeight: 600,
        color: '#fff',
        backgroundColor: colors[status] ?? '#6b7280',
      }}
    >
      {labels[status] ?? status}
    </span>
  );
}

export default StatusBadge;
