import React, { useState, useEffect, useCallback } from 'react';
import '../css/missions.css';

const Missions = () => {
  const [missions, setMissions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [debounceTimer, setDebounceTimer] = useState(null);

  const NASA_API_KEY = '5wrBMBypIPJxv6YsCbLEFwSeBcaSbYVB5LbcdeYh';
  const NASA_SEARCH_URL = 'https://images-api.nasa.gov/search';

  /**
   * Fetch missions from NASA API
   */
  const fetchMissions = useCallback(async (query = 'NASA missions') => {
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams({
        q: query,
        media_type: 'image',
        page_size: 20,
      });

      const response = await fetch(`${NASA_SEARCH_URL}?${params.toString()}`);

      if (!response.ok) {
        throw new Error(`NASA API error: ${response.status}`);
      }

      const data = await response.json();
      const items = data?.collection?.items || [];

      if (items.length === 0) {
        setMissions([]);
        setError(null);
      } else {
        setMissions(items);
      }
    } catch (err) {
      console.error('Error fetching missions:', err);
      setError('Failed to load missions. Please try again.');
      setMissions([]);
    } finally {
      setLoading(false);
    }
  }, []);

  /**
   * Handle search input with debounce
   */
  const handleSearchChange = (e) => {
    const value = e.target.value.trim();
    setSearchTerm(value);

    // Clear previous timer
    if (debounceTimer) {
      clearTimeout(debounceTimer);
    }

    // Set new timer for debounced search (500ms delay)
    const timer = setTimeout(() => {
      const query = value || 'NASA missions';
      fetchMissions(query);
    }, 500);

    setDebounceTimer(timer);
  };

  /**
   * Open NASA mission details in new tab
   */
  const openMissionDetails = (nasaId) => {
    if (nasaId) {
      window.open(`https://images.nasa.gov/details-${nasaId}`, '_blank');
    }
  };

  /**
   * Extract relevant info from mission item
   */
  const getMissionInfo = (item) => {
    const data = item.data[0] || {};
    const links = item.links || [];

    const title = data.title || 'Untitled Mission';
    const description = data.description || 'No description available';
    const dateCreated = data.date_created || 'Unknown date';
    const nasaId = data.nasa_id || '';
    const imageUrl =
      links.length > 0 && links[0].href
        ? links[0].href
        : 'https://via.placeholder.com/300x200?text=No+Image';

    // Format date (YYYY-MM-DD HH:MM:SS → MMM DD, YYYY)
    const formattedDate = dateCreated
      ? new Date(dateCreated).toLocaleDateString('en-US', {
          month: 'short',
          day: 'numeric',
          year: 'numeric',
        })
      : 'Unknown date';

    // Truncate description for card preview (max 120 chars)
    const truncatedDesc =
      description.length > 120
        ? description.substring(0, 120) + '...'
        : description;

    return {
      title,
      description: truncatedDesc,
      fullDescription: description,
      dateCreated: formattedDate,
      nasaId,
      imageUrl,
    };
  };

  /**
   * Load initial missions on component mount
   */
  useEffect(() => {
    fetchMissions('NASA missions');
  }, [fetchMissions]);

  /**
   * Cleanup timer on unmount
   */
  useEffect(() => {
    return () => {
      if (debounceTimer) {
        clearTimeout(debounceTimer);
      }
    };
  }, [debounceTimer]);

  return (
    <div className="missions-container">
      {/* Header Section */}
      <header className="missions-header">
        <h1 className="missions-title">NASA Missions & Discoveries</h1>
        <p className="missions-subtitle">
          Explore the latest images, missions, and discoveries from NASA
        </p>
      </header>

      {/* Search Bar */}
      <div className="missions-search-wrapper">
        <div className="missions-search">
          <input
            type="text"
            className="missions-search-input"
            placeholder="Search missions, discoveries, astronauts..."
            value={searchTerm}
            onChange={handleSearchChange}
            aria-label="Search NASA missions"
          />
          <svg
            className="missions-search-icon"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.35-4.35"></path>
          </svg>
        </div>
      </div>

      {/* Loading Spinner */}
      {loading && (
        <div className="missions-loading">
          <div className="spinner"></div>
          <p>Loading missions...</p>
        </div>
      )}

      {/* Error Message */}
      {error && !loading && (
        <div className="missions-error">
          <p>{error}</p>
          <button
            className="missions-retry-btn"
            onClick={() => fetchMissions(searchTerm || 'NASA missions')}
          >
            Retry
          </button>
        </div>
      )}

      {/* No Results */}
      {!loading && !error && missions.length === 0 && (
        <div className="missions-no-results">
          <p>
            {searchTerm
              ? `No missions found for "${searchTerm}"`
              : 'No missions available'}
          </p>
          <button
            className="missions-retry-btn"
            onClick={() => {
              setSearchTerm('');
              fetchMissions('NASA missions');
            }}
          >
            Clear Search
          </button>
        </div>
      )}

      {/* Missions Grid */}
      {!loading && !error && missions.length > 0 && (
        <div className="missions-grid">
          {missions.map((mission, index) => {
            const info = getMissionInfo(mission);
            return (
              <div
                key={`${info.nasaId || index}`}
                className="mission-card"
                onClick={() => openMissionDetails(info.nasaId)}
                role="button"
                tabIndex={0}
                onKeyDown={(e) => {
                  if (e.key === 'Enter' || e.key === ' ') {
                    openMissionDetails(info.nasaId);
                  }
                }}
              >
                {/* Image Container */}
                <div className="mission-card-image">
                  <img
                    src={info.imageUrl}
                    alt={info.title}
                    loading="lazy"
                    onError={(e) => {
                      e.target.src =
                        'https://via.placeholder.com/300x200?text=Image+Unavailable';
                    }}
                  />
                  <div className="mission-card-overlay">
                    <span className="mission-card-cta">View Details →</span>
                  </div>
                </div>

                {/* Content Container */}
                <div className="mission-card-content">
                  <h3 className="mission-card-title">{info.title}</h3>
                  <p className="mission-card-date">{info.dateCreated}</p>
                  <p className="mission-card-description">
                    {info.description}
                  </p>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default Missions;
