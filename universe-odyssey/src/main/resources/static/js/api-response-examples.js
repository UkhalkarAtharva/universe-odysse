/**
 * API Response Example: Solar System OpenData API
 * 
 * Endpoint: https://api.le-systeme-solaire.net/rest/bodies/{planetName}
 * 
 * Examples shown for all planets the component will handle
 */

// ============================================
// EARTH - Complete Data Example
// ============================================
const earthData = {
  "rel": "https://api.le-systeme-solaire.net/rest/bodies/earth",
  "id": "earth",
  "name": "Earth",
  "englishName": "Earth",
  "isPlanet": true,
  "moons": [
    {
      "rel": "https://api.le-systeme-solaire.net/rest/bodies/moon",
      "body": "The Moon",
      "rel": "https://api.le-systeme-solaire.net/rest/bodies/moon"
    }
  ],
  "semimajorAxis": 149600000,
  "perihelion": 147098290,
  "aphelion": 152098232,
  "eccentricity": 0.01671123,
  "inclination": 0,
  "meanAnomaly": 100.4656,
  "orbitalPeriod": 365.2596,
  "sideralOrbit": 365.25636,
  "avgOrbitSpeed": 29.78,
  "escapeVelocity": 11.186,
  "meanRadius": 6371,
  "equaRadius": 6378.137,
  "polarRadius": 6356.752,
  "flattening": 0.0033528,
  "volume": {
    "volValue": 108.321,
    "volExponent": 10
  },
  "massValue": 5.972,
  "massExponent": 24,
  "mass": {
    "massValue": 5.972,
    "massExponent": 24
  },
  "density": 5.52,
  "gravity": 9.8,
  "meanTemperature": 288,
  "discoveredBy": "Unknown",
  "discoveryDate": null,
  "alternativeName": "Gaia",
  "axialTilt": 23.4393,
  "mainAномaly": 100.4656,
  "image": "/img/earth.jpg"
};

// ============================================
// MARS
// ============================================
const marsData = {
  "englishName": "Mars",
  "isPlanet": true,
  "moons": [
    { "body": "Phobos" },
    { "body": "Deimos" }
  ],
  "semimajorAxis": 227939200,
  "perihelion": 206669100,
  "aphelion": 249209300,
  "eccentricity": 0.09341233,
  "meanAnomaly": 355.45332,
  "orbitalPeriod": 686.971,
  "avgOrbitSpeed": 24.07,
  "escapeVelocity": 5.027,
  "meanRadius": 3389.5,
  "flattening": 0.00589,
  "volume": {
    "volValue": 1.6318,
    "volExponent": 11
  },
  "mass": {
    "massValue": 0.64171,
    "massExponent": 23
  },
  "density": 3.9335,
  "gravity": 3.71,
  "meanTemperature": 210,
  "discoveredBy": "Unknown",
  "discoveryDate": null,
  "axialTilt": 25.1914
};

// ============================================
// JUPITER
// ============================================
const jupiterData = {
  "englishName": "Jupiter",
  "isPlanet": true,
  "moons": [
    { "body": "Io" },
    { "body": "Europa" },
    { "body": "Ganymede" },
    { "body": "Callisto" },
    // ... plus 91 more
  ],
  "semimajorAxis": 778500000,
  "perihelion": 740520000,
  "aphelion": 816620000,
  "eccentricity": 0.04849485,
  "meanAnomaly": 34.40438,
  "orbitalPeriod": 4332.89,
  "avgOrbitSpeed": 13.07,
  "escapeVelocity": 60.2,
  "meanRadius": 69911,
  "flattening": 0.06487,
  "volume": {
    "volValue": 1.43128,
    "volExponent": 15
  },
  "mass": {
    "massValue": 1.89819,
    "massExponent": 27
  },
  "density": 1.326,
  "gravity": 24.79,
  "meanTemperature": 110,
  "discoveredBy": "Unknown",
  "discoveryDate": null,
  "axialTilt": 3.13
};

// ============================================
// SATURN
// ============================================
const saturnData = {
  "englishName": "Saturn",
  "isPlanet": true,
  "moons": [
    { "body": "Titan" },
    { "body": "Rhea" },
    { "body": "Iapetus" },
    { "body": "Enceladus" },
    // ... plus 142 more
  ],
  "semimajorAxis": 1433449370,
  "perihelion": 1352555400,
  "aphelion": 1514343000,
  "eccentricity": 0.05550825,
  "meanAnomaly": 49.94432,
  "orbitalPeriod": 10759.22,
  "avgOrbitSpeed": 9.68,
  "escapeVelocity": 36.09,
  "meanRadius": 58232,
  "flattening": 0.09796,
  "volume": {
    "volValue": 8.27130,
    "volExponent": 14
  },
  "mass": {
    "massValue": 5.6839,
    "massExponent": 26
  },
  "density": 0.687,
  "gravity": 10.44,
  "meanTemperature": 140,
  "discoveredBy": "Unknown",
  "discoveryDate": null,
  "axialTilt": 26.73
};

