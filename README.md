# MY-Portfolio

Structure:
- **Frontend/** — public portfolio website (React + Vite)
- **admin/** — admin dashboard to manage content (React + Vite)
- **Backend/** — Spring Boot API + MySQL (JWT auth, CRUD, resume upload/download)

## Quick Start

### 1. Backend
```
cd Backend
# edit src/main/resources/application.properties with your MySQL details
mvn spring-boot:run
```
Runs at http://localhost:8080

### 2. Frontend (public site)
```
cd Frontend
npm install
npm run dev
```
Runs at http://localhost:5173

### 3. Admin Panel
```
cd admin
npm install
npm run dev
```
Runs at http://localhost:5174 (or whatever port Vite shows — run it in a separate terminal from Frontend)

Login with the `admin.default.username` / `admin.default.password` you set in `Backend/src/main/resources/application.properties`.

Full details, deployment steps, and API reference: see **SETUP-GUIDE.md**.
