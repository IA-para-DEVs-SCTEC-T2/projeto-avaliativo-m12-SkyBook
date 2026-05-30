import { useState, useEffect } from 'react';
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
 *   toggleSeat: (seat: object) => void
 * }}
 */
export function useSeatSelection() {
  const [seats, setSeats] = useState([]);
  const [selectedIds, setSelectedIds] = useState(new Set());
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchSeats()
      .then(setSeats)
      .catch(() => setError('Não foi possível carregar as poltronas. Tente novamente mais tarde.'))
      .finally(() => setLoading(false));
  }, []);

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

  return { seats, selectedIds, total, loading, error, toggleSeat };
}
