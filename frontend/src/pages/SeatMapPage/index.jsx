import { useSeatSelection } from '../../hooks/useSeatSelection';
import SeatMapLayout from '../../templates/SeatMapLayout';
import SeatMap from '../../organisms/SeatMap';
import TotalPanel from '../../organisms/TotalPanel';

/**
 * SeatMapPage — página principal do mapa de poltronas da aeronave.
 *
 * Conecta o hook de seleção aos organismos de mapa e painel de total.
 * Trata estados de loading e erro.
 */
function SeatMapPage() {
  const { seats, selectedIds, total, loading, error, toggleSeat } = useSeatSelection();

  const header = (
    <div>
      <h1 style={{ margin: 0, fontSize: '1.8rem', color: '#1e293b', fontWeight: 700 }}>
        ✈ SkyBook
      </h1>
      <p style={{ margin: '4px 0 0', color: '#64748b', fontSize: '14px' }}>
        Selecione suas poltronas
      </p>
    </div>
  );

  if (loading) {
    return (
      <SeatMapLayout
        header={header}
        panel={<div />}
        map={
          <div style={{ padding: '48px', textAlign: 'center', color: '#64748b', fontSize: '16px' }}>
            Carregando poltronas...
          </div>
        }
      />
    );
  }

  if (error) {
    return (
      <SeatMapLayout
        header={header}
        panel={<div />}
        map={
          <div
            style={{
              padding: '32px',
              textAlign: 'center',
              color: '#ef4444',
              backgroundColor: '#fef2f2',
              borderRadius: '12px',
              border: '1px solid #fecaca',
              fontSize: '15px',
            }}
          >
            ⚠ {error}
          </div>
        }
      />
    );
  }

  return (
    <SeatMapLayout
      header={header}
      panel={<TotalPanel total={total} count={selectedIds.size} />}
      map={<SeatMap seats={seats} selectedIds={selectedIds} onToggle={toggleSeat} />}
    />
  );
}

export default SeatMapPage;
