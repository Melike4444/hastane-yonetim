const BASE = import.meta.env.VITE_API_URL || "http://localhost:8080";

async function req(path, options = {}) {
  const url = path.startsWith("http") ? path : `${BASE}${path}`;

  const res = await fetch(url, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options,
  });

  // 204 No Content → body yok, başarılı kabul et
  if (res.status === 204) return null;

  // Bazı backendler 200 dönüp boş body gönderebiliyor → güvenli oku
  const text = await res.text();
  const data = text ? (() => { try { return JSON.parse(text); } catch { return text; } })() : null;

  if (!res.ok) {
    // Backend JSON error döndürüyorsa message/path vb. çıkar
    const msg =
      (data && typeof data === "object" && (data.message || data.error)) ||
      (typeof data === "string" && data) ||
      `HTTP ${res.status}`;
    throw new Error(msg);
  }

  return data;
}

export const api = {
  // Hastalar
  listHastalar: () => req("/api/hastalar"),
  createHasta: (body) => req("/api/hastalar", { method: "POST", body: JSON.stringify(body) }),
  updateHasta: (id, body) => req(`/api/hastalar/${id}`, { method: "PUT", body: JSON.stringify(body) }),
  deleteHasta: (id) => req(`/api/hastalar/${id}`, { method: "DELETE" }),

  // Bölümler
  listBolumler: () => req("/api/bolumler"),
  createBolum: (body) => req("/api/bolumler", { method: "POST", body: JSON.stringify(body) }),
  updateBolum: (id, body) => req(`/api/bolumler/${id}`, { method: "PUT", body: JSON.stringify(body) }),
  deleteBolum: (id) => req(`/api/bolumler/${id}`, { method: "DELETE" }),

  // Doktorlar
  listDoktorlar: () => req("/api/doktorlar"),
  createDoktor: (body) => req("/api/doktorlar", { method: "POST", body: JSON.stringify(body) }),
  updateDoktor: (id, body) => req(`/api/doktorlar/${id}`, { method: "PUT", body: JSON.stringify(body) }),
  deleteDoktor: (id) => req(`/api/doktorlar/${id}`, { method: "DELETE" }),

  // Randevular
  listRandevular: () => req("/api/randevular"),
  createRandevu: (body) => req("/api/randevular", { method: "POST", body: JSON.stringify(body) }),
  updateRandevu: (id, body) => req(`/api/randevular/${id}`, { method: "PUT", body: JSON.stringify(body) }),
  deleteRandevu: (id) => req(`/api/randevular/${id}`, { method: "DELETE" }),

  // Muayeneler
  listMuayeneler: () => req("/api/muayeneler"),
  createMuayene: (body) => req("/api/muayeneler", { method: "POST", body: JSON.stringify(body) }),
  updateMuayene: (id, body) => req(`/api/muayeneler/${id}`, { method: "PUT", body: JSON.stringify(body) }),
  deleteMuayene: (id) => req(`/api/muayeneler/${id}`, { method: "DELETE" }),
};
