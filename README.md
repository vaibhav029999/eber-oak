# EBER & OAK

A full-stack neighborhood food ordering project. The React frontend talks to a Java Spring Boot REST API, which stores accounts, restaurant menus, carts, addresses and orders in MySQL.

## Technology

- Frontend: React with JavaScript, Vite and plain CSS
- Backend: Java 17, Spring Boot, Spring Web, Spring Data JPA and Spring Security
- Database: MySQL 8
- Authentication: BCrypt password hashes and signed bearer tokens
- Payments: Cash on delivery and a clearly marked online-payment simulation; no payment gateway is connected

## Run it locally

Install Java 17+, Maven 3.6.3+, MySQL 8+, and Node.js 22.12+ with npm. Start MySQL before the API.

### 1. Create the database and sample menu

From this project folder, run the SQL script in PowerShell (MySQL will prompt for the selected account’s password):

```powershell
$OutputEncoding = [System.Text.UTF8Encoding]::new()
Get-Content -Encoding UTF8 .\database\eber_oak_db.sql | mysql --default-character-set=utf8mb4 -u root -p
```

This creates `eber_oak_db` and inserts six sample restaurants, menu items, categories and offer codes. If the MySQL account is not `root`, replace `root` with its username.

### 2. Configure and start Spring Boot

In a terminal, switch into `backend` and set your database credentials. The defaults connect as `root` with no password, so set `DB_PASSWORD` if your MySQL account uses one.

```powershell
cd .\backend
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-mysql-password"
mvn spring-boot:run
```

The API starts at `http://localhost:8080`. You can check it at `http://localhost:8080/api/health`.

The first startup creates the administrator account if it does not exist:

- Email: `admin@eberandoak.local`
- Password: `OakAdmin2026!`

Set `ADMIN_EMAIL` and `ADMIN_PASSWORD` before starting Spring Boot to choose different local credentials. The project defaults and sample account are for local development; change them before any deployment.

For an IntelliJ IDEA setup, open `backend/pom.xml` as a Maven project, set `DB_USERNAME` / `DB_PASSWORD` in the run configuration, and run `EberOakApplication`.

### 3. Start React

In a second terminal:

```powershell
cd .\frontend
npm install
npm run dev
```

Open `http://localhost:5173`. Vite forwards `/api` requests to Spring Boot on port 8080. Keep the API and MySQL running while using the website.

## Try the application

1. Open **Restaurants**, choose a kitchen and add menu items. A bag can contain dishes from one restaurant at a time.
2. Create an account or sign in. Registration stores a BCrypt password hash in MySQL; the API returns a signed token for subsequent requests.
3. Open **Cart**, change quantities or try `OAKFIRST`, `GOODTABLE` (₹499 minimum), or `LUNCHLOVE` (₹299 minimum).
4. Continue to checkout, enter a delivery address and choose cash on delivery or the demo payment simulation. Placing the order persists a snapshot and clears the cart.
5. Open **Your orders** to see order details and status.
6. Sign in as the administrator above and open `/admin` to manage restaurants, menu items, offers, customers and order status.

Order statuses are `PLACED`, `PREPARING`, `OUT_FOR_DELIVERY`, and `DELIVERED`. The dashboard shows these as readable labels.

## Main API routes

| Area | Routes |
| --- | --- |
| Health | `GET /api/health` |
| Authentication | `POST /api/auth/register`, `POST /api/auth/login`, `GET /api/auth/me`, `POST /api/auth/logout` |
| Restaurants and menus | `GET /api/restaurants`, `GET /api/restaurants/{id}`, `GET /api/restaurants/{id}/menu`, `GET /api/categories` |
| Cart | `GET /api/cart`, `POST /api/cart/items`, `PATCH /api/cart/items/{id}`, `DELETE /api/cart/items/{id}` |
| Offers | `GET /api/coupons`, `POST /api/coupons/validate` |
| Orders | `POST /api/orders`, `GET /api/orders`, `GET /api/orders/{id}` |
| Admin | `/api/admin/restaurants`, `/api/admin/foods`, `/api/admin/orders`, `/api/admin/customers`, `/api/admin/coupons` |

Admin APIs require the `ADMIN` role. Customer routes require a bearer token. Public catalog and coupon reads work without signing in.

## Configuration

The API reads configuration from environment variables, with local defaults in `backend/src/main/resources/application.properties`:

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/eber_oak_db?...` | MySQL connection string |
| `DB_USERNAME` | `root` | MySQL account |
| `DB_PASSWORD` | blank | MySQL password |
| `FRONTEND_ORIGIN` | `http://localhost:5173` | Allowed browser origin |
| `JWT_SECRET` | Local development key | Base64 encoded signing secret; use your own long random value outside local development |
| `JWT_EXPIRATION_MS` | `86400000` | Token lifetime (24 hours) |
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | `admin@eberandoak.local` / `OakAdmin2026!` | Initial local administrator, created only if the email is absent |

The SQL sample images load from Unsplash URLs and need internet access in the browser. Restaurant and menu records themselves come from MySQL.

## Folder guide

- `frontend/src/pages/` — home, listings, menu, login, cart, checkout, orders and admin screens
- `frontend/src/components/` — shared navigation, footer, loading and feedback components
- `frontend/src/services/api.js` — JSON Fetch API client and bearer token handling
- `backend/src/main/java/com/eberandoak/api/controller/` — REST endpoints
- `backend/src/main/java/com/eberandoak/api/service/` — application and order logic
- `backend/src/main/java/com/eberandoak/api/repository/` — Spring Data JPA repositories
- `backend/src/main/java/com/eberandoak/api/entity/` — relational entities and enums
- `backend/src/main/java/com/eberandoak/api/dto/` — validated API request and response records
- `backend/src/main/java/com/eberandoak/api/security/` — password hashing, bearer-token auth and role checks
- `database/eber_oak_db.sql` — MySQL schema and sample data

## Notes

- No Docker, external identity provider or payment processor is used.
- The cart and order totals are calculated by Spring Boot. The browser displays the same line items, but does not set the price recorded in an order.
- Orders are scoped to the signed-in account. Deleting a restaurant or dish from the admin screen archives it so existing orders keep their history.
- If the home screen says it cannot reach the menu service, start MySQL, run the SQL file, then restart Spring Boot and refresh the page.
