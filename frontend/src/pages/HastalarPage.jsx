import { useEffect, useMemo, useState } from "react";

const API_BASE = import.meta?.env?.VITE_API_BASE_URL || "http://localhost:8080";

function cx(...xs) {
  return xs.filter(Boolean).join(" ");
}

export default function HastalarPage() {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  const [q, setQ] = useState("");

  const [open, setOpen] = useState(false);
  const [form, setForm] = useState({ ad: "", soyad: "", telefon: "" });
  const [saving, setSaving] = useState(false);

  async function load() {
    setLoading(true);
    setErr("");
    try {
      const r = await fetch(`${API_BASE}/api/hastalar`);
      if (!r.ok) throw new Error(`GET /api/hastalar failed: ${r.status}`);
      const data = await r.json();
      setRows(Array.isArray(data) ? data : []);
    } catch (e) {
      setErr(e?.message || "Liste alınamadı");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const filtered = useMemo(() => {
    const s = q.trim().toLowerCase();
    if (!s) return rows;
    return rows.filter((x) => {
      const ad = (x.ad || "").toLowerCase();
      const soyad = (x.soyad || "").toLowerCase();
      const tel = (x.telefon || "").toLowerCase();
      return ad.includes(s) || soyad.includes(s) || tel.includes(s);
    });
  }, [rows, q]);

  function resetForm() {
    setForm({ ad: "", soyad: "", telefon: "" });
  }

  async function onCreate(e) {
    e.preventDefault();
    setErr("");

    const payload = {
      ad: form.ad.trim(),
      soyad: form.soyad.trim(),
      telefon: form.telefon.trim(),
    };

    if (!payload.ad || !payload.soyad) {
      setErr("Ad ve Soyad zorunlu.");
      return;
    }

    setSaving(true);
    try {
      const r = await fetch(`${API_BASE}/api/hastalar`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!r.ok) {
        const t = await r.text().catch(() => "");
        throw new Error(`POST /api/hastalar failed: ${r.status} ${t}`);
      }
      setOpen(false);
      resetForm();
      await load();
    } catch (e2) {
      setErr(e2?.message || "Kayıt eklenemedi");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-xl font-semibold text-zinc-900">Hastalar</h1>
          <p className="text-sm text-zinc-500">Listeleme ve hızlı hasta ekleme</p>
        </div>

        <div className="flex gap-2">
          <div className="relative">
            <input
              value={q}
              onChange={(e) => setQ(e.target.value)}
              placeholder="Ara: ad, soyad, telefon..."
              className="w-64 max-w-[70vw] rounded-xl border border-zinc-200 bg-white px-3 py-2 text-sm outline-none focus:border-zinc-400"
            />
          </div>

          <button
            onClick={() => setOpen(true)}
            className="rounded-xl bg-zinc-900 px-4 py-2 text-sm font-medium text-white hover:bg-zinc-800"
          >
            + Ekle
          </button>
        </div>
      </div>

      {/* Error */}
      {!!err && (
        <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {err}
        </div>
      )}

      {/* Table */}
      <div className="overflow-hidden rounded-2xl border border-zinc-200 bg-white">
        <div className="border-b border-zinc-200 px-4 py-3 text-sm font-medium text-zinc-700">
          Kayıtlar ({filtered.length})
        </div>

        {loading ? (
          <div className="px-4 py-8 text-sm text-zinc-500">Yükleniyor…</div>
        ) : filtered.length === 0 ? (
          <div className="px-4 py-8 text-sm text-zinc-500">
            Kayıt bulunamadı.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full text-left text-sm">
              <thead className="bg-zinc-50 text-zinc-600">
                <tr>
                  <th className="px-4 py-3 font-medium">ID</th>
                  <th className="px-4 py-3 font-medium">Ad</th>
                  <th className="px-4 py-3 font-medium">Soyad</th>
                  <th className="px-4 py-3 font-medium">Telefon</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-zinc-100">
                {filtered.map((x) => (
                  <tr key={x.id} className="hover:bg-zinc-50">
                    <td className="px-4 py-3 text-zinc-500">{x.id}</td>
                    <td className="px-4 py-3 font-medium text-zinc-900">{x.ad}</td>
                    <td className="px-4 py-3 text-zinc-700">{x.soyad}</td>
                    <td className="px-4 py-3 text-zinc-700">{x.telefon || "-"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Modal */}
      {open && (
        <div className="fixed inset-0 z-50 flex items-center justify-center">
          <div
            className="absolute inset-0 bg-black/40"
            onClick={() => !saving && setOpen(false)}
          />
          <div className="relative w-[560px] max-w-[92vw] rounded-2xl bg-white p-5 shadow-xl">
            <div className="flex items-start justify-between gap-4">
              <div>
                <h2 className="text-lg font-semibold text-zinc-900">Yeni Hasta</h2>
                <p className="text-sm text-zinc-500">Ad, soyad, telefon</p>
              </div>
              <button
                className="rounded-lg px-2 py-1 text-zinc-500 hover:bg-zinc-100"
                onClick={() => !saving && setOpen(false)}
              >
                ✕
              </button>
            </div>

            <form onSubmit={onCreate} className="mt-4 space-y-3">
              <div className="grid gap-3 sm:grid-cols-2">
                <div>
                  <label className="mb-1 block text-xs font-medium text-zinc-600">
                    Ad *
                  </label>
                  <input
                    value={form.ad}
                    onChange={(e) => setForm((p) => ({ ...p, ad: e.target.value }))}
                    className="w-full rounded-xl border border-zinc-200 px-3 py-2 text-sm outline-none focus:border-zinc-400"
                    placeholder="Örn: Ali"
                  />
                </div>
                <div>
                  <label className="mb-1 block text-xs font-medium text-zinc-600">
                    Soyad *
                  </label>
                  <input
                    value={form.soyad}
                    onChange={(e) =>
                      setForm((p) => ({ ...p, soyad: e.target.value }))
                    }
                    className="w-full rounded-xl border border-zinc-200 px-3 py-2 text-sm outline-none focus:border-zinc-400"
                    placeholder="Örn: Yılmaz"
                  />
                </div>
              </div>

              <div>
                <label className="mb-1 block text-xs font-medium text-zinc-600">
                  Telefon
                </label>
                <input
                  value={form.telefon}
                  onChange={(e) =>
                    setForm((p) => ({ ...p, telefon: e.target.value }))
                  }
                  className="w-full rounded-xl border border-zinc-200 px-3 py-2 text-sm outline-none focus:border-zinc-400"
                  placeholder="Örn: 0555..."
                />
              </div>

              <div className="flex items-center justify-end gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => {
                    if (saving) return;
                    setOpen(false);
                    resetForm();
                  }}
                  className="rounded-xl border border-zinc-200 px-4 py-2 text-sm hover:bg-zinc-50"
                >
                  İptal
                </button>
                <button
                  disabled={saving}
                  type="submit"
                  className={cx(
                    "rounded-xl px-4 py-2 text-sm font-medium text-white",
                    saving ? "bg-zinc-400" : "bg-zinc-900 hover:bg-zinc-800"
                  )}
                >
                  {saving ? "Kaydediliyor…" : "Kaydet"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

