import { useEffect, useState } from "react";
import Modal from "../components/Modal";
import { Field, Input } from "../components/Field";
import { api } from "../lib/api";

export default function BolumlerPage() {
  const [rows, setRows] = useState([]);
  const [err, setErr] = useState("");
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState({ ad: "" });

  async function load() {
    setErr("");
    try {
      const data = await api.listBolumler();
      setRows(Array.isArray(data) ? data : []);
    } catch (e) {
      setErr(e.message);
    }
  }

  useEffect(() => { load(); }, []);

  function startCreate() {
    setEditing(null);
    setForm({ ad: "" });
    setOpen(true);
  }

  function startEdit(r) {
    setEditing(r);
    setForm({ ad: r.ad || r.adBolum || "" });
    setOpen(true);
  }

  async function save() {
    setErr("");
    try {
      if (editing?.id) await api.updateBolum(editing.id, form);
      else await api.createBolum(form);
      setOpen(false);
      await load();
    } catch (e) {
      setErr(e.message);
    }
  }

  async function del(r) {
    if (!confirm(`${r.ad || r.adBolum} silinsin mi?`)) return;
    setErr("");
    try {
      await api.deleteBolum(r.id);
      await load();
    } catch (e) {
      setErr(e.message);
    }
  }

  return (
    <div className="p-6">
      <div className="flex items-start justify-between gap-4">
        <div>
          <div className="text-2xl font-semibold text-zinc-900">Bölümler</div>
          <div className="text-sm text-zinc-500">Departman yönetimi</div>
        </div>
        <button
          onClick={startCreate}
          className="rounded-2xl px-4 py-2 text-sm font-medium bg-zinc-900 text-white hover:bg-zinc-800"
        >
          + Yeni Bölüm
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
              <th className="text-left px-4 py-3">Bölüm Adı</th>
              <th className="text-right px-4 py-3">İşlem</th>
            </tr>
          </thead>
          <tbody>
            {rows.length === 0 ? (
              <tr><td className="px-4 py-6 text-zinc-500" colSpan="3">Kayıt yok.</td></tr>
            ) : (
              rows.map((r) => (
                <tr key={r.id} className="border-t border-zinc-100">
                  <td className="px-4 py-3 text-zinc-500">{r.id}</td>
                  <td className="px-4 py-3 font-medium text-zinc-900">{r.ad || r.adBolum}</td>
                  <td className="px-4 py-3">
                    <div className="flex justify-end gap-2">
                      <button
                        onClick={() => startEdit(r)}
                        className="rounded-xl px-3 py-1.5 bg-zinc-100 hover:bg-zinc-200"
                      >
                        Düzenle
                      </button>
                      <button
                        onClick={() => del(r)}
                        className="rounded-xl px-3 py-1.5 bg-red-50 hover:bg-red-100 text-red-700"
                      >
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
        title={editing ? `Bölüm Düzenle (#${editing.id})` : "Yeni Bölüm"}
        onClose={() => setOpen(false)}
      >
        <div className="grid grid-cols-1 gap-4">
          <Field label="Bölüm Adı">
            <Input value={form.ad} onChange={(e) => setForm({ ad: e.target.value })} />
          </Field>
        </div>

        <div className="mt-5 flex justify-end gap-2">
          <button
            onClick={() => setOpen(false)}
            className="rounded-2xl px-4 py-2 text-sm bg-zinc-100 hover:bg-zinc-200"
          >
            Vazgeç
          </button>
          <button
            onClick={save}
            className="rounded-2xl px-4 py-2 text-sm bg-zinc-900 text-white hover:bg-zinc-800"
          >
            Kaydet
          </button>
        </div>
      </Modal>
    </div>
  );
}
