export function Field({ label, children }) {
  return (
    <label className="block">
      <div className="text-sm font-medium text-zinc-700 mb-1">{label}</div>
      {children}
    </label>
  );
}

export function Input(props) {
  return (
    <input
      {...props}
      className={
        "w-full rounded-xl border border-zinc-200 bg-white px-3 py-2 text-sm " +
        "outline-none focus:ring-2 focus:ring-zinc-200 focus:border-zinc-300 " +
        (props.className || "")
      }
    />
  );
}

export function Select(props) {
  return (
    <select
      {...props}
      className={
        "w-full rounded-xl border border-zinc-200 bg-white px-3 py-2 text-sm " +
        "outline-none focus:ring-2 focus:ring-zinc-200 focus:border-zinc-300 " +
        (props.className || "")
      }
    >
      {props.children}
    </select>
  );
}

export function Textarea(props) {
  return (
    <textarea
      {...props}
      className={
        "w-full rounded-xl border border-zinc-200 bg-white px-3 py-2 text-sm " +
        "outline-none focus:ring-2 focus:ring-zinc-200 focus:border-zinc-300 " +
        (props.className || "")
      }
    />
  );
}
