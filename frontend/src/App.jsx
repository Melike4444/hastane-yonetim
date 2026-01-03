import { useState } from "react";
import Dashboard from "./pages/Dashboard";
import HastalarPage from "./pages/HastalarPage";
import DoktorlarPage from "./pages/DoktorlarPage";
import RandevularPage from "./pages/RandevularPage";
import MuayenelerPage from "./pages/MuayenelerPage";
import BolumlerPage from "./pages/BolumlerPage";

const items = [
  { key: "dashboard", label: "Dashboard" },
  { key: "hastalar", label: "Hastalar" },
  { key: "doktorlar", label: "Doktorlar" },
  { key: "bolumler", label: "Bölümler" },
  { key: "randevular", label: "Randevular" },
  { key: "muayeneler", label: "Muayeneler" },
];

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
      {/* Sidebar */}
      <aside className="w-64 bg-white border-r border-zinc-200 p-4">
        <h1 className="mb-6 text-lg font-bold">Hastane Yönetim</h1>

        <nav className="space-y-2">
          {items.map((it) => {
            const isActive = it.key === active;
            return (
              <button
                key={it.key}
                onClick={() => setActive(it.key)}
                className={
                  "w-full text-left rounded-xl px-4 py-2 text-sm " +
                  (isActive
                    ? "bg-zinc-900 text-white"
                    : "text-zinc-700 hover:bg-zinc-100")
                }
              >
                {it.label}
              </button>
            );
          })}
        </nav>
      </aside>

      {/* Content */}
      <main className="flex-1 p-6">
        {active === "dashboard" && <Dashboard />}
        {active === "hastalar" && <HastalarPage />}
        {active === "doktorlar" && <DoktorlarPage />}
        {active === "bolumler" && <BolumlerPage />}
        {active === "randevular" && <RandevularPage />}
        {active === "muayeneler" && <MuayenelerPage />}

        {![
          "dashboard",
          "hastalar",
          "doktorlar",
          "bolumler",
          "randevular",
          "muayeneler",
        ].includes(active) && (
          <Placeholder
            title={items.find((x) => x.key === active)?.label}
          />
        )}
      </main>
    </div>
  );
}

