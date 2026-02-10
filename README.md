# booking-calendar

## Backend API (Ktor)

Run the API server on port 8080:

```bash
./gradlew runServer
```

The API exposes:
- `GET /api/bookings`
- `POST /api/bookings`

## Web frontend (Vue)

```bash
cd web
npm install
npm run dev
```

The dev server runs on port 5173 and proxies `/api` to the Ktor server.
