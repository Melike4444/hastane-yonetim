#!/usr/bin/env bash
set -e

BASE="http://localhost:8080"

echo "== Departments =="
curl -s -X POST "$BASE/api/bolumler" -H "Content-Type: application/json" -d '{"ad":"Kardiyoloji","aciklama":"Kalp ve damar"}' || true
curl -s -X POST "$BASE/api/bolumler" -H "Content-Type: application/json" -d '{"ad":"Dahiliye","aciklama":"Ic hastaliklari"}' || true
echo

echo "== Patients =="
curl -s -X POST "$BASE/api/hastalar" -H "Content-Type: application/json" -d '{"ad":"Ali","soyad":"Yilmaz","telefon":"05550000000"}' || true
curl -s -X POST "$BASE/api/hastalar" -H "Content-Type: application/json" -d '{"ad":"Ayse","soyad":"Kaya","telefon":"05551112233"}' || true
echo

echo "== Doctors =="
curl -s -X POST "$BASE/api/doktorlar" -H "Content-Type: application/json" -d '{"ad":"Ahmet","soyad":"Demir","brans":"Kardiyoloji","departmentId":1}' || true
curl -s -X POST "$BASE/api/doktorlar" -H "Content-Type: application/json" -d '{"ad":"Mehmet","soyad":"Yildiz","brans":"Dahiliye","departmentId":2}' || true
echo

echo "== Appointments =="
curl -s -X POST "$BASE/api/randevular" -H "Content-Type: application/json" -d '{"tarihSaat":"2026-01-10T14:30:00","hastaId":1,"doktorId":1}' || true
curl -s -X POST "$BASE/api/randevular" -H "Content-Type: application/json" -d '{"tarihSaat":"2026-01-12T10:00:00","hastaId":2,"doktorId":3}' || true
echo

echo "== Examinations =="
curl -s -X POST "$BASE/api/muayeneler" -H "Content-Type: application/json" -d '{"randevuId":1,"sikayet":"Gogus agrisi","teshis":"Hipertansiyon","tedavi":"Ilac","notlar":"2 hafta kontrol"}' || true
curl -s -X POST "$BASE/api/muayeneler" -H "Content-Type: application/json" -d '{"randevuId":2,"sikayet":"Halsizlik","teshis":"Demir eksikligi","tedavi":"Takviye","notlar":"1 ay sonra kontrol"}' || true
echo

echo "DONE ✅"
