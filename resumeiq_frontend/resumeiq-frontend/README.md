# ResumeIQ Frontend

React + Vite frontend for ResumeIQ.

## Setup

```bash
npm install
cp .env.example .env   # edit VITE_API_BASE_URL if your backend isn't on localhost:8080
npm run dev
```

Runs on http://localhost:5173 by default - this must match the CORS origin
and the `app.oauth2.redirect-url` configured in the backend.

## Pages

- `/login`, `/register` - email/password auth, plus a "Continue with Google" button
- `/oauth2/redirect` - handles the redirect back from the backend after Google login
- `/dashboard` - upload a resume, add a job description, run the gap analysis
- `/analysis/:id` - score breakdown, missing keywords, weak bullets, confirm-and-optimize flow, PDF/DOCX export

## Notes

- The JWT is stored in `localStorage` under `resumeiq_token` and attached to
  every API call automatically (see `src/api/client.js`).
- File export goes through an authenticated `axios` request (blob response),
  not a plain link, since the export endpoint requires the JWT header.
- This has been built (`npm run build`) successfully against the API shapes
  currently in the backend, but has NOT been run against a live backend
  instance - test the full flow end-to-end before relying on it.
