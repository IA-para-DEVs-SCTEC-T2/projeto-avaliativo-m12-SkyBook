import { render } from '@testing-library/react';
import App from './App';

/**
 * Verifica que o componente raiz da aplicação renderiza sem erros.
 *
 * Equivalente ao SkybookApplicationTests#contextLoads do backend:
 * garante que a aplicação inicializa corretamente.
 */
describe('App', () => {
  it('renders without crashing', () => {
    render(<App />);
  });
});
