import React, { useEffect, useState } from "react";

const API = "http://localhost:8080/api/receteler";

export default function RecetelerPage() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  const [hastaId, setHastaId] = useState("1");
  const [doktorId, setDoktorId] = useState("1");
  const [ilaclar, setIlaclar] = useState("Parol 500mg 2x1");
  const [notlar, setNotlar] = useState("Tok karnına 5 gün");

  async function load() {
    setLoading(true);
    try {
      const r = await fetch(API);
      if (!r.ok) throw new Error(`HTTP ${r.status}`);
      const data = await r.json();
      setItems(Array.isArray(data) ? data : []);
      setErr("");
    } catch (e) {
      setErr(e?.message || "Bir hata oluştu");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function createRecete(e) {
    e.preventDefault();
    setErr("");

    try {
      const payload = {
        hastaId: Number(hastaId),
        doktorId: Number(doktorId),
        ilaclar,
        notlar,
      };

      const r = await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!r.ok) {
        const txt = await r.text();
        throw new Error(`HTTP ${r.status} - ${txt}`);
      }

      await load();
    } catch (e2) {
      setErr(e2?.message || "Kayıt başarısız");
    }
  }

  return (
    <div className="p-6">
      <div className="flex items-end justify-between gap-4">
        <div>
          <h1 className="text-2xl font-semibold text-zinc-900">Reçeteler</h1>
          <p className="text-sm text-zinc-500">
            Listeleme: GET /api/receteler • Oluşturma: POST /api/receteler
          </p>
        </div>
        <div className="text-xs text-zinc-500">
          Kayıt: <span className="font-medium text-zinc-700">{items.length}</span>
        </div>
      </div>

      <form
        onSubmit={createRecete}
        className="mt-5 rounded-2xl border border-zinc-200 bg-white shadow-sm p-4"
      >
        <div className="flex items-center justify-between">
          <div className="text-sm font-medium text-zinc-800">Yeni Reçete</div>
          {err && <div className="text-xs text-red-600">Hata: {err}</div>}
        </div>

        <div className="mt-3 grid grid-cols-1 md:grid-cols-4 gap-3">
          <label className="text-xs text-zinc-600">
            HastaId
            <input
              value={hastaId}
              onChange={(e) => setHastaId(e.target.value)}
              className="mt-1 w-full rounded-xl border border-zinc-200 px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-zinc-300"
              placeholder="1"
            />
          </label>

          <label className="text-xs text-zinc-600">
            DoktorId
            <input
              value={doktorId}
              onChange={(e) => setDoktorId(e.target.value)}
              className="mt-1 w-full rounded-xl border border-zinc-200 px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-zinc-300"
              placeholder="1"
            />
          </label>

          <label className="text-xs text-zinc-600 md:col-span-2">
            İlaçlar
            <input
              value={ilaclar}
              onChange={(e) => setIlaclar(e.target.value)}
              className="mt-1 w-full rounded-xl border border-zinc-200 px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-zinc-300"
              placeholder="Parol 500mg 2x1"
            />
          </label>

          <label className="text-xs text-zinc-600 md:col-span-4">
            Notlar
            <input
              value={notlar}
              onChange={(e) => setNotlar(e.target.value)}
              className="mt-1 w-full rounded-xl border border-zinc-200 px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-zinc-300"
              placeholder="Tok karnına 5 gün"
            />
          </label>
        </div>

        <div className="mt-3 flex items-center gap-2">
          <button
            type="submit"
            className="rounded-xl bg-zinc-900 text-white px-4 py-2 text-sm hover:bg-zinc-800"
          >
            Kaydet
          </button>
          <button
            type="button"
            onClick={load}
            className="rounded-xl border border-zinc-200 bg-white px-4 py-2 text-sm text-zinc-700 hover:bg-zinc-50"
          >
            Yenile
          </button>
          {loading && <div className="text-xs text-zinc-500">Yükleniyor…</div>}
        </div>
      </form>

      <div className="mt-5 rounded-2xl border border-zinc-200 bg-white shadow-sm overflow-hidden">
        <div className="px-4 py-3 border-b border-zinc-100 flex items-center justify-between">
          <div className="text-sm font-medium text-zinc-800">Liste</div>
        </div>

        <div className="overflow-auto">
          <table className="min-w-full text-sm">
            <thead className="bg-zinc-50 text-zinc-600">
              <tr className="text-left">
                <th className="px-4 py-3 font-medium">ID</th>
                <th className="px-4 py-3 font-medium">HastaId</th>
                <th className="px-4 py-3 font-medium">DoktorId</th>
                <th className="px-4 py-3 font-medium">İlaçlar</th>
                <th className="px-4 py-3 font-medium">Notlar</th>
                <th className="px-4 py-3 font-medium">Tarih</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-zinc-100">
              {!loading && items.length === 0 && (
                <tr>
                  <td className="px-4 py-6 text-zinc-500" colSpan={6}>
                    Henüz kayıt yok.
                  </td>
                </tr>
              )}

              {items.map((r) => (
                <tr key={r.id} className="hover:bg-zinc-50">
                  <td className="px-4 py-3 font-medium text-zinc-900">{r.id}</td>
                  <td className="px-4 py-3 text-zinc-700">{r.hastaId}</td>
                  <td className="px-4 py-3 text-zinc-700">{r.doktorId}</td>
                  <td className="px-4 py-3 text-zinc-700">{r.ilaclar}</td>
                  <td className="px-4 py-3 text-zinc-700">{r.notlar}</td>
                  <td className="px-4 py-3 text-zinc-600">
                    {r.tarihSaat ? new Date(r.tarihSaat).toLocaleString() : "-"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
