import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

/**
 * Busca todas as poltronas da aeronave.
 * @returns {Promise<Array>} Lista de poltronas com id, code, price e available.
 */
export async function fetchSeats() {
  const response = await api.get('/seats/listSeats');
  return response.data;
}
