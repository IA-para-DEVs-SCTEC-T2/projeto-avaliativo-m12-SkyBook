/**
 * Text — átomo de texto genérico.
 *
 * @param {object} props
 * @param {React.ReactNode} props.children - Conteúdo textual a ser exibido.
 */
function Text({ children }) {
  return <p>{children}</p>;
}

export default Text;
