# Sistem Terdistribusi - Reservasi Tiket (API Gateway & Microservices)

Proyek implementasi arsitektur microservices untuk tema **"Reservasi Tiket"** menggunakan **Java 21** dan **Spring Boot**, dilengkapi dengan **Spring Cloud Gateway (WebFlux)** sebagai pintu gerbang tunggal (Single Entry Point).

---

## 1. Daftar Service, Port, dan Path

| No | Service | Port | Path Controller | Deskripsi Singkat |
|---|---|---|---|---|
| 1 | `gateaway` | `8000` | `/api/**` | Spring Cloud Gateway (Reactive/WebFlux) merutekan request ke microservices |
| 2 | `user-service` | `8101` | `/api/users` | Pengelolaan data pengguna/pelanggan |
| 3 | `schedule-service`| `8102` | `/api/schedules` | Pengelolaan jadwal keberangkatan & tarif tiket |
| 4 | `booking-service` | `8103` | `/api/bookings` | Reservasi tiket & validasi jadwal ke `schedule-service` via `RestClient` |
| 5 | `seat-service` | `8104` | `/api/seats` | Pengelolaan data ketersediaan kursi kendaraan |
| 6 | `payment-service` | `8105` | `/api/payments` | Pemrosesan dan pencatatan riwayat pembayaran |
| 7 | `notification-service` | `8106` | `/api/notifications` | Pengiriman status & notifikasi reservasi/pembayaran |

---

## 2. Struktur Folder

```text
Reservasi Tiket_API Gateaway/
│
├── gateaway/               # API Gateway (Port 8000)
├── user-service/           # Microservice Pengguna (Port 8101)
├── schedule-service/       # Microservice Jadwal (Port 8102)
├── booking-service/        # Microservice Pemesanan (Port 8103)
├── seat-service/           # Microservice Kursi (Port 8104)
├── payment-service/        # Microservice Pembayaran (Port 8105)
├── notification-service/   # Microservice Notifikasi (Port 8106)
├── start-all.bat           # Script batch untuk menjalankan semua service sekaligus
└── README.md               # Dokumentasi proyek
```

---

## 3. Catatan Penting Konfigurasi

1. **Spring Cloud Gateway Reactive Routes:**
   Pada Spring Boot 4.x / Spring Cloud 2025.x, konfigurasi routing wajib ditempatkan di bawah:
   ```yaml
   spring:
     cloud:
       gateway:
         server:
           webflux:
             routes:
               - id: user-service
                 uri: http://localhost:8101
                 predicates:
                   - Path=/api/users/**
   ```
2. **Java Runtime:**
   Semua service dikompilasi menggunakan Java 21. `start-all.bat` telah dilengkapi deteksi otomatis JDK 21.
3. **Komunikasi Antar Service:**
   `booking-service` menggunakan `org.springframework.web.client.RestClient` untuk memvalidasi `scheduleId` ke `schedule-service` (`http://localhost:8102/api/schedules/{id}`) sebelum menyimpan data booking di memori.

---

## 4. Cara Menjalankan

### Opsi A: Menjalankan Sekaligus (Rekomendasi)
Jalankan file batch `start-all.bat` dari File Explorer atau Terminal:
```cmd
.\start-all.bat
```
Script ini akan membuka terminal terpisah untuk masing-masing service (microservice terlebih dahulu, lalu Gateway terakhir).

### Opsi B: Menjalankan Manual per Service
Buka terminal di masing-masing folder service dan jalankan:
```powershell
.\mvnw.cmd spring-boot:run
```

Urutan yang disarankan:
1. `schedule-service`
2. `user-service`, `seat-service`, `payment-service`, `notification-service`, `booking-service`
3. `gateaway`

---

## 5. Contoh Request (API Testing)

Semua endpoint dapat diuji melalui **API Gateway (`http://localhost:8000`)** maupun langsung ke **port service**.

### A. User Service (Port 8101 / via Gateway: 8000)
- **Get All Users:**
  ```bash
  curl http://localhost:8000/api/users
  ```
- **Get User by ID:**
  ```bash
  curl http://localhost:8000/api/users/1
  ```

---

### B. Schedule Service (Port 8102 / via Gateway: 8000)
- **Get All Schedules:**
  ```bash
  curl http://localhost:8000/api/schedules
  ```
- **Get Schedule by ID:**
  ```bash
  curl http://localhost:8000/api/schedules/1
  ```

---

### C. Booking Service (Port 8103 / via Gateway: 8000)
- **Get All Bookings:**
  ```bash
  curl http://localhost:8000/api/bookings
  ```
- **Get Booking by ID:**
  ```bash
  curl http://localhost:8000/api/bookings/1
  ```
- **Create New Booking (Inter-service validation ke schedule-service):**
  ```bash
  curl -X POST http://localhost:8000/api/bookings \
    -H "Content-Type: application/json" \
    -d "{\"scheduleId\": 1, \"seatNumber\": \"15C\", \"userId\": 1}"
  ```

---

### D. Seat Service (Port 8104 / via Gateway: 8000)
- **Get All Seats:**
  ```bash
  curl http://localhost:8000/api/seats
  ```
- **Get Seat by ID:**
  ```bash
  curl http://localhost:8000/api/seats/1
  ```

---

### E. Payment Service (Port 8105 / via Gateway: 8000)
- **Get All Payments:**
  ```bash
  curl http://localhost:8000/api/payments
  ```
- **Get Payment by ID:**
  ```bash
  curl http://localhost:8000/api/payments/1
  ```
- **Create Payment:**
  ```bash
  curl -X POST http://localhost:8000/api/payments \
    -H "Content-Type: application/json" \
    -d "{\"bookingId\": 3, \"amount\": 120000, \"paymentMethod\": \"QRIS\"}"
  ```

---

### F. Notification Service (Port 8106 / via Gateway: 8000)
- **Get All Notifications:**
  ```bash
  curl http://localhost:8000/api/notifications
  ```
- **Get Notification by ID:**
  ```bash
  curl http://localhost:8000/api/notifications/1
  ```
