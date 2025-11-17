# 🌌 Universe Odyssey - Complete Project Documentation

**Version**: 1.0  
**Status**: ✅ **PRODUCTION READY**  
**Last Updated**: November 16, 2025

---

## 📖 Table of Contents

1. [Project Overview](#project-overview)
2. [Getting Started](#getting-started)
3. [Architecture](#architecture)
4. [Features](#features)
5. [API Reference](#api-reference)
6. [User Guide](#user-guide)
7. [Admin Guide](#admin-guide)
8. [Troubleshooting](#troubleshooting)

---

## 🌟 Project Overview

**Universe Odyssey** is a comprehensive web application dedicated to space exploration, featuring:

- 🪐 **Interactive Planet Explorer** - Browse and learn about planets with real data from NASA and OpenSolarSystem APIs
- 🎮 **3D Planet Visualization** - View 3D models of celestial bodies (model-viewer library)
- 🚀 **Space Missions Database** - Explore historic and current space missions
- ⚡ **Admin Portal** - Comprehensive management system for content
- 📱 **Responsive Design** - Works seamlessly on desktop, tablet, and mobile

### Tech Stack

**Backend**:
- Spring Boot 3.4.11
- MySQL 8.0
- JPA/Hibernate
- Spring Security
- Spring Cache

**Frontend**:
- HTML5, CSS3, Vanilla JavaScript
- Google Model-Viewer (3D visualization)
- AOS (Animate on Scroll)
- Responsive Grid Layout

**External APIs**:
- OpenSolarSystem API (free, no key required)
- NASA Images API (server-side key required)

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MySQL 8.0+
- Port 8080 available

### Installation

1. **Clone or extract the project**:
   ```bash
   cd universe-odyssey
   ```

2. **Configure MySQL Database**:
   ```sql
   CREATE DATABASE universe_odyssey;
   -- Application will auto-create tables
   ```

3. **Update database credentials** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/universe_odyssey
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

4. **Build the project**:
   ```bash
   ./mvnw clean package
   ```

5. **Run the application**:
   ```bash
   java -jar target/universe-odyssey-0.0.1-SNAPSHOT.jar
   ```

6. **Access the application**:
   - User Site: http://localhost:8080
   - Admin Portal: http://localhost:8080/admin
   - Default Admin: username=`admin`, password=`admin123`

---

## 🏗️ Architecture

### System Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    Client (Browser)                     │
│  (HTML/CSS/JS + model-viewer + AOS animations)         │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│              Spring Boot Application                    │
├─────────────────────────────────────────────────────────┤
│ Controllers:                                            │
│  - HomeController (page routing)                        │
│  - PlanetRestController (planet API)                    │
│  - SpaceMissionRestController (mission API)             │
│  - AdminPageController & AdminApiController             │
├─────────────────────────────────────────────────────────┤
│ Services:                                               │
│  - ExternalPlanetDataService (multi-source fetching)    │
│  - PlanetDetailService                                  │
│  - SpaceMissionService                                  │
│  - (Other business logic services)                      │
├─────────────────────────────────────────────────────────┤
│ Data Layer:                                             │
│  - Spring Data JPA Repositories                         │
│  - Entity Models (Planet, Mission, User, etc.)          │
│  - Spring Cache (in-memory caching)                     │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│              MySQL Database                             │
│  Tables: planet_detail, space_mission, admin_users, etc │
└─────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────┐
│            External APIs (Server-side only)             │
│  - OpenSolarSystem API (for planet data)                │
│  - NASA Images API (for imagery)                        │
└─────────────────────────────────────────────────────────┘
```

### Data Flow: Planet Detail Page

```
User visits /planet-detail?planet=Mars
    ↓
planet-detail.html loads
    ↓
JavaScript calls /api/planets/data/mars
    ↓
ExternalPlanetDataService executes:
    1. Check database for "Mars"
       ├─ Found? Return from DB ✓
       └─ Not found? Continue...
    2. Call OpenSolarSystem API
       ├─ Success? Get basic data, continue to NASA
       ├─ Fail? Try NASA directly
    3. Call NASA Images API
       ├─ Success? Get image data, save to DB ✓
       ├─ Fail? Return partial data ✓
    ↓
Return PlanetDetailResponse as JSON
    ↓
JavaScript renders planet specs, image, and 3D model
```

---

## ✨ Features

### 🌐 Public Features (User Site)

| Feature | Description | Location |
|---------|-------------|----------|
| **Home Page** | Welcome screen with featured planets | `/` |
| **Planet Explorer** | Browse all planets with grid layout | `/planets` |
| **Planet Details** | Full details with 3D model and specs | `/planet-detail?planet={name}` |
| **Missions** | View space missions database | `/missions` |
| **Login** | User authentication | `/login.html` |
| **Signup** | Create new account | `/signup.html` |

### 🔐 Admin Features

| Feature | Description | Location |
|---------|-------------|----------|
| **Dashboard** | Overview with stats | `/admin/dashboard` |
| **Planet Management** | CRUD for planets | `/admin/planets` |
| **Mission Management** | CRUD for missions | `/admin/missions` |
| **Facts Management** | CRUD for space facts | `/admin/facts` |
| **User Management** | CRUD for users | `/admin/users` |

### 🎨 UI/UX Features

- ✅ Glassmorphism design with neon accents
- ✅ Smooth fade-up animations (AOS library)
- ✅ Loading skeletons for better UX
- ✅ Responsive grid layouts
- ✅ Dark theme optimized for space theme
- ✅ Touch-friendly mobile interface
- ✅ Modal dialogs for editing
- ✅ Toast notifications for feedback

---

## 📡 API Reference

### Planet API

#### Get Planet Data (Multi-source)
```
GET /api/planets/data/{planetName}

Response: 200 OK
{
  "name": "Earth",
  "mass": 5.972e24,
  "gravity": 9.807,
  "radius": 6371,
  "orbitalPeriod": 365.25,
  "distanceFromSun": 149.6,
  "temperature": 288,
  "atmosphere": "N2, O2, Ar, CO2",
  "overview": "The blue planet",
  "shortDescription": "A celestial body",
  "longDescription": "Long description...",
  "nasaImageUrl": "https://...",
  "modelUrl": "/models/earth.glb",
  "moons": 1,
  "found": true
}
```

#### Get All Planets
```
GET /api/planets

Response: 200 OK
[{ planet objects from database }]
```

### Mission API

#### Get All Missions
```
GET /api/missions

Response: 200 OK
[
  {
    "id": 1,
    "missionName": "Apollo 11",
    "description": "First crewed Moon landing",
    "launchDate": "1969-07-16",
    "status": "Completed"
  },
  ...
]
```

#### Get Mission by ID
```
GET /api/missions/{id}

Response: 200 OK
{
  "id": 1,
  "missionName": "Apollo 11",
  "description": "...",
  "launchDate": "1969-07-16",
  "status": "Completed"
}
```

---

## 👤 User Guide

### How to Explore Planets

1. Visit http://localhost:8080
2. Click "Explore" or navigate to `/planets`
3. Browse available planets in grid
4. Click on a planet card
5. View detailed information:
   - Physical specifications (mass, radius, gravity, etc.)
   - Orbital information
   - Atmospheric composition
   - 3D model (if available)
   - NASA imagery
6. Click "Want to know more?" to visit NASA's website

### How to View Missions

1. Navigate to `/missions`
2. Browse mission cards
3. See mission status (Active, Completed, Planned)
4. View launch dates and objectives
5. Click "View Details" for more information

### How to Create Account

1. Click "Sign Up" on any page
2. Enter username, email, and password
3. Click "Create Account"
4. Login with your credentials
5. Access personalized features (bookmarks, favorites, etc.)

---

## 🔑 Admin Guide

### Access Admin Panel

1. Navigate to http://localhost:8080/admin/login
2. Login with admin credentials (default: admin/admin123)
3. You'll be redirected to admin dashboard

### Manage Planets

1. Go to **Planets** section
2. View all planets in the list
3. Click **Edit** to modify planet details
4. Click **Delete** to remove a planet
5. Click **+ New Planet** to add a new planet

### Manage Missions

1. Go to **Missions** section
2. Edit existing missions or create new ones
3. Update mission status (Active, Completed, Planned)
4. Add launch dates and descriptions

### Manage Facts

1. Go to **Facts** section
2. Add interesting space facts
3. Organize by category
4. Mark as featured or archived

### Manage Users

1. Go to **Users** section
2. View all user accounts
3. Edit user roles and status
4. Deactivate or delete users as needed

---

## 🐛 Troubleshooting

### Application Won't Start

**Issue**: "Port 8080 is already in use"
```bash
# Kill process using port 8080
Get-Process -Name "java" | Stop-Process -Force
```

**Issue**: "Cannot connect to MySQL"
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database exists: `CREATE DATABASE universe_odyssey;`

### Planet Data Not Loading

**Issue**: API returns empty response
- Check network tab in browser dev tools
- Verify `/api/planets/data/{name}` endpoint is reachable
- Check server logs for errors
- Ensure database has planet records

**Issue**: 3D model not displaying
- Verify `earth.glb` file exists in `/static/models/`
- Check browser console for model-viewer errors
- Ensure browser supports WebGL

### Database Issues

**Issue**: Tables not created
- Verify `spring.jpa.hibernate.ddl-auto=update` in properties
- Check MySQL user has CREATE TABLE privileges
- Restart application to trigger table creation

**Issue**: Data not persisting
- Check MySQL connection string
- Verify database exists and is accessible
- Check MySQL error logs

### Admin Panel Issues

**Issue**: Can't login to admin
- Default credentials: username=`admin`, password=`admin123`
- Password was reset on first startup
- Check server logs for authentication errors

**Issue**: Changes not saving
- Verify you're logged in as admin
- Check browser console for JavaScript errors
- Ensure CSRF token is present in forms

---

## 📞 Support & Documentation

| Document | Purpose |
|----------|---------|
| `PROJECT_COMPLETE.md` | Admin portal redesign details |
| `EXTERNAL_API_INTEGRATION.md` | API integration documentation |
| `ARCHITECTURE_DIAGRAMS.md` | System architecture |
| `QUICK_REFERENCE.md` | Quick command reference |

---

## 🎓 Development Notes

### Adding a New Planet

1. **Via Admin Panel**:
   - Login to `/admin`
   - Go to Planets → New Planet
   - Fill in details and save

2. **Via Database**:
   ```sql
   INSERT INTO planet_detail (name, mass, radius, gravity, orbital_period, distance_from_sun, surface_temp, moons, short_description, long_description, atmosphere)
   VALUES ('Venus', 4.867e24, 6052, 8.87, 224.7, 108.2, 735, 0, 'The hot planet', '...', 'CO2, N2');
   ```

### Adding a New Mission

1. **Via Admin Panel**:
   - Login to `/admin`
   - Go to Missions → New Mission
   - Fill in details and save

2. **Via API**:
   ```bash
   curl -X POST http://localhost:8080/api/missions \
     -H "Content-Type: application/json" \
     -d '{
       "missionName": "Mars Rover",
       "description": "...",
       "launchDate": "2025-01-01",
       "status": "Planned"
     }'
   ```

### Adding 3D Models

1. Place `.glb` files in `/src/main/resources/static/models/`
2. Name them `{planetname}.glb` (lowercase)
3. Restart application or reload page
4. Model will auto-display on planet detail page

---

## ✅ Quality Assurance

- ✅ All endpoints tested and working
- ✅ Database operations verified
- ✅ Security implemented (API key server-side)
- ✅ Error handling in place
- ✅ Responsive design verified
- ✅ Browser compatibility tested
- ✅ Performance optimized with caching
- ✅ Production-ready deployment

---

## 📄 License & Credits

This project is built with:
- Spring Boot (Spring Framework)
- Model-Viewer (Google)
- AOS (Animate On Scroll)
- OpenSolarSystem API
- NASA API

---

**Ready to explore the universe? 🚀**

Visit http://localhost:8080 and start your odyssey today!
