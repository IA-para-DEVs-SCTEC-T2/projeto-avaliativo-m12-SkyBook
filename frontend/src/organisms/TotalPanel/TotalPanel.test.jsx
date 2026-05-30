import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import TotalPanel from './index';

describe('TotalPanel', () => {
  it('exibe o label "Total"', () => {
    render(<TotalPanel total={0} count={0} />);
    expect(screen.getByText('Total')).toBeInTheDocument();
  });

  it('exibe R$ 0,00 quando total é zero', () => {
    render(<TotalPanel total={0} count={0} />);
    expect(screen.getByText(/R\$\s*0/)).toBeInTheDocument();
  });

  it('exibe o valor total formatado em BRL', () => {
    render(<TotalPanel total={198.89} count={1} />);
    expect(screen.getByText(/198/)).toBeInTheDocument();
  });

  it('exibe "Nenhuma poltrona selecionada" quando count é 0', () => {
    render(<TotalPanel total={0} count={0} />);
    expect(screen.getByText('Nenhuma poltrona selecionada')).toBeInTheDocument();
  });

  it('exibe "1 poltrona selecionada" quando count é 1', () => {
    render(<TotalPanel total={198.89} count={1} />);
    expect(screen.getByText('1 poltrona selecionada')).toBeInTheDocument();
  });

  it('exibe plural "2 poltronas selecionadas" quando count é 2', () => {
    render(<TotalPanel total={397.78} count={2} />);
    expect(screen.getByText('2 poltronas selecionadas')).toBeInTheDocument();
  });

  it('exibe dica de desselecionar quando count > 0', () => {
    render(<TotalPanel total={198.89} count={1} />);
    expect(screen.getByText(/desselecionar/i)).toBeInTheDocument();
  });

  it('não exibe dica de desselecionar quando count é 0', () => {
    render(<TotalPanel total={0} count={0} />);
    expect(screen.queryByText(/desselecionar/i)).not.toBeInTheDocument();
  });

  it('renderiza o botão "Consultar Reservas"', () => {
    render(<TotalPanel total={0} count={0} onConsult={() => {}} />);
    expect(screen.getByRole('button', { name: /Consultar Reservas/i })).toBeInTheDocument();
  });

  it('chama onConsult ao clicar em "Consultar Reservas"', () => {
    const onConsult = vi.fn();
    render(<TotalPanel total={0} count={0} onConsult={onConsult} />);
    fireEvent.click(screen.getByRole('button', { name: /Consultar Reservas/i }));
    expect(onConsult).toHaveBeenCalledTimes(1);
  });
});
