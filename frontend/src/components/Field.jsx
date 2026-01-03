import React from "react";

export function Field({ label, hint, children }) {
  return (
    <label className="block">
      {label && (
        <div className="mb-1 text-sm font-medium text-zinc-700">
          {label}
        </div>
      )}
      {children}
      {hint && <div className="mt-1 text-xs text-zinc-500">{hint}</div>}
    </label>
  );
}

export function Input(props) {
  return (
    <input
      {...props}
      className={[
        "w-full rounded-xl border border-zinc-200 bg-white px-3 py-2 text-sm",
        "outline-none focus:ring-2 focus:ring-zinc-900/10 focus:border-zinc-300",
        props.className || "",
      ].join(" ")}
    />
  );
}

export function Select(props) {
  return (
    <select
      {...props}
      className={[
        "w-full rounded-xl border border-zinc-200 bg-white px-3 py-2 text-sm",
        "outline-none focus:ring-2 focus:ring-zinc-900/10 focus:border-zinc-300",
        props.className || "",
      ].join(" ")}
    />
  );
}

export function Textarea(props) {
  return (
    <textarea
      {...props}
      rows={props.rows ?? 4}
      className={[
        "w-full rounded-xl border border-zinc-200 bg-white px-3 py-2 text-sm",
        "outline-none focus:ring-2 focus:ring-zinc-900/10 focus:border-zinc-300",
        props.className || "",
      ].join(" ")}
    />
  );
}