// ============================================
// URANUS
// ============================================
const uranusData = {
  "englishName": "Uranus",
  "isPlanet": true,
  "moons": [
    { "body": "Titania" },
    { "body": "Oberon" },
    { "body": "Umbriel" },
    // ... plus 25 more
  ],
  "semimajorAxis": 2873550000,
  "perihelion": 2748000000,
  "aphelion": 3000600000,
  "eccentricity": 0.047167,
  "meanAnomaly": 313.23081,
  "orbitalPeriod": 30688.5,
  "avgOrbitSpeed": 6.81,
  "escapeVelocity": 21.29,
  "meanRadius": 25362,
  "flattening": 0.02293,
  "volume": {
    "volValue": 6.83346,
    "volExponent": 13
  },
  "mass": {
    "massValue": 8.6810,
    "massExponent": 25
  },
  "density": 1.27,
  "gravity": 8.87,
  "meanTemperature": 76,
  "discoveredBy": "William Herschel",
  "discoveryDate": "1781-03-13",
  "axialTilt": 97.77
};

// ============================================
// NEPTUNE
// ============================================
const neptuneData = {
  "englishName": "Neptune",
  "isPlanet": true,
  "moons": [
    { "body": "Triton" },
    { "body": "Proteus" },
    // ... plus 14 more
  ],
  "semimajorAxis": 4495060000,
  "perihelion": 4459753000,
  "aphelion": 4537377000,
  "eccentricity": 0.00859048,
  "meanAnomaly": 304.88003,
  "orbitalPeriod": 60182,
  "avgOrbitSpeed": 5.43,
  "escapeVelocity": 23.5,
  "meanRadius": 24622,
  "flattening": 0.0171,
  "volume": {
    "volValue": 6.25328,
    "volExponent": 13
  },
  "mass": {
    "massValue": 1.02413,
    "massExponent": 26
  },
  "density": 1.638,
  "gravity": 11.15,
  "meanTemperature": 72,
  "discoveredBy": "Urbain Le Verrier",
  "discoveryDate": "1846-09-23",
  "axialTilt": 28.32
};

// ============================================
// MERCURY
// ============================================
const mercuryData = {
  "englishName": "Mercury",
  "isPlanet": true,
  "moons": [],
  "semimajorAxis": 57909100,
  "perihelion": 46001200,
  "aphelion": 69818100,
  "eccentricity": 0.2056,
  "meanAnomaly": 174.7948,
  "orbitalPeriod": 87.969,
  "avgOrbitSpeed": 47.87,
  "escapeVelocity": 4.3,
  "meanRadius": 2439.7,
  "flattening": 0,
  "volume": {
    "volValue": 6.083,
    "volExponent": 10
  },
  "mass": {
    "massValue": 3.285,
    "massExponent": 23
  },
  "density": 5.427,
  "gravity": 3.7,
  "meanTemperature": 440,
  "discoveredBy": "Unknown",
  "discoveryDate": null,
  "axialTilt": 0.04
};

// ============================================
// VENUS
// ============================================
const venusData = {
  "englishName": "Venus",
  "isPlanet": true,
  "moons": [],
  "semimajorAxis": 108208000,
  "perihelion": 107476000,
  "aphelion": 108949000,
  "eccentricity": 0.00677323,
  "meanAnomaly": 50.115,
  "orbitalPeriod": 224.701,
  "avgOrbitSpeed": 35.02,
  "escapeVelocity": 10.36,
  "meanRadius": 6051.8,
  "flattening": 0,
  "volume": {
    "volValue": 9.2843,
    "volExponent": 11
  },
  "mass": {
    "massValue": 4.8675,
    "massExponent": 24
  },
  "density": 5.204,
  "gravity": 8.87,
  "meanTemperature": 737,
  "discoveredBy": "Unknown",
  "discoveryDate": null,
  "axialTilt": 177.36
};

// ============================================
// MAPPING LOGIC FOR THE COMPONENT
// ============================================
/**
 * The PlanetDetails component maps these fields:
 * 
 * Display Field                 API Field
 * ─────────────────────────────────────────────
 * Planet Name                   englishName
 * Surface Gravity (m/s²)        gravity
 * Mean Radius (km)              meanRadius
 * Mass (scientific)             mass.massValue + mass.massExponent
 * Discovery Date                discoveryDate
 * Discovered By                 discoveredBy
 * Number of Moons               moons.length
 * Axial Tilt (°)                axialTilt
 * Density (g/cm³)               density
 * Mean Temperature (K)          meanTemperature
 * Moon Names                    moons[].body
 * Type Label                    isPlanet (true/false)
 */

// ============================================
// UNIT CONVERSIONS (FOR REFERENCE)
// ============================================
/**
 * If you want to convert units in the component:
 * 
 * Temperature: Kelvin to Celsius = K - 273.15
 * Mass Exponent: × 10^24 kg = × 10^(24-24) yotta-kilograms
 * 
 * Example for mass formatting:
 * Earth: 5.972 × 10^24 kg
 * Display: "5.972 × 10²⁴ kg"
 */

// ============================================
// API RESPONSE STRUCTURE NOTES
// ============================================
/**
 * Not all fields are guaranteed for all bodies:
 * - discoveryDate might be null (planets were not "discovered")
 * - moons array might be empty
 * - Some numeric fields might be null
 * 
 * The component handles null values gracefully
 * by checking !== null && !== undefined
 */

export {
  earthData,
  marsData,
  jupiterData,
  saturnData,
  uranusData,
  neptuneData,
  mercuryData,
  venusData
};
