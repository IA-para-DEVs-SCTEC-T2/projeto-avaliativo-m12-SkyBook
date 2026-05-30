import { useSeatSelection } from '../../hooks/useSeatSelection';
import { useBooking } from '../../hooks/useBooking';
import SeatMapLayout from '../../templates/SeatMapLayout';
import SeatMap from '../../organisms/SeatMap';
import TotalPanel from '../../organisms/TotalPanel';
import ConfirmBookingModal from '../../organisms/ConfirmBookingModal';
import PassengerFormModal from '../../organisms/PassengerFormModal';

/**
 * SeatMapPage — página principal do mapa de poltronas da aeronave.
 *
 * Conecta o hook de seleção e o hook de reserva aos organismos de mapa,
 * painel de total e modais de confirmação.
 * Trata estados de loading e erro.
 */
function SeatMapPage() {
  const {
    seats,
    selectedIds,
    total,
    loading,
    error,
    toggleSeat,
    clearSelection,
    refetch,
  } = useSeatSelection();

  const { step, loading: bookingLoading, error: bookingError, openSummary, goToForm, cancel, confirm } =
    useBooking(handleBookingSuccess);

  /** Poltronas selecionadas como objetos completos (para exibir no modal). */
  const selectedSeats = seats.filter((s) => selectedIds.has(s.id));

  /** Códigos das poltronas selecionadas (para enviar ao backend). */
  const selectedCodes = selectedSeats.map((s) => s.code);

  /** Após reserva bem-sucedida: limpa seleção e recarrega o mapa. */
  function handleBookingSuccess() {
    clearSelection();
    refetch();
  }

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
    <>
      <SeatMapLayout
        header={header}
        panel={
          <TotalPanel
            total={total}
            count={selectedIds.size}
            onBook={openSummary}
          />
        }
        map={<SeatMap seats={seats} selectedIds={selectedIds} onToggle={toggleSeat} />}
      />

      {/* Modal 1 — Resumo das poltronas selecionadas */}
      <ConfirmBookingModal
        open={step === 'summary'}
        onClose={cancel}
        onContinue={goToForm}
        selectedSeats={selectedSeats}
        total={total}
      />

      {/* Modal 2 — Dados do passageiro */}
      <PassengerFormModal
        open={step === 'form'}
        onClose={cancel}
        onConfirm={(name, email) => confirm(name, email, selectedCodes)}
        loading={bookingLoading}
        error={bookingError}
      />
    </>
  );
}

export default SeatMapPage;
