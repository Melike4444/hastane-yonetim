import { useEffect, useMemo, useState } from "react";
import Modal from "../components/Modal";
import { Field, Input, Select, Textarea } from "../components/Field";
import { api } from "../lib/api";

function fmtRandevu(r) {
  const dt = r.tarihSaat || r.tarih || r.dateTime || "";
  const hasta = r.hasta ? `${r.hasta.ad} ${r.hasta.soyad}` : (r.hastaId ? `Hasta#${r.hastaId}` : "Hasta?");
  const doktor = r.doktor ? `${r.doktor.ad} ${r.doktor.soyad}` : (r.doktorId ? `Doktor#${r.doktorId}` : "Doktor?");
  return `#${r.id} • ${dt} • ${hasta} → ${doktor}`;
}

export default function MuayenelerPage() {
  const [rows, setRows] = useState([]);
  const [randevular, setRandevular] = useState([]);
  const [err, setErr] = useState("");

  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({
    randevuId: "",
    sikayet: "",
    tani: "",
    notlar: "",
  });

  async function loadAll() {
    setErr("");
    try {
      const [m, r] = await Promise.all([
        api.listMuayeneler(),
        api.listRandevular(),
      ]);
      setRows(Array.isArray(m) ? m : []);
      setRandevular(Array.isArray(r) ? r : []);
    } catch (e) {
      setErr(e.message || "Load failed");
    }
  }

  useEffect(() => { loadAll(); }, []);

  const randevuOptions = useMemo(
    () => randevular.map((x) => ({ id: String(x.id), label: fmtRandevu(x) })),
    [randevular]
  );

  function startCreate() {
    setEditing(null);
    setForm({
      randevuId: randevuOptions[0]?.id || "",
      sikayet: "",
      tani: "",
      notlar: "",
    });
    setOpen(true);
  }

  function startEdit(r) {
    setEditing(r);
    setForm({
      randevuId: String(r.randevuId || r.randevu?.id || ""),
      sikayet: r.sikayet || "",
      tani: r.tani || "",
      notlar: r.notlar || r.not || "",
    });
    setOpen(true);
  }

  async function save() {
    setErr("");
    try {
      const rid = Number(form.randevuId);
      if (!rid) throw new Error("Randevu seçmek zorunlu (backend randevuId istiyor).");

      const payload = {
        randevuId: rid,
        randevu: { id: rid },
        sikayet: form.sikayet || "",
        tani: form.tani || "",
        not: form.notlar || "",
        notlar: form.notlar || "",
      };

      if (editing?.id) await api.updateMuayene(editing.id, payload);
      else await api.createMuayene(payload);

      setOpen(false);
      await loadAll();
    } catch (e) {
      setErr(e.message || "Save failed");
    }
  }

  async function del(r) {
    if (!confirm(`Muayene (#${r.id}) silinsin mi?`)) return;
    setErr("");
    try {
      await api.deleteMuayene(r.id);
      await loadAll();
    } catch (e) {
      setErr(e.message || "Delete failed");
    }
  }

  function randevuLabel(r) {
    const rid = r.randevu?.id || r.randevuId;
    if (!rid) return "-";
    const found = randevular.find((x) => String(x.id) === String(rid));
    return found ? fmtRandevu(found) : `#${rid}`;
  }

  return (
    <div className="p-6">
      <div className="flex items-start justify-between gap-4">
        <div>
          <div className="text-2xl font-semibold text-zinc-900">Muayeneler</div>
          <div className="text-sm text-zinc-500">Backend: muayene randevuId ile açılıyor</div>
        </div>
        <button
          onClick={startCreate}
          className="rounded-2xl px-4 py-2 text-sm font-medium bg-zinc-900 text-white hover:bg-zinc-800"
        >
          + Yeni Muayene
        </button>
      </div>

      {err && (
        <div className="mt-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700 whitespace-pre-wrap">
          {err}
        </div>
      )}

      <div className="mt-4 overflow-hidden rounded-2xl border border-zinc-200 bg-white">
        <table className="w-full text-sm">
          <thead className="bg-zinc-50 text-zinc-600">
            <tr>
              <th className="text-left px-4 py-3">ID</th>
              <th className="text-left px-4 py-3">Randevu</th>
              <th className="text-left px-4 py-3">Şikayet</th>
              <th className="text-left px-4 py-3">Tanı</th>
              <th className="text-right px-4 py-3">İşlem</th>
            </tr>
          </thead>
          <tbody>
            {rows.length === 0 ? (
              <tr><td className="px-4 py-6 text-zinc-500" colSpan="5">Kayıt yok.</td></tr>
            ) : (
              rows.map((r) => (
                <tr key={r.id} className="border-t border-zinc-100">
                  <td className="px-4 py-3 text-zinc-500">{r.id}</td>
                  <td className="px-4 py-3 font-medium text-zinc-900">{randevuLabel(r)}</td>
                  <td className="px-4 py-3">{r.sikayet || "-"}</td>
                  <td className="px-4 py-3">{r.tani || "-"}</td>
                  <td className="px-4 py-3">
                    <div className="flex justify-end gap-2">
                      <button onClick={() => startEdit(r)} className="rounded-xl px-3 py-1.5 bg-zinc-100 hover:bg-zinc-200">
                        Düzenle
                      </button>
                      <button onClick={() => del(r)} className="rounded-xl px-3 py-1.5 bg-red-50 hover:bg-red-100 text-red-700">
                        Sil
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <Modal
        open={open}
        title={editing ? `Muayene Düzenle (#${editing.id})` : "Yeni Muayene"}
        onClose={() => setOpen(false)}
      >
        <div className="grid grid-cols-1 gap-4">
          <Field label="Randevu">
            <Select value={form.randevuId} onChange={(e) => setForm({ ...form, randevuId: e.target.value })}>
              <option value="">Seçiniz</option>
              {randevuOptions.map((o) => <option key={o.id} value={o.id}>{o.label}</option>)}
            </Select>
          </Field>

          <Field label="Şikayet">
            <Input value={form.sikayet} onChange={(e) => setForm({ ...form, sikayet: e.target.value })} />
          </Field>

          <Field label="Tanı">
            <Input value={form.tani} onChange={(e) => setForm({ ...form, tani: e.target.value })} />
          </Field>

          <Field label="Notlar">
            <Textarea value={form.notlar} onChange={(e) => setForm({ ...form, notlar: e.target.value })} />
          </Field>
        </div>

        <div className="mt-5 flex justify-end gap-2">
          <button onClick={() => setOpen(false)} className="rounded-2xl px-4 py-2 text-sm bg-zinc-100 hover:bg-zinc-200">
            Vazgeç
          </button>
          <button onClick={save} className="rounded-2xl px-4 py-2 text-sm bg-zinc-900 text-white hover:bg-zinc-800">
            Kaydet
          </button>
        </div>
      </Modal>
    </div>
  );
}
