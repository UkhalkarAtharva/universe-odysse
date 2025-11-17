# Architecture & Flow Diagrams

## 📊 System Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                     USER BROWSER                             │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │              REACT SPA (index.html)                │    │
│  ├────────────────────────────────────────────────────┤    │
│  │                                                    │    │
│  │  Home Component          PlanetDetails Component  │    │
│  │  ┌────────────┐         ┌─────────────────────┐  │    │
│  │  │ Planet     │         │ useParams()         │  │    │
│  │  │ Cards      │ click   │ useEffect()         │  │    │
│  │  │ (Clickable)├────────→│ useState()          │  │    │
│  │  │            │         │ Fetch from API      │  │    │
│  │  └────────────┘         │ Render Data         │  │    │
│  │                         └─────────────────────┘  │    │
│  │        React Router                              │    │
│  │        /planet/:name                             │    │
│  │                                                  │    │
│  └────────────────────────────────────────────────────┘    │
│                         ↓                                    │
│        planet-card-handler.js (click interceptor)           │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                            │
                            │ HTTP GET
                            ↓
┌──────────────────────────────────────────────────────────────┐
│                  SPRING BOOT SERVER                          │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  HTTP Request: GET /planet/earth                            │
│            ↓                                                 │
│  @GetMapping("/planet/**")                                  │
│            ↓                                                 │
│  forward:/index.html                                        │
│            ↓                                                 │
│  Serve React SPA (index.html)                               │
│                                                              │
│  (React handles routing on client side)                     │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                            │
                            │ JavaScript: fetch()
                            ↓
┌──────────────────────────────────────────────────────────────┐
│              SOLAR SYSTEM OPENDATA API                       │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  https://api.le-systeme-solaire.net/rest/bodies/earth      │
│                                                              │
│  Response: {                                                 │
│    englishName: "Earth",                                    │
│    gravity: 9.8,                                            │
│    meanRadius: 6371,                                        │
│    mass: { massValue: 5.972, massExponent: 24 },           │
│    moons: ["The Moon"],                                     │
│    ... (10+ more fields)                                    │
│  }                                                           │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

## 🔄 Data Flow Sequence Diagram

```
User          Browser        React          API
  │             │            │              │
  │ Click       │            │              │
  │─────────────→ Card       │              │
  │             │            │              │
  │             │ Navigate   │              │
  │             │ /planet/   │              │
  │             │ earth      │              │
  │             │            │              │
  │             │────────────→ Router       │
  │             │            │ matches     │
  │             │            │ route       │
  │             │            │             │
  │             │ Render     │             │
  │             │ Component  │             │
  │             │←─────────────             │
  │             │            │             │
  │             │ Show       │             │
  │             │ Loading    │             │
  │             │────────────→ useEffect() │
  │             │            │ calls fetch │
  │             │            │─────────────→ GET /rest/bodies/earth
  │             │            │             │
  │             │            │← ─ ─ ─ ─ ─ Response JSON
  │             │            │             │
  │             │ Update     │             │
  │             │ State with │             │
  │             │ data       │             │
  │             │←─────────────             │
  │             │            │             │
  │ Display     │            │             │
  │←───────────── Planet      │             │
  │             │ Details    │             │
  │             │            │             │
```

---

## 🗂️ File Organization

