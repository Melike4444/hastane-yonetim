# Hastane Yönetim Sistemi – YDG Projesi

Bu proje, Yazılım Doğrulama ve Geçerleme (YDG) dersi kapsamında geliştirilmiş,
CI/CD süreçleri ve test otomasyonu içeren kapsamlı bir hastane yönetim sistemidir.

## 🚀 Kullanılan Teknolojiler
- Backend: Spring Boot (Java)
- Frontend: React + Tailwind CSS
- Veritabanı: H2 / JPA
- CI/CD: Jenkins
- Testler:
  - Unit Test
  - Integration Test
  - Selenium (UI / Senaryo Testleri)
- Container: Docker, Docker Compose

## 📦 Sistem Bileşenleri
- Hasta Yönetimi
- Doktor Yönetimi
- Randevu Yönetimi
- Muayene Yönetimi
- Bölüm (Department) Yönetimi

## 🔁 CI/CD Süreci (Jenkins)
Pipeline aşağıdaki adımları otomatik olarak gerçekleştirir:
1. GitHub’dan kodların çekilmesi
2. Maven ile build alınması
3. Unit testlerin çalıştırılması ve raporlanması
4. Integration testlerin çalıştırılması ve raporlanması
5. Docker container’ların ayağa kaldırılması (ortama bağlı)
6. Selenium ile 3 ayrı test senaryosunun çalıştırılması

## 🧪 Selenium Test Senaryoları
- Senaryo 1: Uygulama açılıyor mu?
- Senaryo 2: Doktorlar endpoint kontrolü
- Senaryo 3: Randevular endpoint kontrolü

Her senaryo Jenkins pipeline içerisinde ayrı bir stage olarak çalıştırılmaktadır.

## 🐳 Docker Kullanımı
Sistem Docker container’ları üzerinde çalıştırılabilir:

```bash
docker compose up -d --build

