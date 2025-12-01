# Solar System OpenData API Integration Guide

This guide explains how to integrate the Solar System OpenData API with your React SPA for dynamic planet details.

## Overview

Instead of hardcoded planet data from your backend, the app now fetches **real, dynamic data** from:
```
https://api.le-systeme-solaire.net/rest/bodies/{planetName}
```

Example requests:
```
/api/earth    → https://api.le-systeme-solaire.net/rest/bodies/earth
/api/mars     → https://api.le-systeme-solaire.net/rest/bodies/mars
/api/jupiter  → https://api.le-systeme-solaire.net/rest/bodies/jupiter
```

## What Changed

### 1. **Navigation Routes**
- **Old:** `/planets/{name}` (Spring Boot Thymeleaf templates)
- **New:** `/planet/{name}` (React Router with client-side rendering)

### 2. **Planet Card Click Handler**
Updated `planet-card-handler.js` to navigate to:
```javascript
window.location.href = `/planet/${encodeURIComponent(planetName.toLowerCase())}`;
```

### 3. **React Router Setup**
Add this route to your main App.jsx or routing configuration:

```jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import PlanetDetails from './components/PlanetDetails';
import YourHomeComponent from './components/Home'; // or whatever your home is

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<YourHomeComponent />} />
        <Route path="/planet/:name" element={<PlanetDetails />} />
        {/* Other routes */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
```

## Component: PlanetDetails.jsx

Created at: `src/main/resources/static/js/PlanetDetails.jsx`

**Key Features:**
- Uses `useParams()` to read planet name from URL
- Uses `useEffect()` to fetch data on component mount
- Fetches from Solar System OpenData API
- Displays:
  - englishName
  - gravity (m/s²)
  - meanRadius (km)
  - mass (with massValue and massExponent)
  - discoveryDate
  - discoveredBy
  - Number of moons
  - Additional data: axialTilt, density, meanTemperature
  - List of moon names (up to 10)

**Error Handling:**
- Gracefully handles API failures
- Shows user-friendly error messages
- Includes loading state

## CSS Styling

Add the following to your global stylesheet (`site.css` or similar):

```css
/* Planet Details Page */
.planet-details-container {
  background: linear-gradient(135deg, #0f1724 0%, #0b1230 100%);
  color: #e6eef8;
  min-height: 100vh;
  padding: 2rem;
  font-family: "Inter", system-ui, -apple-system, "Segoe UI", Roboto;
}

.planet-details-page {
  max-width: 1200px;
  margin: 0 auto;
}

.planet-header {
  margin-bottom: 3rem;
  position: relative;
}

.back-button {
  background: none;
  border: 1px solid rgba(123, 97, 255, 0.3);
  color: #7b61ff;
  padding: 0.6rem 1.2rem;
  border-radius: 20px;
  font-size: 0.9rem;
  cursor: pointer;
  margin-bottom: 1.5rem;
  transition: all 0.3s ease;
}

.back-button:hover {
  background: rgba(123, 97, 255, 0.15);
  border-color: rgba(123, 97, 255, 0.5);
  transform: translateY(-2px);
}

.planet-title {
  font-size: 3rem;
  font-weight: 700;
  margin: 1rem 0 0.5rem 0;
  text-transform: capitalize;
  color: #fff;
}

.planet-type {
  font-size: 1.1rem;
  color: #b8c5d6;
  margin: 0;
}

/* Specifications Section */
.specs-section {
  margin: 3rem 0;
}

.specs-section h2 {
  font-size: 1.8rem;
  margin-bottom: 2rem;
  color: #fff;
}

.specs-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.spec-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 1.5rem;
  transition: all 0.3s ease;
}

.spec-card:hover {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(123, 97, 255, 0.3);
  transform: translateY(-4px);
}

.spec-label {
  font-size: 0.85rem;
  color: #b8c5d6;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  margin-bottom: 0.5rem;
}

.spec-value {
  font-size: 1.3rem;
  font-weight: 600;
  color: #7b61ff;
}

/* Additional Info Section */
.additional-info-section {
  margin: 3rem 0;
}

.additional-info-section h2 {
  font-size: 1.8rem;
  margin-bottom: 2rem;
  color: #fff;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.info-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 12px;
  padding: 1.5rem;
}

.info-card h4 {
  margin: 0 0 0.5rem 0;
  color: #fff;
  font-size: 1rem;
}

.info-card p {
  margin: 0;
  color: #b8c5d6;
  font-size: 1.1rem;
}

/* Moons Section */
.moons-section {
  margin: 3rem 0;
}

.moons-section h2 {
  font-size: 1.8rem;
  margin-bottom: 2rem;
  color: #fff;
}

.moons-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
}

.moon-item {
  background: rgba(123, 97, 255, 0.1);
  border: 1px solid rgba(123, 97, 255, 0.2);
  border-radius: 8px;
  padding: 1rem;
  text-align: center;
}

.moon-name {
  color: #e6eef8;
  font-size: 0.95rem;
  font-weight: 500;
}

.moons-info {
  grid-column: 1 / -1;
  color: #b8c5d6;
  font-style: italic;
}

/* Loading State */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  text-align: center;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(123, 97, 255, 0.2);
  border-top-color: #7b61ff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Error State */
.error-container {
  background: rgba(255, 107, 107, 0.1);
  border: 1px solid rgba(255, 107, 107, 0.3);
  border-radius: 12px;
  padding: 2rem;
  margin: 2rem 0;
  text-align: center;
}

.error-container h2 {
  color: #ff6b6b;
  margin-top: 0;
}

.error-container p {
  color: #b8c5d6;
  margin-bottom: 1.5rem;
}

/* Responsive Design */
@media (max-width: 768px) {
  .planet-title {
    font-size: 2rem;
  }

  .specs-grid {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }

  .moons-list {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }
}

@media (max-width: 480px) {
  .planet-details-container {
    padding: 1rem;
  }

  .planet-title {
    font-size: 1.5rem;
  }

  .specs-grid,
  .info-grid {
    grid-template-columns: 1fr;
  }
}
```

