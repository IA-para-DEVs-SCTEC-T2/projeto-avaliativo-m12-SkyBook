/**
 * MainLayout — template de layout principal da aplicação.
 *
 * Define a estrutura base das páginas sem dados reais.
 *
 * @param {object} props
 * @param {React.ReactNode} props.children - Conteúdo da página.
 */
function MainLayout({ children }) {
  return (
    <main>
      {children}
    </main>
  );
}

export default MainLayout;
