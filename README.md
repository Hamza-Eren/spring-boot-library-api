<a name="english"></a>
> 🌍 **Choose Language / Dil Seçimi:** [English](#english) | [Türkçe](#turkish)

# 📚 Spring Boot Library Management API

This project is a comprehensive Library Management System REST API developed using Spring Boot 3+ and Java 21. The project features advanced library functionalities such as JWT-based authentication, role-based access control (e.g., ADMIN, AUTHOR), and a complete book borrowing system.

## 🚀 Features

- **JWT Authentication:** Secure login, user registration, and refresh token management.
- **Role-Based Access Control (RBAC):** API security secured with roles such as `ADMIN` and `AUTHOR`.
- **Book Management (CRUD):** Add, delete, update, and paginate books.
- **Author Management:** Manage authors in the system.
- **Borrowing System:**
  - Users can borrow available books.
  - Return books functionality.
  - Tracking of active borrowed books and borrowing history.
- **User Profiles:** Manage user details and profiles.
- **Database Migrations:** Safe and versioned database schema management for PostgreSQL using Flyway.
- **API Documentation:** Interactive API documentation using Swagger (OpenAPI) and Scalar UI.

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security & JWT** (`io.jsonwebtoken`)
- **Spring Data JPA** & **PostgreSQL**
- **Flyway** (Database Migration)
- **MapStruct** (Entity - DTO Mapping)
- **Lombok** (Boilerplate code reduction)
- **Docker & Docker Compose**
- **Swagger / Springdoc OpenAPI**

## 📂 Project Structure

The project is designed following Layered Architecture principles:
- `controller`: REST API endpoints.
- `service`: Layer where business rules are applied.
- `repository`: Spring Data JPA interfaces for database operations.
- `entity`: Models corresponding to database tables.
- `dto`: Data transfer objects between client and server.
- `mapper`: Entity - DTO conversions (using MapStruct).
- `security`: JWT configuration and Spring Security settings.

## ⚙️ Setup and Running

Follow the steps below to run the project in your local environment.

### 1. Setting Environment Variables

Create a `.env` file in the project root directory or edit an existing one as follows:

```env
DB_PASSWORD=your_db_password
DB_USERNAME=manager
DB_URL=jdbc:postgresql://postgres:5432/library_db
JWT_SECRET=your_secret_jwt_key_here
ADMIN_PASSWORD=admin123
```
> **Note:** For running locally outside of Docker, the `postgres` address in the `DB_URL` should be changed to `localhost`.

### 2. Quick Start with Docker Compose (Recommended)

You can spin up PostgreSQL, Adminer, and the Spring Boot application with a single command using the `docker-compose.yml` file:

```bash
docker-compose up -d --build
```
After this command, the API will be available at `http://localhost:8080`.

### 3. Running via IDE with Docker Database Only

If you only want to start the PostgreSQL and Adminer (database management tool) services:
```bash
docker-compose up -d postgres adminer
```
Once the database is up, you can run the `Application.java` file via your IDE (IntelliJ IDEA, etc.). Flyway will automatically create the tables.
> *To access Adminer:* `http://localhost:8081`

## 📄 API Documentation

After the application starts successfully, you can navigate to the following addresses to test the endpoints:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Scalar UI:** `http://localhost:8080/scalar`

## 🔐 Default Roles and Permissions
- **ADMIN:** Has all administrative permissions in the system. Can delete books or add books on behalf of others.
- **AUTHOR:** Can add and edit their own books.
- **USER:** Can log in, view active books, and borrow books.

---

<a name="turkish"></a>
# 📚 Spring Boot Library Management API (Türkçe)

Bu proje, Spring Boot 3+ ve Java 21 kullanılarak geliştirilmiş, kapsamlı bir Kütüphane Yönetim Sistemi REST API'sidir. Projede JWT tabanlı kimlik doğrulama, rol bazlı yetkilendirme (ADMIN, AUTHOR vb.) ve kitap ödünç alma işlemleri gibi gelişmiş kütüphane fonksiyonları yer almaktadır.

## 🚀 Özellikler

- **JWT Kimlik Doğrulama:** Güvenli giriş (login), kayıt olma (register) ve refresh token yönetimi.
- **Rol Bazlı Erişim (RBAC):** `ADMIN` ve `AUTHOR` gibi rollerle API güvenliği.
- **Kitap Yönetimi (CRUD):** Kitap ekleme, silme, güncelleme ve sayfalı (pagination) listeleme.
- **Yazar Yönetimi:** Sistemdeki yazarların yönetimi.
- **Ödünç Alma Sistemi:**
  - Kullanıcıların uygun durumdaki kitapları ödünç alması.
  - Kitap iade etme işlemleri.
  - Aktif olarak elde bulunan ve geçmişte okunan kitapların takibi.
- **Kullanıcı Profilleri:** Kullanıcı detaylarının ve profillerinin yönetimi.
- **Veritabanı Göçleri (Migration):** Flyway kullanılarak PostgreSQL veritabanı şemasının güvenli ve versiyonlu yönetimi.
- **API Dokümantasyonu:** Swagger (OpenAPI) ve Scalar UI ile interaktif API dokümantasyonu.

## 🛠️ Kullanılan Teknolojiler

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security & JWT** (`io.jsonwebtoken`)
- **Spring Data JPA** & **PostgreSQL**
- **Flyway** (Database Migration)
- **MapStruct** (DTO - Entity Dönüşümleri)
- **Lombok** (Boilerplate kod azaltımı)
- **Docker & Docker Compose**
- **Swagger / Springdoc OpenAPI**

## 📂 Proje Yapısı

Proje katmanlı mimari (Layered Architecture) prensiplerine uygun olarak tasarlanmıştır:
- `controller`: REST API uç noktaları.
- `service`: İş kurallarının (business logic) uygulandığı katman.
- `repository`: Veritabanı işlemleri için Spring Data JPA arayüzleri.
- `entity`: Veritabanı tablolarına karşılık gelen modeller.
- `dto`: İstemci ile sunucu arasındaki veri transfer objeleri.
- `mapper`: Entity - DTO dönüşümleri (MapStruct ile).
- `security`: JWT yapılandırması ve Spring Security konfigürasyonları.

## ⚙️ Kurulum ve Çalıştırma

Projeyi yerel ortamınızda ayağa kaldırmak için aşağıdaki adımları izleyebilirsiniz.

### 1. Ortam Değişkenlerini (Environment Variables) Ayarlama

Proje dizininde bir `.env` dosyası oluşturun veya mevcut olanı aşağıdaki gibi düzenleyin:

```env
DB_PASSWORD=kendi_db_sifreniz
DB_USERNAME=manager
DB_URL=jdbc:postgresql://postgres:5432/library_db
JWT_SECRET=gizli_jwt_anahtariniz_buraya_gelecek
ADMIN_PASSWORD=admin123
```
> **Not:** Docker dışı (lokal) çalıştırmalarda `DB_URL` kısmındaki `postgres` adresi `localhost` olarak değiştirilmelidir.

### 2. Docker Compose ile Hızlı Başlangıç (Önerilen)

Proje dizininde bulunan `docker-compose.yml` dosyası sayesinde PostgreSQL, Adminer ve Spring Boot uygulamasını tek bir komutla ayağa kaldırabilirsiniz:

```bash
docker-compose up -d --build
```
Bu komut sonrası API, `http://localhost:8080` adresinde hizmet vermeye başlayacaktır.

### 3. Sadece Veritabanını Docker ile Kaldırıp IDE'den Çalıştırmak

Sadece PostgreSQL ve Adminer (veritabanı yönetim aracı) servislerini başlatmak isterseniz:
```bash
docker-compose up -d postgres adminer
```
Veritabanı ayağa kalktıktan sonra, IDE'niz üzerinden (IntelliJ IDEA vb.) `Application.java` dosyasını çalıştırabilirsiniz. Flyway tabloları otomatik olarak oluşturacaktır.
> *Adminer'a ulaşmak için:* `http://localhost:8081`

## 📄 API Dokümantasyonu

Uygulama başarılı bir şekilde çalıştıktan sonra, uç noktaları test etmek için aşağıdaki adreslere gidebilirsiniz:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Scalar UI:** `http://localhost:8080/scalar`

## 🔐 Varsayılan Roller ve Yetkiler
- **ADMIN:** Sistemdeki tüm yönetimsel yetkilere sahiptir. Kitap silebilir veya başkası adına kitap ekleyebilir.
- **AUTHOR:** Kendi kitaplarını ekleyebilir ve düzenleyebilir.
- **KULLANICI:** Sisteme giriş yapıp aktif kitapları görüntüleyebilir ve kitap ödünç alabilir.