```
Project Root/
│
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ com/universeodyssey/.../
│  │  │     ├─ controller/
│  │  │     │  └─ HomeController.java
│  │  │     │     ├─ @GetMapping("/")
│  │  │     │     └─ @GetMapping("/planet/**")  ← ADD THIS
│  │  │     ├─ model/
│  │  │     ├─ repository/
│  │  │     └─ service/
│  │  │
│  │  └─ resources/
│  │     └─ static/
│  │        ├─ js/
│  │        │  ├─ planet-card-handler.js  ← UPDATED
│  │        │  ├─ PlanetDetails.jsx  ← CREATE OR MOVE HERE
│  │        │  ├─ planet-3d-viewer.js
│  │        │  └─ lottie-player.js
│  │        ├─ css/
│  │        │  └─ site.css  ← ADD CSS STYLES
│  │        ├─ index.html  ← React SPA entry point
│  │        └─ assets/
│  │
│  └─ components/ (if using standard React structure)
│     └─ PlanetDetails.jsx  ← OR HERE
│
├─ App.jsx  ← ADD ROUTE HERE
│
├─ SOLAR_API_INTEGRATION.md  ← Detailed guide
├─ INTEGRATION_CHECKLIST.md  ← Step-by-step
├─ IMPLEMENTATION_SUMMARY.md  ← Overview
├─ QUICK_REFERENCE.md  ← Quick lookup
├─ ARCHITECTURE_DIAGRAMS.md  ← This file
├─ api-response-examples.js  ← API examples
├─ pom.xml
└─ mvnw / mvnw.cmd
```

---

## 🔀 Route Handling Flow

```
User Request: GET /planet/earth
     │
     ↓
Spring Boot (port 8080)
     │
     ├─ Check HomeController
     │  └─ @GetMapping("/planet/**")
     │     ├─ Matches! ✓
     │     └─ Forward to /index.html
     │
     ↓
Serve React SPA (index.html + JS bundles)
     │
     ↓
Browser loads React
     │
     ├─ Parse URL: /planet/earth
     ├─ React Router checks routes
     │  └─ Matches: /planet/:name
     │
     ↓
Load PlanetDetails Component
     │
     ├─ useParams() extracts: name = "earth"
     ├─ useEffect() runs
     │  └─ fetch("https://api.../rest/bodies/earth")
     │
     ↓
Component renders with API data
     │
     ↓
User sees: Earth details page
```

---

## 🎯 Component State Machine

```
PlanetDetails Component States:

    ┌─────────────┐
    │   INITIAL   │
    │ loading:true│
    │ data: null  │
    │ error: null │
    └──────┬──────┘
           │ useEffect fires
           ↓
    ┌─────────────────┐
    │   FETCHING      │
    │ loading: true   │
    │ API in progress │
    └──────┬──────────┘
           │
      ┌────┴────┐
      │          │
      ↓          ↓
 ┌────────┐  ┌──────────┐
 │SUCCESS │  │  ERROR   │
 ├────────┤  ├──────────┤
 │load:false │load:false│
 │data: {..}│error: msg│
 │error:null│data: null│
 └────────┘  └──────────┘
      │          │
      │          │ Show error message
      │          │ with retry button
      │          │
      └────┬─────┘
           ↓
    ┌────────────────┐
    │   RENDERED     │
    │  Display data  │
    │  to user       │
    └────────────────┘
```

---

## 📡 API Communication

```
Component                          API Server
    │                                │
    │ fetch(url)                     │
    │───────────────────────────────→│
    │                                │
    │        HTTP GET Request:       │
    │        /rest/bodies/earth      │
    │                                │
    │                    Process     │
    │                    ↓           │
    │                    Database    │
    │                    ↓           │
    │                    Cache       │
    │                    ↓           │
    │    HTTP 200 OK                 │
    │    Content-Type: application/json
    │←───────────────────────────────│
    │                                │
    │ {                              │
    │   englishName: "Earth",        │
    │   gravity: 9.8,                │
    │   meanRadius: 6371,            │
    │   mass: {...},                 │
    │   moons: [...],                │
    │   ... more data ...            │
    │ }                              │
    │                                │
    ↓                                │
Parse JSON → setState → Re-render → Display
```

---

## 🎨 UI Component Hierarchy

```
PlanetDetails (Main Component)
    │
    ├─ Loading State
    │  └─ Spinner & "Loading..." text
    │
    ├─ Error State
    │  ├─ Error title
    │  ├─ Error message
    │  ├─ Back button
    │  └─ Retry button (optional)
    │
    └─ Success State
       │
       ├─ Header Section
       │  ├─ Back button
       │  ├─ Planet title
       │  └─ Planet type (Planet/Celestial Body)
       │
       ├─ Specifications Section
       │  ├─ Gravity card
       │  ├─ Radius card
       │  ├─ Mass card
       │  ├─ Moons count card
       │  ├─ Discovery date card
       │  └─ Discovered by card
       │
       ├─ Additional Info Section (optional)
       │  ├─ Axial tilt
       │  ├─ Density
       │  └─ Temperature
       │
       └─ Moons Section (if has moons)
          ├─ Moon 1
          ├─ Moon 2
          ├─ ... up to 10
          └─ "+ X more moons" (if > 10)
```

