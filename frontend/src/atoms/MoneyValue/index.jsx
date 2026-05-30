/**
 * MoneyValue — átomo que formata e exibe um valor monetário em BRL.
 *
 * @param {object} props
 * @param {number} props.value - Valor numérico a ser formatado.
 * @param {string} [props.fontSize] - Tamanho da fonte (ex: '14px', '2rem').
 * @param {string} [props.color] - Cor do texto.
 * @param {boolean} [props.bold] - Se verdadeiro, aplica font-weight bold.
 */
function MoneyValue({ value, fontSize = '14px', color = 'inherit', bold = false }) {
  const formatted = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);

  return (
    <span style={{ fontSize, color, fontWeight: bold ? 700 : 400 }}>
      {formatted}
    </span>
  );
}

export default MoneyValue;
