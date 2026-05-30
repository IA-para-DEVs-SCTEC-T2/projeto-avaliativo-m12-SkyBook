import { renderHook, act } from '@testing-library/react';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { useSeatSelection } from './useSeatSelection';
import * as seatsService from '../services/seatsService';

vi.mock('../services/seatsService');

const mockSeats = [
  { id: 1, code: '1A', price: 198.89, available: true },
  { id: 2, code: '1B', price: 198.89, available: false },
  { id: 3, code: '1C', price: 149.90, available: true },
];

describe('useSeatSelection', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('inicia com loading true, seats vazio e total zero', async () => {
    seatsService.fetchSeats.mockResolvedValue(mockSeats);
    const { result } = renderHook(() => useSeatSelection());

    expect(result.current.loading).toBe(true);
    expect(result.current.seats).toEqual([]);
    expect(result.current.total).toBe(0);
    expect(result.current.error).toBeNull();
  });

  it('carrega poltronas e finaliza loading', async () => {
    seatsService.fetchSeats.mockResolvedValue(mockSeats);
    const { result } = renderHook(() => useSeatSelection());

    await act(async () => {});

    expect(result.current.loading).toBe(false);
    expect(result.current.seats).toEqual(mockSeats);
    expect(result.current.error).toBeNull();
  });

  it('define mensagem de erro quando a API falha', async () => {
    seatsService.fetchSeats.mockRejectedValue(new Error('Network Error'));
    const { result } = renderHook(() => useSeatSelection());

    await act(async () => {});

    expect(result.current.loading).toBe(false);
    expect(result.current.error).toBe(
      'Não foi possível carregar as poltronas. Tente novamente mais tarde.'
    );
  });

  it('seleciona poltrona disponível e soma ao total', async () => {
    seatsService.fetchSeats.mockResolvedValue(mockSeats);
    const { result } = renderHook(() => useSeatSelection());
    await act(async () => {});

    act(() => {
      result.current.toggleSeat(mockSeats[0]);
    });

    expect(result.current.selectedIds.has(1)).toBe(true);
    expect(result.current.total).toBeCloseTo(198.89);
  });

  it('deseleciona poltrona já selecionada e subtrai do total', async () => {
    seatsService.fetchSeats.mockResolvedValue(mockSeats);
    const { result } = renderHook(() => useSeatSelection());
    await act(async () => {});

    act(() => { result.current.toggleSeat(mockSeats[0]); });
    act(() => { result.current.toggleSeat(mockSeats[0]); });

    expect(result.current.selectedIds.has(1)).toBe(false);
    expect(result.current.total).toBeCloseTo(0);
  });

  it('seleciona múltiplas poltronas e acumula total', async () => {
    seatsService.fetchSeats.mockResolvedValue(mockSeats);
    const { result } = renderHook(() => useSeatSelection());
    await act(async () => {});

    act(() => { result.current.toggleSeat(mockSeats[0]); });
    act(() => { result.current.toggleSeat(mockSeats[2]); });

    expect(result.current.selectedIds.size).toBe(2);
    expect(result.current.total).toBeCloseTo(198.89 + 149.90);
  });

  it('ignora clique em poltrona indisponível', async () => {
    seatsService.fetchSeats.mockResolvedValue(mockSeats);
    const { result } = renderHook(() => useSeatSelection());
    await act(async () => {});

    act(() => { result.current.toggleSeat(mockSeats[1]); }); // id=2, available=false

    expect(result.current.selectedIds.size).toBe(0);
    expect(result.current.total).toBe(0);
  });
});
