# Quiz Feature Deployment Notes

Backend (Render):
- Dockerfile present. Build and push image with the Maven built jar.
- Environment variables:
  - SPRING_DATASOURCE_URL
  - SPRING_DATASOURCE_USERNAME
  - SPRING_DATASOURCE_PASSWORD
  - GEMINI_API_KEY
  - FRONTEND_URL
- Start command:
  ```bash
  java -jar /app/app.jar
  ```

Frontend (Vercel):
- Add env var `REACT_APP_API_BASE=https://<render-host>/api`
- Build and deployment: standard Vercel deployment for CRA/Vite.

Notes:
- Keep the GEMINI_API_KEY secret in backend only.
- Run Flyway migrations before starting the app.
