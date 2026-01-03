import { useEffect, useState } from "react";
import Modal from "../components/Modal";
import { Field, Input, Select } from "../components/Field";
import { api } from "../lib/api";

export default function DoktorlarPage() {
  const [rows, setRows] = useState([]);
  const [bolumler, setBolumler] = useState([]);
  const [err, setErr] = useState("");

  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ ad: "", soyad: "", brans: "", departmentId: "" });

  async function load() {
    setErr("");
    try {
      const [doks, deps] = await Promise.all([
        api.listDoktorlar(),
        api.listBolumler().catch(() => []),
      ]);
      setRows(Array.isArray(doks) ? doks : []);
      setBolumler(Array.isArray(deps) ? deps : []);
    } catch (e) {
      setErr(e.message || "Load failed");
    }
  }

  useEffect(() => { load(); }, []);

  function startCreate() {
    setEditing(null);
    setForm({
      ad: "",
      soyad: "",
      brans: "",
      departmentId: bolumler?.[0]?.id ? String(bolumler[0].id) : "",
    });
    setOpen(true);
  }

  function startEdit(r) {
    const depId = r.department?.id ?? r.departmentId ?? "";
    setEditing(r);
    setForm({
      ad: r.ad || "",
      soyad: r.soyad || "",
      brans: r.brans || "",
      departmentId: depId ? String(depId) : (bolumler?.[0]?.id ? String(bolumler[0].id) : ""),
    });
    setOpen(true);
  }

  async function save() {
    setErr("");
    try {
      const payload = {
        ad: form.ad,
        soyad: form.soyad,
        brans: form.brans,
        departmentId: Number(form.departmentId),
      };
      if (!payload.departmentId) throw new Error("Bölüm seçmek zorunlu.");

      if (editing?.id) await api.updateDoktor(editing.id, payload);
      else await api.createDoktor(payload);

      setOpen(false);
      await load();
    } catch (e) {
      setErr(e.message || "Save failed");
    }
  }

  async function del(r) {
    if (!confirm(`${r.ad} ${r.soyad} silinsin mi?`)) return;
    setErr("");
    try {
      await api.deleteDoktor(r.id);
      await load();
    } catch (e) {
      setErr(e.message || "Delete failed");
    }
  }

  return (
    <div className="p-6">
      <div className="flex items-start justify-between gap-4">
        <div>
          <div className="text-2xl font-semibold text-zinc-900">Doktorlar</div>
          <div className="text-sm text-zinc-500">CRUD + delete</div>
        </div>
        <button
          onClick={startCreate}
          className="rounded-2xl px-4 py-2 text-sm font-medium bg-zinc-900 text-white hover:bg-zinc-800"
        >
          + Yeni Doktor
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
              <th className="text-left px-4 py-3">Ad Soyad</th>
              <th className="text-left px-4 py-3">Branş</th>
              <th className="text-left px-4 py-3">Bölüm</th>
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
                  <td className="px-4 py-3 font-medium text-zinc-900">{r.ad} {r.soyad}</td>
                  <td className="px-4 py-3 text-zinc-800">{r.brans}</td>
                  <td className="px-4 py-3 text-zinc-800">{r.department?.ad || r.departmentId || "-"}</td>
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

      <Modal open={open} title={editing ? `Doktor Düzenle (#${editing.id})` : "Yeni Doktor"} onClose={() => setOpen(false)}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Field label="Ad"><Input value={form.ad} onChange={(e) => setForm({ ...form, ad: e.target.value })} /></Field>
          <Field label="Soyad"><Input value={form.soyad} onChange={(e) => setForm({ ...form, soyad: e.target.value })} /></Field>
          <Field label="Branş"><Input value={form.brans} onChange={(e) => setForm({ ...form, brans: e.target.value })} /></Field>
          <Field label="Bölüm">
            <Select value={form.departmentId} onChange={(e) => setForm({ ...form, departmentId: e.target.value })}>
              <option value="">Seçiniz</option>
              {bolumler.map((b) => (
                <option key={b.id} value={String(b.id)}>{b.ad || b.adBolum}</option>
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