---

## 🔌 Integration Points

```
Your Code                    What We Provide
─────────────────────────────────────────────
App.jsx                      <Route path="/planet/:name" ... />
HomeController.java          @GetMapping("/planet/**") method
site.css                     CSS styling code
React source folder          PlanetDetails.jsx component

↓↓↓ These already updated ↓↓↓

planet-card-handler.js       Navigation to /planet/{name}
index.html                   SPA entry point (unchanged)
```

---

## 🚀 Deployment Flow

```
Development
    ↓
1. Add PlanetDetails.jsx to React source
2. Add route to App.jsx
3. Add CSS to site.css
4. Add method to HomeController
    ↓
5. npm run build (compile React)
6. mvnw clean package (compile Java)
    ↓
Testing
7. mvnw spring-boot:run
8. Test /planet/* routes
9. Verify API data loads
    ↓
Production
10. Deploy .jar file
11. Server starts on port 8080
12. Users access application
    ↓
13. API data flows: Browser → Spring Boot → React → API → Display
```

---

## 📊 Data Transformation

```
Raw API Response                Component Display
─────────────────────────────────────────────────
{
  englishName: "Earth"      →  "Earth" (capitalized heading)
  gravity: 9.8              →  "9.8 m/s²" (formatted)
  meanRadius: 6371          →  "6,371 km" (comma-separated)
  mass: {                   →  "5.972 × 10²⁴ kg" (scientific)
    massValue: 5.972,
    massExponent: 24
  },
  moons: ["The Moon"]       →  "1 moons" (count) + list
  axialTilt: 23.4393        →  "23.44°" (rounded)
  density: 5.52             →  "5.52 g/cm³"
  meanTemperature: 288      →  "288 K"
}
```

---

## ✨ Key Design Decisions

```
Decision                        Reason
────────────────────────────────────────────────
Use /planet/{name}             • Shorter than /planets/details/{name}
                               • Matches React Router conventions
                               • RESTful naming
                               
Use lowercase names            • API expects lowercase
                               • Standard convention
                               • Easier to type
                               
Fetch on client-side           • Reduces server load
                               • Real-time data
                               • No backend database needed
                               
Handle errors gracefully       • User-friendly messages
                               • Don't confuse users with errors
                               • Enable retry mechanism
                               
Cache API responses            • Browser handles automatically
                               • Faster subsequent loads
                               • Reduces API calls
                               
Show loading state             • Indicates something is happening
                               • Better UX than blank page
                               • Prevents user impatience
```

---

## 🎯 Critical Paths

**Happy Path (Everything Works):**
```
User clicks → Navigate /planet/earth → React renders → API responds → User sees data ✓
```

**Error Path (API fails):**
```
User clicks → Navigate /planet/earth → API request fails → Show error → User clicks back ✓
```

**Direct URL Path (No clicking):**
```
User types /planet/earth in address bar → Spring Boot serves index.html → React Router handles it → User sees data ✓
```

---

## 📈 Performance Paths

```
First Visit to /planet/earth:
  Browser → Spring Boot (2ms) → React loads (100ms) → API fetch (300ms) → Render (50ms) = ~450ms total

Second Visit (cached):
  Browser cache returns data → Render (50ms) = ~50ms total

Switch between planets (same session):
  React unmounts old component → Mounts new → Fetch (300ms) → Render (50ms) = ~350ms
```

---

This architecture ensures:
✅ Clean separation of concerns
✅ Maintainable code structure  
✅ Scalable design for future features
✅ Good user experience
✅ Minimal server load
✅ Real-time, accurate data
