/**
 * SeatMapLayout — template de layout da tela de mapa de poltronas.
 *
 * Define a estrutura visual com painel lateral esquerdo (total)
 * e área principal direita (mapa de assentos), sem dados reais.
 *
 * @param {object} props
 * @param {React.ReactNode} props.panel - Conteúdo do painel lateral (TotalPanel).
 * @param {React.ReactNode} props.map - Conteúdo da área principal (SeatMap).
 * @param {React.ReactNode} [props.header] - Cabeçalho opcional da página.
 */
function SeatMapLayout({ panel, map, header }) {
  return (
    <div
      style={{
        minHeight: '100vh',
        backgroundColor: '#f1f5f9',
        padding: '24px',
        fontFamily: "'Segoe UI', system-ui, sans-serif",
      }}
    >
      {header && (
        <header style={{ marginBottom: '24px', textAlign: 'center' }}>
          {header}
        </header>
      )}

      <div
        style={{
          display: 'flex',
          gap: '32px',
          alignItems: 'flex-start',
          justifyContent: 'center',
          flexWrap: 'wrap',
        }}
      >
        {/* Painel lateral esquerdo — total */}
        <div style={{ flexShrink: 0 }}>{panel}</div>

        {/* Área principal — mapa de poltronas */}
        <div style={{ flexShrink: 0 }}>{map}</div>
      </div>
    </div>
  );
}

export default SeatMapLayout;
