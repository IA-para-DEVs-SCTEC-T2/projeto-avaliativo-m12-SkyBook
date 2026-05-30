import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import SeatCard from './index';

const availableSeat = { id: 1, code: '1A', price: 198.89, available: true };
const unavailableSeat = { id: 2, code: '1B', price: 198.89, available: false };

describe('SeatCard', () => {
  it('renderiza o código da poltrona', () => {
    render(<SeatCard seat={availableSeat} selected={false} onToggle={() => {}} />);
    expect(screen.getByText('1A')).toBeInTheDocument();
  });

  it('renderiza o preço formatado em BRL', () => {
    render(<SeatCard seat={availableSeat} selected={false} onToggle={() => {}} />);
    expect(screen.getByText(/198/)).toBeInTheDocument();
  });

  it('chama onToggle ao clicar em poltrona disponível', () => {
    const onToggle = vi.fn();
    render(<SeatCard seat={availableSeat} selected={false} onToggle={onToggle} />);
    fireEvent.click(screen.getByRole('button'));
    expect(onToggle).toHaveBeenCalledWith(availableSeat);
  });

  it('não chama onToggle ao clicar em poltrona indisponível', () => {
    const onToggle = vi.fn();
    render(<SeatCard seat={unavailableSeat} selected={false} onToggle={onToggle} />);
    fireEvent.click(screen.getByRole('button'));
    expect(onToggle).not.toHaveBeenCalled();
  });

  it('botão está desabilitado quando poltrona é indisponível', () => {
    render(<SeatCard seat={unavailableSeat} selected={false} onToggle={() => {}} />);
    expect(screen.getByRole('button')).toBeDisabled();
  });

  it('aria-pressed é true quando selecionada', () => {
    render(<SeatCard seat={availableSeat} selected={true} onToggle={() => {}} />);
    expect(screen.getByRole('button')).toHaveAttribute('aria-pressed', 'true');
  });

  it('aria-pressed é false quando não selecionada', () => {
    render(<SeatCard seat={availableSeat} selected={false} onToggle={() => {}} />);
    expect(screen.getByRole('button')).toHaveAttribute('aria-pressed', 'false');
  });

  it('aria-label indica disponível quando não selecionada', () => {
    render(<SeatCard seat={availableSeat} selected={false} onToggle={() => {}} />);
    expect(screen.getByRole('button')).toHaveAttribute(
      'aria-label',
      expect.stringContaining('disponível')
    );
  });

  it('aria-label indica indisponível quando poltrona não está disponível', () => {
    render(<SeatCard seat={unavailableSeat} selected={false} onToggle={() => {}} />);
    expect(screen.getByRole('button')).toHaveAttribute(
      'aria-label',
      expect.stringContaining('indisponível')
    );
  });
});
