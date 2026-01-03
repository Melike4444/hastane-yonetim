import { useEffect, useMemo, useState } from "react";
import { api } from "../lib/api";

function Card({ title, value, hint }) {
  return (
    <div className="rounded-3xl border border-zinc-200 bg-white p-5 shadow-sm">
      <div className="text-sm text-zinc-500">{title}</div>
      <div className="mt-2 text-3xl font-semibold text-zinc-900">{value}</div>
      {hint && <div className="mt-2 text-xs text-zinc-500">{hint}</div>}
    </div>
  );
}

function BarRow({ label, value, max }) {
  const pct = max ? Math.round((value / max) * 100) : 0;
  return (
    <div className="flex items-center gap-3">
      <div className="w-32 text-sm text-zinc-700">{label}</div>
      <div className="flex-1 h-3 rounded-full bg-zinc-100 overflow-hidden">
        <div className="h-full bg-zinc-900/80" style={{ width: `${pct}%` }} />
      </div>
      <div className="w-10 text-right text-sm text-zinc-700">{value}</div>
    </div>
  );
}

export default function Dashboard() {
  const [counts, setCounts] = useState({ hastalar: 0, doktorlar: 0, bolumler: 0, randevular: 0, muayeneler: 0 });
  const [recent, setRecent] = useState([]);
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(false);

  async function load() {
    setErr("");
    setLoading(true);
    try {
      const [h, d, b, r, m] = await Promise.all([
        api.listHastalar(),
        api.listDoktorlar(),
        api.listBolumler(),
        api.listRandevular(),
        api.listMuayeneler(),
      ]);

      setCounts({
        hastalar: Array.isArray(h) ? h.length : 0,
        doktorlar: Array.isArray(d) ? d.length : 0,
        bolumler: Array.isArray(b) ? b.length : 0,
        randevular: Array.isArray(r) ? r.length : 0,
        muayeneler: Array.isArray(m) ? m.length : 0,
      });

      const rr = Array.isArray(r) ? [...r] : [];
      rr.sort((a, c) => (c.tarihSaat || "").localeCompare(a.tarihSaat || ""));
      setRecent(rr.slice(0, 6));
    } catch (e) {
      setErr(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { load(); }, []);

  const maxVal = useMemo(() => Math.max(1, ...Object.values(counts)), [counts]);

  return (
    <div className="p-6">
      <div className="flex items-start justify-between gap-4">
        <div>
          <div className="text-2xl font-semibold text-zinc-900">Dashboard</div>
          <div className="text-sm text-zinc-500">Yönetim paneli özet görünümü • canlı veriler</div>
        </div>
        <button onClick={load} className="rounded-2xl px-4 py-2 text-sm bg-zinc-100 hover:bg-zinc-200 text-zinc-800">
          Yenile
        </button>
      </div>

      {err && <div className="mt-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{err}</div>}

      <div className="mt-5 grid grid-cols-1 md:grid-cols-2 xl:grid-cols-5 gap-4">
        <Card title="Hastalar" value={loading ? "…" : counts.hastalar} hint="Kayıtlı hasta sayısı" />
        <Card title="Doktorlar" value={loading ? "…" : counts.doktorlar} hint="Aktif doktor sayısı" />
        <Card title="Bölümler" value={loading ? "…" : counts.bolumler} hint="Departman sayısı" />
        <Card title="Randevular" value={loading ? "…" : counts.randevular} hint="Toplam randevu" />
        <Card title="Muayeneler" value={loading ? "…" : counts.muayeneler} hint="Toplam muayene" />
      </div>

      <div className="mt-6 grid grid-cols-1 xl:grid-cols-2 gap-4">
        <div className="rounded-3xl border border-zinc-200 bg-white p-5 shadow-sm">
          <div className="text-lg font-semibold text-zinc-900">Dağılım</div>
          <div className="mt-1 text-sm text-zinc-500">Modüllere göre kayıt sayıları</div>
          <div className="mt-5 space-y-3">
            <BarRow label="Hastalar" value={counts.hastalar} max={maxVal} />
            <BarRow label="Doktorlar" value={counts.doktorlar} max={maxVal} />
            <BarRow label="Bölümler" value={counts.bolumler} max={maxVal} />
            <BarRow label="Randevular" value={counts.randevular} max={maxVal} />
            <BarRow label="Muayeneler" value={counts.muayeneler} max={maxVal} />
          </div>
        </div>

        <div className="rounded-3xl border border-zinc-200 bg-white p-5 shadow-sm">
          <div>
            <div className="text-lg font-semibold text-zinc-900">Son Randevular</div>
            <div className="mt-1 text-sm text-zinc-500">En güncel 6 kayıt</div>
          </div>

          <div className="mt-4 overflow-hidden rounded-2xl border border-zinc-200">
            <table className="w-full text-sm">
              <thead className="bg-zinc-50 text-zinc-600">
                <tr>
                  <th className="text-left px-4 py-3">Tarih</th>
                  <th className="text-left px-4 py-3">Hasta</th>
                  <th className="text-left px-4 py-3">Doktor</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td className="px-4 py-6 text-zinc-500" colSpan="3">Yükleniyor...</td></tr>
                ) : recent.length === 0 ? (
                  <tr><td className="px-4 py-6 text-zinc-500" colSpan="3">Kayıt yok.</td></tr>
                ) : (
                  recent.map((r) => (
                    <tr key={r.id} className="border-t border-zinc-100">
                      <td className="px-4 py-3 text-zinc-900 font-medium">{(r.tarihSaat || "").replace("T", " ")}</td>
                      <td className="px-4 py-3 text-zinc-800">{r.hasta ? `${r.hasta.ad} ${r.hasta.soyad}` : (r.hastaId ?? "-")}</td>
                      <td className="px-4 py-3 text-zinc-800">{r.doktor ? `${r.doktor.ad} ${r.doktor.soyad}` : (r.doktorId ?? "-")}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>

          <div className="mt-4 text-xs text-zinc-500">
            Not: Backend nested nesne dönmüyorsa tabloda ID görünür.
          </div>
        </div>
      </div>
    </div>
  );
}
