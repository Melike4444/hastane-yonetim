import { useEffect, useState } from "react";
import Modal from "../components/Modal";
import { Field, Input, Select } from "../components/Field";
import { api } from "../lib/api";

export default function RandevularPage() {
  const [rows, setRows] = useState([]);
  const [hastalar, setHastalar] = useState([]);
  const [doktorlar, setDoktorlar] = useState([]);
  const [err, setErr] = useState("");

  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ tarihSaat: "", hastaId: "", doktorId: "" });

  async function loadAll() {
    setErr("");
    try {
      const [r, h, d] = await Promise.all([
        api.listRandevular(),
        api.listHastalar(),
        api.listDoktorlar(),
      ]);
      setRows(Array.isArray(r) ? r : []);
      setHastalar(Array.isArray(h) ? h : []);
      setDoktorlar(Array.isArray(d) ? d : []);
    } catch (e) {
      setErr(e.message);
    }
  }

  useEffect(() => { loadAll(); }, []);

  function startCreate() {
    setEditing(null);
    setForm({
      tarihSaat: "",
      hastaId: hastalar?.[0]?.id ? String(hastalar[0].id) : "",
      doktorId: doktorlar?.[0]?.id ? String(doktorlar[0].id) : "",
    });
    setOpen(true);
  }

  function startEdit(r) {
    setEditing(r);
    const hid = r.hasta?.id ?? r.hastaId ?? "";
    const did = r.doktor?.id ?? r.doktorId ?? "";
    setForm({
      tarihSaat: (r.tarihSaat || "").slice(0, 16), // "2026-01-10T14:30"
      hastaId: hid ? String(hid) : "",
      doktorId: did ? String(did) : "",
    });
    setOpen(true);
  }

  async function save() {
    setErr("");
    try {
      const payload = {
        tarihSaat: form.tarihSaat,
        hastaId: Number(form.hastaId),
        doktorId: Number(form.doktorId),
      };
      if (!payload.tarihSaat) throw new Error("tarihSaat zorunlu.");
      if (!payload.hastaId) throw new Error("hastaId zorunlu.");
      if (!payload.doktorId) throw new Error("doktorId zorunlu.");

      if (editing?.id) await api.updateRandevu(editing.id, payload);
      else await api.createRandevu(payload);

      setOpen(false);
      await loadAll();
    } catch (e) {
      setErr(e.message);
    }
  }

  async function del(r) {
    if (!confirm(`Randevu (#${r.id}) silinsin mi?`)) return;
    setErr("");
    try {
      await api.deleteRandevu(r.id);
      await loadAll();
    } catch (e) {
      setErr(e.message);
    }
  }

  function hastaLabel(r) {
    const h = r.hasta;
    return h ? `${h.ad} ${h.soyad}` : (r.hastaId ?? "-");
  }
  function doktorLabel(r) {
    const d = r.doktor;
    return d ? `${d.ad} ${d.soyad}` : (r.doktorId ?? "-");
  }

  return (
    <div className="p-6">
      <div className="flex items-start justify-between gap-4">
        <div>
          <div className="text-2xl font-semibold text-zinc-900">Randevular</div>
          <div className="text-sm text-zinc-500">hastaId + doktorId + tarihSaat</div>
        </div>
        <button onClick={startCreate} className="rounded-2xl px-4 py-2 text-sm font-medium bg-zinc-900 text-white hover:bg-zinc-800">
          + Yeni Randevu
        </button>
      </div>

      {err && (
        <div className="mt-4 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {err}
        </div>
      )}

      <div className="mt-4 overflow-hidden rounded-2xl border border-zinc-200 bg-white">
        <table className="w-full text-sm">
          <thead className="bg-zinc-50 text-zinc-600">
            <tr>
              <th className="text-left px-4 py-3">ID</th>
              <th className="text-left px-4 py-3">Tarih/Saat</th>
              <th className="text-left px-4 py-3">Hasta</th>
              <th className="text-left px-4 py-3">Doktor</th>
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
                  <td className="px-4 py-3 text-zinc-900 font-medium">{(r.tarihSaat || "").replace("T", " ")}</td>
                  <td className="px-4 py-3 text-zinc-800">{hastaLabel(r)}</td>
                  <td className="px-4 py-3 text-zinc-800">{doktorLabel(r)}</td>
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

      <Modal open={open} title={editing ? `Randevu Düzenle (#${editing.id})` : "Yeni Randevu"} onClose={() => setOpen(false)}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Field label="Tarih/Saat">
            <Input type="datetime-local" value={form.tarihSaat} onChange={(e) => setForm({ ...form, tarihSaat: e.target.value })} />
          </Field>

          <Field label="Hasta">
            <Select value={form.hastaId} onChange={(e) => setForm({ ...form, hastaId: e.target.value })}>
              <option value="">Seçiniz</option>
              {hastalar.map((h) => (
                <option key={h.id} value={String(h.id)}>{h.ad} {h.soyad}</option>
              ))}
            </Select>
          </Field>

          <Field label="Doktor">
            <Select value={form.doktorId} onChange={(e) => setForm({ ...form, doktorId: e.target.value })}>
              <option value="">Seçiniz</option>
              {doktorlar.map((d) => (
                <option key={d.id} value={String(d.id)}>{d.ad} {d.soyad} • {d.brans}</option>
              ))}
            </Select>
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