## API Response Example

When you navigate to `/planet/earth`, it fetches:
```json
{
  "englishName": "Earth",
  "gravity": 9.8,
  "meanRadius": 6371,
  "mass": {
    "massValue": 5.972,
    "massExponent": 24
  },
  "discoveryDate": null,
  "discoveredBy": "Unknown",
  "isPlanet": true,
  "moons": ["The Moon"],
  "axialTilt": 23.4393,
  "density": 5.52,
  "meanTemperature": 288
}
```

## Supported Planets

The Solar System OpenData API supports all 8 planets:
- earth
- mars
- mercury
- venus
- jupiter
- saturn
- uranus
- neptune

Also works with moons and other celestial bodies!

## Testing

**Test URLs:**
```
http://localhost:8080/planet/earth
http://localhost:8080/planet/mars
http://localhost:8080/planet/jupiter
http://localhost:8080/planet/saturn
http://localhost:8080/planet/uranus
http://localhost:8080/planet/neptune
http://localhost:8080/planet/mercury
http://localhost:8080/planet/venus
```

## Advantages of This Approach

✅ **Real Data:** No hardcoding - always get current, accurate planetary data
✅ **No Backend Changes:** All data comes from public API, no Spring Boot modifications needed
✅ **Client-Side Rendering:** React handles all the rendering and state management
✅ **Fast:** Data fetching happens in the browser, reduces server load
✅ **Scalable:** Can easily add more fields from the API response
✅ **Maintainable:** Single component handles all planet details
✅ **SEO-Friendly:** If you add server-side rendering later, easy to convert

## Next Steps

1. ✅ Update your React source code with the `PlanetDetails.jsx` component
2. ✅ Add the route to your router configuration
3. ✅ Add the CSS styles to your stylesheet
4. ✅ Rebuild and deploy your React app
5. ✅ Update `HomeController` to forward `/planet/*` to index.html (see below)

## HomeController Update

Update your Spring Boot `HomeController.java` to forward the new route:

```java
@GetMapping("/planet/**")
public String forwardPlanet() {
    return "forward:/index.html";
}
```

This ensures that direct visits to `/planet/earth` load the React app correctly.

## Troubleshooting

**Issue:** "Planet not found"
- **Solution:** Check that the planet name is lowercase. The API is case-sensitive.

**Issue:** CORS errors
- **Solution:** The Solar System OpenData API has CORS enabled. If you get errors, you might need to use a CORS proxy or add a backend endpoint that proxies the API calls.

**Issue:** No moons displaying
- **Solution:** Not all celestial bodies have moons data. The component handles this gracefully.

## Additional Features You Can Add

1. **Planet Images:** Fetch from NASA API
2. **Orbital Information:** Add orbital data from the API response
3. **Comparison:** Side-by-side planet comparisons
4. **3D Visualization:** Integrate Three.js (you already have the script)
5. **Share:** Add social media share buttons
6. **Favorites:** Store favorite planets in localStorage

---

**API Documentation:** https://api.le-systeme-solaire.net/en/
