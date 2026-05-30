import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

/**
 * Realiza a reserva de poltronas para um passageiro.
 *
 * @param {string[]} seatCodes - Códigos das poltronas selecionadas (ex: ['1A', '2B']).
 * @param {string} passengerName - Nome do passageiro.
 * @param {string} passengerEmail - E-mail do passageiro.
 * @returns {Promise<object>} Resposta da API com os dados da reserva.
 */
export async function createBooking(seatCodes, passengerName, passengerEmail) {
  const response = await api.post('/bookings/bookSeat', {
    seatCodes,
    passengerName,
    passengerEmail,
  });
  return response.data;
}
