import { render, screen, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { MemoryRouter } from 'react-router-dom';
import SeatMapPage from './index';
import * as seatsService from '../../services/seatsService';

vi.mock('../../services/seatsService');

/** Gera 60 poltronas disponíveis. */
function generateSeats() {
  const cols = ['A', 'B', 'C', 'D', 'E', 'F'];
  const seats = [];
  let id = 1;
  for (let row = 1; row <= 10; row++) {
    for (const col of cols) {
      seats.push({ id, code: `${row}${col}`, price: 110.0, available: true });
      id++;
    }
  }
  return seats;
}

const renderPage = () =>
  render(
    <MemoryRouter>
      <SeatMapPage />
    </MemoryRouter>
  );

describe('SeatMapPage', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('exibe "Carregando poltronas..." durante o loading', () => {
    // fetchSeats nunca resolve — mantém loading
    seatsService.fetchSeats.mockReturnValue(new Promise(() => {}));
    renderPage();
    expect(screen.getByText('Carregando poltronas...')).toBeInTheDocument();
  });

  it('exibe mensagem de erro quando a API falha', async () => {
    seatsService.fetchSeats.mockRejectedValue(new Error('fail'));
    renderPage();
    await waitFor(() => {
      expect(
        screen.getByText(/Não foi possível carregar as poltronas/i)
      ).toBeInTheDocument();
    });
  });

  it('exibe o título SkyBook após carregar', async () => {
    seatsService.fetchSeats.mockResolvedValue(generateSeats());
    renderPage();
    await waitFor(() => {
      expect(screen.getByText(/SkyBook/i)).toBeInTheDocument();
    });
  });

  it('exibe o mapa de poltronas após carregar', async () => {
    seatsService.fetchSeats.mockResolvedValue(generateSeats());
    renderPage();
    await waitFor(() => {
      // 60 botões de poltrona + 1 botão "Realizar Reserva" = 61 botões no total
      const seatButtons = screen
        .getAllByRole('button')
        .filter((btn) => btn.getAttribute('aria-label')?.startsWith('Poltrona'));
      expect(seatButtons).toHaveLength(60);
    });
  });

  it('exibe o botão "Realizar Reserva" desabilitado sem seleção', async () => {
    seatsService.fetchSeats.mockResolvedValue(generateSeats());
    renderPage();
    await waitFor(() => {
      const bookBtn = screen.getByRole('button', { name: /Realizar Reserva/i });
      expect(bookBtn).toBeDisabled();
    });
  });

  it('exibe o painel de total com R$ 0,00 inicial', async () => {
    seatsService.fetchSeats.mockResolvedValue(generateSeats());
    renderPage();
    await waitFor(() => {
      expect(screen.getByText(/R\$\s*0/)).toBeInTheDocument();
    });
  });

  it('exibe "Nenhuma poltrona selecionada" no estado inicial', async () => {
    seatsService.fetchSeats.mockResolvedValue(generateSeats());
    renderPage();
    await waitFor(() => {
      expect(screen.getByText('Nenhuma poltrona selecionada')).toBeInTheDocument();
    });
  });
});
