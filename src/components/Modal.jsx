export default function Modal({ open, title, children, onClose }) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/40" onClick={onClose} />
      <div className="relative w-[min(720px,92vw)] rounded-2xl bg-white shadow-xl border border-zinc-200">
        <div className="flex items-center justify-between px-5 py-4 border-b border-zinc-100">
          <div className="text-lg font-semibold text-zinc-900">{title}</div>
          <button
            onClick={onClose}
            className="rounded-xl px-3 py-1.5 text-sm bg-zinc-100 hover:bg-zinc-200 text-zinc-800"
          >
            Kapat
          </button>
        </div>
        <div className="p-5">{children}</div>
      </div>
    </div>
  );
}
