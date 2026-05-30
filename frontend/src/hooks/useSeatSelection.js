import { useState, useEffect, useCallback } from 'react';
import { fetchSeats } from '../services/seatsService';

/**
 * Hook que gerencia o carregamento das poltronas, seleção e cálculo do total.
 *
 * @returns {{
 *   seats: Array,
 *   selectedIds: Set<number>,
 *   total: number,
 *   loading: boolean,
 *   error: string|null,
 *   toggleSeat: (seat: object) => void,
 *   clearSelection: () => void,
 *   refetch: () => void
 * }}
 */
export function useSeatSelection() {
  const [seats, setSeats] = useState([]);
  const [selectedIds, setSelectedIds] = useState(new Set());
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadSeats = useCallback(() => {
    setLoading(true);
    setError(null);
    fetchSeats()
      .then(setSeats)
      .catch(() => setError('Não foi possível carregar as poltronas. Tente novamente mais tarde.'))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    loadSeats();
  }, [loadSeats]);

  /**
   * Seleciona ou deseleciona uma poltrona disponível.
   * Poltronas indisponíveis são ignoradas.
   * @param {object} seat - Objeto da poltrona.
   */
  function toggleSeat(seat) {
    if (!seat.available) return;

    setSelectedIds((prev) => {
      const next = new Set(prev);
      if (next.has(seat.id)) {
        next.delete(seat.id);
        setTotal((t) => t - seat.price);
      } else {
        next.add(seat.id);
        setTotal((t) => t + seat.price);
      }
      return next;
    });
  }

  /** Limpa toda a seleção e zera o total. */
  function clearSelection() {
    setSelectedIds(new Set());
    setTotal(0);
  }

  /** Recarrega a lista de poltronas do backend. */
  function refetch() {
    loadSeats();
  }

  return { seats, selectedIds, total, loading, error, toggleSeat, clearSelection, refetch };
}
