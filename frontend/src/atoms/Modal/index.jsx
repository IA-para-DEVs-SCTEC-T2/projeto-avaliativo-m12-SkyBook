import { useEffect } from 'react';

/**
 * Modal — átomo que fornece a estrutura base de um modal (overlay + container).
 *
 * Fecha ao pressionar Escape ou clicar no overlay.
 *
 * @param {object} props
 * @param {boolean} props.open - Controla a visibilidade do modal.
 * @param {function} props.onClose - Callback para fechar o modal.
 * @param {string} [props.title] - Título exibido no cabeçalho do modal.
 * @param {React.ReactNode} props.children - Conteúdo interno do modal.
 */
function Modal({ open, onClose, title, children }) {
  useEffect(() => {
    if (!open) return;
    const handleKey = (e) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleKey);
    return () => document.removeEventListener('keydown', handleKey);
  }, [open, onClose]);

  if (!open) return null;

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="modal-title"
      onClick={onClose}
      style={{
        position: 'fixed',
        inset: 0,
        backgroundColor: 'rgba(0,0,0,0.5)',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        zIndex: 1000,
        padding: '16px',
      }}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        style={{
          backgroundColor: '#fff',
          borderRadius: '16px',
          padding: '32px',
          minWidth: '360px',
          maxWidth: '480px',
          width: '100%',
          boxShadow: '0 20px 60px rgba(0,0,0,0.3)',
          position: 'relative',
        }}
      >
        {/* Botão X */}
        <button
          onClick={onClose}
          aria-label="Fechar modal"
          style={{
            position: 'absolute',
            top: '16px',
            right: '16px',
            background: 'none',
            border: 'none',
            fontSize: '20px',
            cursor: 'pointer',
            color: '#64748b',
            lineHeight: 1,
            padding: '4px',
          }}
        >
          ✕
        </button>

        {/* Título */}
        {title && (
          <h2
            id="modal-title"
            style={{
              margin: '0 0 24px',
              fontSize: '1.25rem',
              fontWeight: 700,
              color: '#1e293b',
            }}
          >
            {title}
          </h2>
        )}

        {children}
      </div>
    </div>
  );
}

export default Modal;
