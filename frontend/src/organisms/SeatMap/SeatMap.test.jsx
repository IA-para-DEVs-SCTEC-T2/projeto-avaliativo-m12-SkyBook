import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import SeatMap from './index';

/** Gera 60 poltronas simulando a configuração real da aeronave. */
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

const seats = generateSeats();

describe('SeatMap', () => {
  it('renderiza os cabeçalhos das colunas A–F', () => {
    render(<SeatMap seats={seats} selectedIds={new Set()} onToggle={() => {}} />);
    ['A', 'B', 'C', 'D', 'E', 'F'].forEach((col) => {
      expect(screen.getAllByText(col).length).toBeGreaterThan(0);
    });
  });

  it('renderiza os números das fileiras 1–10', () => {
    render(<SeatMap seats={seats} selectedIds={new Set()} onToggle={() => {}} />);
    for (let i = 1; i <= 10; i++) {
      expect(screen.getAllByText(String(i)).length).toBeGreaterThan(0);
    }
  });

  it('renderiza 60 botões de poltrona', () => {
    render(<SeatMap seats={seats} selectedIds={new Set()} onToggle={() => {}} />);
    expect(screen.getAllByRole('button')).toHaveLength(60);
  });

  it('exibe a legenda com os 3 status', () => {
    render(<SeatMap seats={seats} selectedIds={new Set()} onToggle={() => {}} />);
    expect(screen.getByText('Disponível')).toBeInTheDocument();
    expect(screen.getByText('Indisponível')).toBeInTheDocument();
    expect(screen.getByText('Selecionado')).toBeInTheDocument();
  });

  it('chama onToggle ao clicar em uma poltrona', () => {
    const onToggle = vi.fn();
    render(<SeatMap seats={seats} selectedIds={new Set()} onToggle={onToggle} />);
    fireEvent.click(screen.getAllByRole('button')[0]);
    expect(onToggle).toHaveBeenCalledTimes(1);
  });

  it('poltrona indisponível está desabilitada', () => {
    const seatsWithUnavailable = seats.map((s) =>
      s.id === 1 ? { ...s, available: false } : s
    );
    render(<SeatMap seats={seatsWithUnavailable} selectedIds={new Set()} onToggle={() => {}} />);
    const buttons = screen.getAllByRole('button');
    expect(buttons[0]).toBeDisabled();
  });

  it('poltrona selecionada tem aria-pressed true', () => {
    render(<SeatMap seats={seats} selectedIds={new Set([1])} onToggle={() => {}} />);
    const buttons = screen.getAllByRole('button');
    expect(buttons[0]).toHaveAttribute('aria-pressed', 'true');
  });

  it('renderiza grid vazio sem erros quando seats é array vazio', () => {
    render(<SeatMap seats={[]} selectedIds={new Set()} onToggle={() => {}} />);
    expect(screen.queryAllByRole('button')).toHaveLength(0);
  });
});
