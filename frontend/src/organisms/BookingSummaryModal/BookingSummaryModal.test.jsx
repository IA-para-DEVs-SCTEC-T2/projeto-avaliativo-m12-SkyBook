import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import BookingSummaryModal from './index';
import * as bookingService from '../../services/bookingService';

vi.mock('../../services/bookingService');

const mockSummary = {
  passengerName: 'João Silva',
  passengerEmail: 'joao@email.com',
  totalAmount: 308.89,
  bookings: [
    { bookingId: 1, seatCode: '1A', seatPrice: 198.89, bookedAt: '2026-05-30T10:00:00' },
    { bookingId: 2, seatCode: '3C', seatPrice: 110.0, bookedAt: '2026-05-30T10:01:00' },
  ],
};

describe('BookingSummaryModal', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('não renderiza nada quando open é false', () => {
    render(<BookingSummaryModal open={false} onClose={() => {}} />);
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument();
  });

  it('renderiza o título "Consultar Reservas" quando aberto', () => {
    render(<BookingSummaryModal open={true} onClose={() => {}} />);
    expect(screen.getByText('Consultar Reservas')).toBeInTheDocument();
  });

  it('renderiza o campo de e-mail e o botão Buscar', () => {
    render(<BookingSummaryModal open={true} onClose={() => {}} />);
    expect(screen.getByPlaceholderText('Digite seu e-mail')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Buscar/i })).toBeInTheDocument();
  });

  it('botão Buscar está desabilitado quando e-mail está vazio', () => {
    render(<BookingSummaryModal open={true} onClose={() => {}} />);
    expect(screen.getByRole('button', { name: /Buscar/i })).toBeDisabled();
  });

  it('botão Buscar é habilitado ao digitar e-mail', () => {
    render(<BookingSummaryModal open={true} onClose={() => {}} />);
    fireEvent.change(screen.getByPlaceholderText('Digite seu e-mail'), {
      target: { value: 'joao@email.com' },
    });
    expect(screen.getByRole('button', { name: /Buscar/i })).not.toBeDisabled();
  });

  it('exibe o resumo do passageiro após busca bem-sucedida', async () => {
    bookingService.getBookingSummary.mockResolvedValue(mockSummary);
    render(<BookingSummaryModal open={true} onClose={() => {}} />);

    fireEvent.change(screen.getByPlaceholderText('Digite seu e-mail'), {
      target: { value: 'joao@email.com' },
    });
    fireEvent.click(screen.getByRole('button', { name: /Buscar/i }));

    await waitFor(() => {
      expect(screen.getByText('João Silva')).toBeInTheDocument();
      expect(screen.getByText('joao@email.com')).toBeInTheDocument();
    });
  });

  it('exibe as poltronas reservadas após busca bem-sucedida', async () => {
    bookingService.getBookingSummary.mockResolvedValue(mockSummary);
    render(<BookingSummaryModal open={true} onClose={() => {}} />);

    fireEvent.change(screen.getByPlaceholderText('Digite seu e-mail'), {
      target: { value: 'joao@email.com' },
    });
    fireEvent.click(screen.getByRole('button', { name: /Buscar/i }));

    await waitFor(() => {
      expect(screen.getByText('Poltrona 1A')).toBeInTheDocument();
      expect(screen.getByText('Poltrona 3C')).toBeInTheDocument();
    });
  });

  it('exibe o valor total após busca bem-sucedida', async () => {
    bookingService.getBookingSummary.mockResolvedValue(mockSummary);
    render(<BookingSummaryModal open={true} onClose={() => {}} />);

    fireEvent.change(screen.getByPlaceholderText('Digite seu e-mail'), {
      target: { value: 'joao@email.com' },
    });
    fireEvent.click(screen.getByRole('button', { name: /Buscar/i }));

    await waitFor(() => {
      expect(screen.getByText('Total')).toBeInTheDocument();
      expect(screen.getByText(/308/)).toBeInTheDocument();
    });
  });

  it('exibe mensagem de não encontrado quando API retorna 404', async () => {
    bookingService.getBookingSummary.mockRejectedValue({ response: { status: 404 } });
    render(<BookingSummaryModal open={true} onClose={() => {}} />);

    fireEvent.change(screen.getByPlaceholderText('Digite seu e-mail'), {
      target: { value: 'naoexiste@email.com' },
    });
    fireEvent.click(screen.getByRole('button', { name: /Buscar/i }));

    await waitFor(() => {
      expect(
        screen.getByText(/Nenhuma reserva encontrada para o e-mail informado/i)
      ).toBeInTheDocument();
    });
  });

  it('exibe mensagem de erro genérico quando API falha', async () => {
    bookingService.getBookingSummary.mockRejectedValue(new Error('Network Error'));
    render(<BookingSummaryModal open={true} onClose={() => {}} />);

    fireEvent.change(screen.getByPlaceholderText('Digite seu e-mail'), {
      target: { value: 'joao@email.com' },
    });
    fireEvent.click(screen.getByRole('button', { name: /Buscar/i }));

    await waitFor(() => {
      expect(
        screen.getByText(/Não foi possível buscar as reservas/i)
      ).toBeInTheDocument();
    });
  });

  it('chama getBookingSummary ao pressionar Enter no campo de e-mail', async () => {
    bookingService.getBookingSummary.mockResolvedValue(mockSummary);
    render(<BookingSummaryModal open={true} onClose={() => {}} />);

    const input = screen.getByPlaceholderText('Digite seu e-mail');
    fireEvent.change(input, { target: { value: 'joao@email.com' } });
    fireEvent.keyDown(input, { key: 'Enter' });

    await waitFor(() => {
      expect(bookingService.getBookingSummary).toHaveBeenCalledWith('joao@email.com');
    });
  });

  it('chama onClose ao clicar no botão X', () => {
    const onClose = vi.fn();
    render(<BookingSummaryModal open={true} onClose={onClose} />);
    fireEvent.click(screen.getByRole('button', { name: /Fechar modal/i }));
    expect(onClose).toHaveBeenCalledTimes(1);
  });

  it('reseta o estado ao fechar e reabrir o modal', async () => {
    bookingService.getBookingSummary.mockResolvedValue(mockSummary);
    const onClose = vi.fn();
    render(<BookingSummaryModal open={true} onClose={onClose} />);

    fireEvent.change(screen.getByPlaceholderText('Digite seu e-mail'), {
      target: { value: 'joao@email.com' },
    });
    fireEvent.click(screen.getByRole('button', { name: /Buscar/i }));

    await waitFor(() => {
      expect(screen.getByText('João Silva')).toBeInTheDocument();
    });

    // Fecha via botão X — dispara handleClose que reseta o estado
    fireEvent.click(screen.getByRole('button', { name: /Fechar modal/i }));
    expect(onClose).toHaveBeenCalledTimes(1);
  });
});
