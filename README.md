# 📚 Spring Boot Library Management API

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
