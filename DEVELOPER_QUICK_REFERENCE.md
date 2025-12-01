# 🔧 Developer Quick Reference Card

**Print this card for quick reference!**

---

## ⚡ Quick Commands

### Build & Run
```bash
# Clean build
mvnw clean package

# Run application
java -jar target/universe-odyssey-0.0.1-SNAPSHOT.jar

# Build and run (one line)
mvnw clean package && java -jar target/universe-odyssey-0.0.1-SNAPSHOT.jar
```

### Database
```sql
-- Create database
CREATE DATABASE universe_odyssey;

-- Connect
USE universe_odyssey;

-- View tables
SHOW TABLES;

-- Query planets
SELECT * FROM planet_detail;

-- Query missions
SELECT * FROM space_mission;
```

---

## 🌐 API Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/planets/data/{name}` | Get planet data (multi-source) |
| GET | `/api/planets` | Get all planets |
| GET | `/api/planets/{id}` | Get planet by ID |
| GET | `/api/missions` | Get all missions |
| GET | `/api/missions/{id}` | Get mission by ID |

### Example Calls
```bash
# Get Earth data
http://localhost:8080/api/planets/data/earth

# Get all missions
http://localhost:8080/api/missions

# Get mission 1
http://localhost:8080/api/missions/1
```

---

## 🔑 Key Files

| File | Purpose | Lines |
|------|---------|-------|
| `ExternalPlanetDataService.java` | Multi-source data fetching | 200+ |
| `PlanetRestController.java` | Planet API endpoints | 50 |
| `SpaceMissionRestController.java` | Mission API endpoints | 25 |
| `RestTemplateConfig.java` | HTTP client config | 20 |
| `PlanetDetailResponse.java` | Response DTO | 150+ |
| `application.properties` | App configuration | 50 |

---

## 🎯 Key Classes & Methods

### ExternalPlanetDataService
```java
// Get planet data (multi-source with fallback)
PlanetDetailResponse getPlanetData(String planetName)

// Internal methods
PlanetDetailResponse fetchFromOpenSolarSystem(String name)
void augmentWithNasaData(PlanetDetailResponse response, String name)
void cacheInDatabase(PlanetDetailResponse response)
```

### PlanetRestController
```java
@GetMapping("/data/{name}")
public ResponseEntity<PlanetDetailResponse> getPlanetData(String name)
```

### SpaceMissionRestController
```java
@GetMapping
public List<SpaceMission> getAllMissions()

@GetMapping("/{id}")
public SpaceMission getMissionById(Long id)
```

---

## 🐛 Debugging Tips

### Check Logs
```bash
# Look for errors
grep -i error catalina.out

# Find specific class logs
grep ExternalPlanetDataService catalina.out
```

### Test Endpoints
```bash
# Using curl
curl -X GET http://localhost:8080/api/planets/data/earth

# Using PowerShell
(Invoke-WebRequest -Uri "http://localhost:8080/api/planets/data/earth").Content
```

### Browser Console
- Press F12 to open developer tools
- Check Console tab for JavaScript errors
- Check Network tab to see API calls
- Check Application tab to see stored data

---

## 📊 Configuration Reference

### application.properties Key Settings
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/universe_odyssey
spring.datasource.username=root
spring.datasource.password=Admin@123

# API Key (Server-side only)
nasa.api.key=5wrBMBypIPJxv6YsCbLEFwSeBcaSbYVB5LbcdeYh

# Cache
spring.cache.type=simple
spring.cache.cache-names=planets

# JPA
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔒 Security Checklist

Before deployment:

- [ ] NASA API key stored in `application.properties` ✓
- [ ] Never hardcode secrets in code
- [ ] Use environment variables in production
- [ ] Enable HTTPS
- [ ] Configure CORS if needed
- [ ] Implement rate limiting
- [ ] Add authentication/authorization
- [ ] Enable request validation

---

## 🧪 Testing Checklist

Before committing code:

- [ ] Code compiles: `mvnw clean compile`
- [ ] No warnings: Check console output
- [ ] Application starts: `java -jar target/...jar`
- [ ] Database connects: Check logs
- [ ] API endpoints work: Test in browser/Postman
- [ ] Frontend loads: Visit http://localhost:8080
- [ ] No console errors: Check browser F12
- [ ] No database errors: Check MySQL logs

---

## 📱 URL Quick Links

| Link | URL |
|------|-----|
| **Home** | http://localhost:8080 |
| **Planets** | http://localhost:8080/planets |
| **Missions** | http://localhost:8080/missions |
| **Admin** | http://localhost:8080/admin |
| **Admin Login** | http://localhost:8080/admin/login |
| **Planet Detail** | http://localhost:8080/planet-detail?planet=earth |

---

## 🚨 Common Issues & Fixes

| Issue | Fix |
|-------|-----|
| Port 8080 in use | `Get-Process -Name java \| Stop-Process -Force` |
| DB not connecting | Check MySQL running, verify credentials |
| API returns 404 | Check endpoint spelling and HTTP method |
| 3D model not showing | Verify `.glb` file in `/static/models/` |
| CSS not loading | Clear browser cache, check static folder |
| Data not caching | Verify `@EnableCaching` annotation present |

---

## 📚 Documentation Map

```
FINAL_REPORT.md
    ↓
TASK_COMPLETION_SUMMARY.md
    ↓
README_COMPLETE.md (User & Admin Guide)
    ↓
EXTERNAL_API_INTEGRATION.md (API Details)
    ↓
DOCUMENTATION_MAP.md (Full Index)
    ↓
ARCHITECTURE_DIAGRAMS.md
```

---

## 🔄 Typical Development Workflow

1. **Make Code Changes**
   ```bash
   # Edit files in src/
   ```

2. **Rebuild**
   ```bash
   mvnw clean package -DskipTests
   ```

3. **Stop Old App**
   ```bash
   Get-Process -Name java | Stop-Process -Force
   ```

4. **Start New App**
   ```bash
   java -jar target/universe-odyssey-0.0.1-SNAPSHOT.jar
   ```

5. **Test**
   - Visit http://localhost:8080 in browser
   - Check console output for errors
   - Test specific endpoints

6. **Verify**
   - Check database data
   - Check API responses
   - Check frontend display

---

## 💡 Pro Tips

✅ Use VS Code extension "Spring Boot Extension Pack"  
✅ Use IntelliJ IDEA for better Spring support  
✅ Use Postman for API testing  
✅ Use MySQL Workbench for database management  
✅ Keep terminal open to watch logs while testing  
✅ Clear browser cache when CSS changes don't show  
✅ Check MySQL error log if data issues occur  
✅ Use breakpoints for debugging complex logic  

---

## 📞 Need Help?

1. Check `README_COMPLETE.md` troubleshooting section
2. Review server console output for errors
3. Check browser console (F12) for frontend errors
4. Search documentation files for keywords
5. Review code comments for implementation details

---

## 🎯 Remember

- **Never expose API keys in frontend code** ✅
- **Always test after changes** ✅
- **Keep documentation updated** ✅
- **Use version control** ✅
- **Back up database** ✅
- **Monitor error logs** ✅
- **Test on multiple browsers** ✅
- **Verify responsive design** ✅

---

**Good luck with development! 🚀**
