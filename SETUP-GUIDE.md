# Pushpak Portfolio — Setup & Run Guide

Three pieces:
1. **portfolio.jsx** — the public portfolio website (React)
2. **admin-panel.jsx** — the admin dashboard to add/edit/delete everything (React)
3. **portfolio-backend.zip** — Spring Boot + MySQL API that both talk to

---

## 1. Backend Setup (Spring Boot + MySQL)

### Prerequisites
- Java 17+ installed (`java -version`)
- Maven installed (`mvn -version`) — or use the included `mvnw` if present
- MySQL installed and running (or a free cloud MySQL — see Deployment section)

### Steps
1. Unzip `portfolio-backend.zip`
2. Create the database:
   ```sql
   CREATE DATABASE portfolio_db;
   ```
3. Open `src/main/resources/application.properties` and set your real DB credentials:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db?useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```
4. **Important — change these before running:**
   ```
   jwt.secret=<put a long random string here>
   admin.default.username=admin
   admin.default.password=<choose your own admin password>
   ```
   This admin account is auto-created the first time the app starts. Login with these credentials on the admin panel.
5. Run it:
   ```bash
   cd portfolio-backend
   mvn spring-boot:run
   ```
6. Backend runs at `http://localhost:8080`. Tables (profile, skill, experience, project, certification, contact_message, admin) are created automatically (`ddl-auto=update`).
7. Your resume PDF is already placed at `uploads/resume.pdf` — downloadable immediately at `http://localhost:8080/api/public/resume`. Replace it anytime from the admin panel's Resume tab.

### First-time content
The database starts empty (except your resume + admin login). Log into the admin panel and add your Skills, Experience, Projects, and Certifications, and fill in the Profile tab — the public site pulls all of this live from the API.

---

## 2. Admin Panel Setup

`admin-panel.jsx` is a React component. To run it:

1. Create a React app if you don't have one yet:
   ```bash
   npm create vite@latest portfolio-admin -- --template react
   cd portfolio-admin
   npm install
   ```
2. Replace `src/App.jsx` with the contents of `admin-panel.jsx`
3. In `admin-panel.jsx`, update this line to match your backend:
   ```js
   const API_BASE = "http://localhost:8080";
   ```
4. Run:
   ```bash
   npm run dev
   ```
5. Open the shown localhost URL → log in with the admin username/password you set in `application.properties`.

**Tabs available:** Profile, Skills, Experience, Projects, Certifications, Resume (upload new PDF), Messages (contact form submissions) — full Add / Edit / Delete on each.

---

## 3. Public Portfolio Setup

1. Same as above — create a Vite React app (or reuse one), replace `src/App.jsx` with `portfolio.jsx`
2. Update the API URL near the top of the file:
   ```js
   const API_BASE = "http://localhost:8080";
   ```
3. `npm run dev` and open the localhost URL.

The site fetches Skills/Experience/Projects/Certifications from the backend on load. If the backend isn't running, it silently falls back to the built-in resume data so the site never looks broken.

---

## 4. Free Deployment (as you asked for)

| Piece | Where | Notes |
|---|---|---|
| Backend | **Render.com** (free tier) | New → Web Service → connect your GitHub repo containing the backend → build command `mvn clean install`, start command `java -jar target/portfolio-1.0.0.jar` |
| Database | **Railway.app** or **Clever Cloud** (free MySQL) | Copy the connection URL/username/password into `application.properties` (or Render's environment variables) |
| Frontend (portfolio + admin) | **Netlify** | Push each React app to GitHub → New site from Git → build command `npm run build`, publish directory `dist` |

**Before deploying:**
- Update `app.cors.allowed-origins` in `application.properties` to your real Netlify URL(s)
- Update `API_BASE` in both `portfolio.jsx` and `admin-panel.jsx` to your real Render backend URL
- Move secrets (`jwt.secret`, DB password, admin password) into environment variables on Render instead of leaving them in the file

## 5. Making it appear on Google
- Once deployed, submit your Netlify URL to **Google Search Console**
- Add a `sitemap.xml` and `robots.txt` to the frontend's `public/` folder
- Add proper `<title>` and `<meta name="description">` tags in `index.html`
- This part isn't built yet — say the word and I'll add the meta tags + sitemap next.

---

## Quick Reference — API Endpoints

| Method | Endpoint | Auth | Purpose |
|---|---|---|---|
| POST | `/api/auth/login` | — | Admin login, returns JWT |
| GET | `/api/public/profile` | — | Public profile data |
| GET | `/api/public/skills` / `/experience` / `/projects` / `/certifications` | — | Public content |
| GET | `/api/public/resume` | — | Download resume PDF |
| POST | `/api/public/contact` | — | Submit contact form |
| GET/POST/PUT/DELETE | `/api/admin/skills`, `/experience`, `/projects`, `/certifications` | JWT required | CRUD from admin panel |
| PUT | `/api/admin/profile` | JWT required | Update profile |
| POST | `/api/admin/resume` | JWT required | Replace resume PDF |
| GET/DELETE | `/api/admin/messages` | JWT required | View/delete contact messages |
