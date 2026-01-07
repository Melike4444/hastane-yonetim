import { useState } from "react";

import Dashboard from "./pages/Dashboard";
import HastalarPage from "./pages/HastalarPage";
import DoktorlarPage from "./pages/DoktorlarPage";
import BolumlerPage from "./pages/BolumlerPage";
import RandevularPage from "./pages/RandevularPage";
import MuayenelerPage from "./pages/MuayenelerPage";
import RecetelerPage from "./pages/RecetelerPage";

const items = [
  { key: "dashboard", label: "Dashboard" },
  { key: "hastalar", label: "Hastalar" },
  { key: "doktorlar", label: "Doktorlar" },
  { key: "bolumler", label: "Bölümler" },
  { key: "randevular", label: "Randevular" },
  { key: "muayeneler", label: "Muayeneler" },
  { key: "receteler", label: "Reçeteler" },
];

function Sidebar({ active, setActive }) {
  return (
    <aside className="w-64 border-r border-zinc-200 bg-white">
      <div className="p-4 border-b border-zinc-100">
        <div className="text-lg font-semibold text-zinc-900">Hastane Yönetim</div>
        <div className="text-xs text-zinc-500">React + Tailwind • Premium UI</div>
      </div>

      <nav className="p-2">
        {items.map((it) => (
          <button
            key={it.key}
            onClick={() => setActive(it.key)}
            className={[
              "w-full text-left px-3 py-2 rounded-xl mb-1 text-sm",
              active === it.key
                ? "bg-zinc-900 text-white"
                : "text-zinc-700 hover:bg-zinc-100",
            ].join(" ")}
          >
            {it.label}
          </button>
        ))}
      </nav>
    </aside>
  );
}

function Placeholder({ title }) {
  return (
    <div className="p-10 text-zinc-500">
      {title} sayfası yakında
    </div>
  );
}

export default function App() {
  const [active, setActive] = useState("dashboard");

  return (
    <div className="flex min-h-screen bg-zinc-50">
      <Sidebar active={active} setActive={setActive} />

      <main className="flex-1">
        {active === "dashboard" && <Dashboard />}
        {active === "hastalar" && <HastalarPage />}
        {active === "doktorlar" && <DoktorlarPage />}
        {active === "bolumler" && <BolumlerPage />}
        {active === "randevular" && <RandevularPage />}
        {active === "muayeneler" && <MuayenelerPage />}
        {active === "receteler" && <RecetelerPage />}

        {active !== "dashboard" &&
          active !== "hastalar" &&
          active !== "doktorlar" &&
          active !== "bolumler" &&
          active !== "randevular" &&
          active !== "muayeneler" &&
          active !== "receteler" && (
            <Placeholder title={items.find((x) => x.key === active)?.label} />
          )}
      </main>
    </div>
  );
}
