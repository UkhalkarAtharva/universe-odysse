Environment & NASA API Key

The application expects the NASA API key to be configured server-side only. You can set it either in `src/main/resources/application.properties` or as an environment variable.

1) Set in `application.properties` (already present):

   nasa.api.key=5wrBMBypIPJxv6YsCbLEFwSeBcaSbYVB5LbcdeYh

   This is safe because the key is read by the Spring Boot backend and never sent to the browser.

2) Or set as an environment variable (recommended for production):

   In PowerShell (temporary for current session):

```powershell
$env:NASA_API_KEY = '5wrBMBypIPJxv6YsCbLEFwSeBcaSbYVB5LbcdeYh'
```

   To have Spring Boot pick up the env var without changing code, you can pass it as a JVM property on startup:

```powershell
mvnw.cmd spring-boot:run -Dspring-boot.run.jvmArguments="-Dnasa.api.key=$env:NASA_API_KEY"
```

   Or set the env var system-wide via Windows Environment Variables (Control Panel) and restart the service/IDE.

Notes:
- DO NOT include the NASA key in any frontend JS or commit it to public repos.
- The backend reads the property using `@Value("${nasa.api.key}")` and uses it only for server-side augmenting of data.
