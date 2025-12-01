# 🚀 Deployment Guide for Railway

This guide explains how to deploy the **Universe Odyssey** Spring Boot application to [Railway](https://railway.app/).

## Prerequisites
- A GitHub account with this repository pushed.
- A [Railway](https://railway.app/) account (Free tier available).

## 1. Setup Project on Railway
1.  Log in to Railway.
2.  Click **"New Project"** > **"Deploy from GitHub repo"**.
3.  Select your `universe-odyssey` repository.
4.  Click **"Deploy Now"**.

## 2. Add a Database (MySQL)
1.  In your Railway project dashboard, click **"New"** (or right-click the canvas).
2.  Select **"Database"** > **"MySQL"**.
3.  Wait for the MySQL service to initialize.

## 3. Configure Environment Variables
1.  Click on your **Spring Boot Application** service card.
2.  Go to the **"Variables"** tab.
3.  Add the following variables:

    | Variable Name | Value | Description |
    | :--- | :--- | :--- |
    | `SPRING_PROFILES_ACTIVE` | `prod` | Activates `application-prod.properties` |
    | `NASA_API_KEY` | `YOUR_KEY` | Your NASA API Key |
    | `GEMINI_API_KEY` | `YOUR_KEY` | Your Google Gemini API Key |
    | `PORT` | `8080` | (Optional) Port number |

4.  **Link the Database:**
    - While still in the "Variables" tab, look for "Variable Reference" or simply use the Railway magic variables.
    - Railway automatically provides `MYSQL_URL`, `MYSQLUSER`, `MYSQLPASSWORD`, etc., to your app service if they are in the same project.
    - **Verify**: Check that `MYSQL_URL` is present in the "Raw Editor" or "Shared Variables".

## 4. Verify Deployment
1.  Go to the **"Settings"** tab of your application service.
2.  Under **"Networking"**, click **"Generate Domain"** to get a public URL (e.g., `universe-odyssey-production.up.railway.app`).
3.  Visit the URL. Your app should be live!

## Troubleshooting

### Database Connection Fails?
- Ensure `spring.datasource.url` in `application-prod.properties` matches the format Railway provides.
- Our config uses: `jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}` which maps to Railway's standard variables.

### Build Fails?
- Check the **"Build Logs"**.
- Ensure your `pom.xml` is in the root directory.
- Ensure you are using Java 21 (defined in `Dockerfile`).

### App Crashes on Start?
- Check **"Deploy Logs"**.
- Common issue: Missing API Keys. Ensure `NASA_API_KEY` and `GEMINI_API_KEY` are set in Railway variables.
