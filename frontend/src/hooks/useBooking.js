import { useState } from 'react';
import { createBooking } from '../services/bookingService';

/**
 * Hook que gerencia o fluxo de reserva de poltronas em dois passos:
 * 1. Modal de resumo (confirmação das poltronas selecionadas)
 * 2. Modal de dados do passageiro (nome e e-mail)
 *
 * @param {function} onSuccess - Callback chamado após reserva bem-sucedida.
 *   Recebe o resultado da API como argumento.
 * @returns {{
 *   step: 'idle'|'summary'|'form',
 *   loading: boolean,
 *   error: string|null,
 *   openSummary: () => void,
 *   goToForm: () => void,
 *   cancel: () => void,
 *   confirm: (name: string, email: string, seatCodes: string[]) => Promise<void>
 * }}
 */
export function useBooking(onSuccess) {
  const [step, setStep] = useState('idle');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /** Abre o Modal 1 (resumo). */
  function openSummary() {
    setError(null);
    setStep('summary');
  }

  /** Avança do Modal 1 para o Modal 2 (dados do passageiro). */
  function goToForm() {
    setError(null);
    setStep('form');
  }

  /** Cancela o fluxo e fecha todos os modais. */
  function cancel() {
    setStep('idle');
    setError(null);
  }

  /**
   * Envia a requisição de reserva ao backend.
   * Em caso de sucesso, fecha os modais e chama onSuccess.
   * Em caso de erro, exibe mensagem amigável sem perder a seleção.
   *
   * @param {string} name - Nome do passageiro.
   * @param {string} email - E-mail do passageiro.
   * @param {string[]} seatCodes - Códigos das poltronas selecionadas.
   */
  async function confirm(name, email, seatCodes) {
    setLoading(true);
    setError(null);
    try {
      const result = await createBooking(seatCodes, name, email);
      setStep('idle');
      onSuccess(result);
    } catch (err) {
      const message =
        err?.response?.data?.message ||
        'Não foi possível concluir a reserva. Tente novamente.';
      setError(message);
    } finally {
      setLoading(false);
    }
  }

  return { step, loading, error, openSummary, goToForm, cancel, confirm };
}
