import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const PlanetDetails = () => {
  const { name } = useParams();
  const navigate = useNavigate();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPlanetData = async () => {
      try {
        setLoading(true);
        // Fetch from Solar System OpenData API
        const apiUrl = `https://api.le-systeme-solaire.net/rest/bodies/${name}`;
        const response = await fetch(apiUrl);
        
        if (!response.ok) {
          throw new Error(`Failed to fetch data for planet: ${name}`);
        }
        
        const planetData = await response.json();
        setData(planetData);
        setError(null);
      } catch (err) {
        console.error('Error fetching planet data:', err);
        setError(err.message);
        setData(null);
      } finally {
        setLoading(false);
      }
    };

    if (name) {
      fetchPlanetData();
    }
  }, [name]);

  const formatNumberWithCommas = (num) => {
    if (num === null || num === undefined) return 'N/A';
    return num.toLocaleString('en-US', { maximumFractionDigits: 2 });
  };

  const formatMass = (mass) => {
    if (!mass) return 'N/A';
    const { massValue, massExponent } = mass;
    if (massValue === null || massExponent === null) return 'N/A';
    return `${massValue} × 10^${massExponent} kg`;
  };

  const renderLoading = () => (
    <div className="loading-container">
      <div className="spinner"></div>
      <p>Loading {name} details...</p>
    </div>
  );

  const renderError = () => (
    <div className="error-container">
      <h2>Error</h2>
      <p>{error}</p>
      <button onClick={() => navigate('/')}>Back to Home</button>
    </div>
  );

  const renderPlanetDetails = () => {
    if (!data) return null;

    return (
      <div className="planet-details-page container">
        {/* Header Section */}
        <div className="planet-header grid-two-column">
          <button className="back-button" onClick={() => navigate('/')}>
            ← Back to Planets
          </button>
          <h1 className="planet-title break-word">{data.englishName || name}</h1>
          <p className="planet-type">
            {data.isPlanet ? 'Planet' : 'Celestial Body'}
          </p>
        </div>

        {/* Key Specifications Section */}
        <div className="specs-section">
          <h2>Key Specifications</h2>
          <div className="specs-grid">
            {data.gravity !== null && data.gravity !== undefined && (
              <div className="spec-card">
                <div className="spec-label">Surface Gravity</div>
                <div className="spec-value break-word">{formatNumberWithCommas(data.gravity)} m/s²</div>
              </div>
            )}

            {data.meanRadius !== null && data.meanRadius !== undefined && (
              <div className="spec-card">
                <div className="spec-label">Mean Radius</div>
                <div className="spec-value break-word">{formatNumberWithCommas(data.meanRadius)} km</div>
              </div>
            )}

            {data.mass && (
              <div className="spec-card">
                <div className="spec-label">Mass</div>
                <div className="spec-value break-word">{formatMass(data.mass)}</div>
              </div>
            )}

            {data.moons && data.moons.length > 0 && (
              <div className="spec-card">
                <div className="spec-label">Number of Moons</div>
                <div className="spec-value break-word">{data.moons.length}</div>
              </div>
            )}

            {data.discoveryDate && (
              <div className="spec-card">
                <div className="spec-label">Discovery Date</div>
                <div className="spec-value break-word">{data.discoveryDate}</div>
              </div>
            )}

            {data.discoveredBy && (
              <div className="spec-card">
                <div className="spec-label">Discovered By</div>
                <div className="spec-value break-word">{data.discoveredBy}</div>
              </div>
            )}
          </div>
        </div>

        {/* Additional Information Section */}
        {(data.axialTilt || data.density || data.meanTemperature) && (
          <div className="additional-info-section">
            <h2>Additional Information</h2>
            <div className="info-grid">
              {data.axialTilt !== null && data.axialTilt !== undefined && (
                <div className="info-card">
                  <h4>Axial Tilt</h4>
                  <p className="break-word">{formatNumberWithCommas(data.axialTilt)}°</p>
                </div>
              )}

              {data.density !== null && data.density !== undefined && (
                <div className="info-card">
                  <h4>Density</h4>
                  <p className="break-word">{formatNumberWithCommas(data.density)} g/cm³</p>
                </div>
              )}

              {data.meanTemperature !== null && data.meanTemperature !== undefined && (
                <div className="info-card">
                  <h4>Mean Temperature</h4>
                  <p className="break-word">{formatNumberWithCommas(data.meanTemperature)} K</p>
                </div>
              )}
            </div>
          </div>
        )}

        {/* Moons Section */}
        {data.moons && data.moons.length > 0 && (
          <div className="moons-section">
            <h2>Moons ({data.moons.length})</h2>
            <div className="moons-list">
              {data.moons.slice(0, 10).map((moon, index) => (
                <div key={index} className="moon-item">
                  <span className="moon-name break-word">{moon}</span>
                </div>
              ))}
              {data.moons.length > 10 && (
                <p className="moons-info">+ {data.moons.length - 10} more moons</p>
              )}
            </div>
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="planet-details-container">
      {loading ? renderLoading() : error ? renderError() : renderPlanetDetails()}
    </div>
  );
};

export default PlanetDetails;
