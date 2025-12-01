// site-ui.js - helpers for animations, mission rendering, and model-viewer conditional logic
document.addEventListener('DOMContentLoaded', () => {
  initFadeUpObserver();
  initNasaMissions();
});

function initFadeUpObserver() {
  const obs = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
        obs.unobserve(entry.target);
      }
    });
  }, { threshold: 0.12 });

  document.querySelectorAll('.spec-card, .mission-card, .fade-up').forEach(el => obs.observe(el));
}

// --- NASA Missions Logic ---

const NASA_SEARCH_URL = 'https://images-api.nasa.gov/search';

const MISSION_LIST = {
  active: [
    { id: 'artemis', query: 'Artemis program', title: 'Artemis Program' },
    { id: 'jwst', query: 'James Webb Space Telescope', title: 'James Webb Telescope' },
    { id: 'perseverance', query: 'Perseverance rover', title: 'Perseverance Rover' },
    { id: 'juno', query: 'Juno spacecraft', title: 'Juno Mission' },
    { id: 'iss', query: 'International Space Station', title: 'International Space Station' }
  ],
  historic: [
    { id: 'apollo11', query: 'Apollo 11', title: 'Apollo 11' },
    { id: 'cassini', query: 'Cassini Huygens', title: 'Cassini-Huygens' },
    { id: 'shuttle', query: 'Space Shuttle Atlantis', title: 'Space Shuttle Program' },
    { id: 'kepler', query: 'Kepler Space Telescope', title: 'Kepler Mission' },
    { id: 'voyager', query: 'Voyager spacecraft', title: 'Voyager Program' }
  ]
};

async function fetchMissionData(mission) {
  try {
    const params = new URLSearchParams({
      q: mission.query,
      media_type: 'image',
      page_size: 1
    });

    const response = await fetch(`${NASA_SEARCH_URL}?${params.toString()}`);
    if (!response.ok) throw new Error('API Error');

    const data = await response.json();
    const item = data.collection?.items?.[0];

    if (!item) return null;

    return {
      id: mission.id,
      title: mission.title,
      description: item.data[0].description,
      imageUrl: item.links[0].href,
      date: item.data[0].date_created,
      nasaId: item.data[0].nasa_id
    };
  } catch (err) {
    console.error(`Error fetching ${mission.title}:`, err);
    return null;
  }
}

async function initNasaMissions() {
  const container = document.getElementById('missionsContainer');
  if (!container) return;

  container.innerHTML = '<div class="missions-loading"><div class="spinner"></div><p>Accessing NASA Archives...</p></div>';

  try {
    const [activeMissions, historicMissions] = await Promise.all([
      Promise.all(MISSION_LIST.active.map(fetchMissionData)),
      Promise.all(MISSION_LIST.historic.map(fetchMissionData))
    ]);

    const validActive = activeMissions.filter(m => m !== null);
    const validHistoric = historicMissions.filter(m => m !== null);

    renderNasaMissions(container, validActive, validHistoric);
  } catch (err) {
    console.error('Error loading missions:', err);
    container.innerHTML = '<div class="missions-error"><p>Failed to load mission archives.</p></div>';
  }
}

function renderNasaMissions(container, active, historic) {
  container.innerHTML = '';

  // Helper to create section with list view
  const createSection = (title, icon, missions, statusLabel) => {
    if (missions.length === 0) return '';

    const section = document.createElement('section');
    section.className = 'missions-section';

    const header = document.createElement('h2');
    header.className = 'section-title';
    header.innerHTML = `<span class="icon">${icon}</span> ${title}`;
    section.appendChild(header);

    const listContainer = document.createElement('div');
    listContainer.className = 'missions-list-container';

    // List Header
    const listHeader = document.createElement('div');
    listHeader.className = 'missions-list-header';
    listHeader.innerHTML = `
      <div class="col-name">Mission Name</div>
      <div class="col-status">Status</div>
      <div class="col-date">Start Date</div>
    `;
    listContainer.appendChild(listHeader);

    // List Items
    const listBody = document.createElement('div');
    listBody.className = 'missions-list-body';

    missions.forEach(mission => {
      const row = document.createElement('div');
      row.className = 'mission-row fade-up';
      row.onclick = () => openMissionModal(mission);

      row.innerHTML = `
        <div class="col-name">${escapeHtml(mission.title)}</div>
        <div class="col-status">
          <span class="status-badge ${statusLabel.toLowerCase()}">${statusLabel}</span>
        </div>
        <div class="col-date">${new Date(mission.date).getFullYear()}</div>
      `;
      listBody.appendChild(row);
    });

    listContainer.appendChild(listBody);
    section.appendChild(listContainer);
    return section;
  };

  const activeSection = createSection('Active Missions', '🚀', active, 'Active');
  if (activeSection) container.appendChild(activeSection);

  const historicSection = createSection('Historic Archives', '🏛️', historic, 'Completed');
  if (historicSection) container.appendChild(historicSection);

  // Re-init observer for new elements
  initFadeUpObserver();
}
function openMissionModal(mission) {
  // Remove existing modal if any
  const existing = document.querySelector('.modal-backdrop');
  if (existing) existing.remove();

  const backdrop = document.createElement('div');
  backdrop.className = 'modal-backdrop';
  backdrop.onclick = (e) => { if (e.target === backdrop) closeMissionModal(); };

  backdrop.innerHTML = `
    <div class="modal-content">
      <button class="modal-close" onclick="closeMissionModal()">&times;</button>
      <div class="modal-image-wrapper">
        <img src="${mission.imageUrl}" alt="${escapeHtml(mission.title)}">
      </div>
      <div class="modal-body">
        <h2 class="modal-title">${escapeHtml(mission.title)}</h2>
        <p class="modal-date">Established: ${new Date(mission.date).toLocaleDateString()}</p>
        <div class="modal-description">
          <p>${escapeHtml(mission.description)}</p>
        </div>
        <div class="modal-footer">
          <span class="nasa-id">NASA ID: ${escapeHtml(mission.nasaId)}</span>
        </div>
      </div>
    </div>
  `;

  document.body.appendChild(backdrop);
  document.body.style.overflow = 'hidden'; // Prevent background scrolling

  // Close on Escape
  document.addEventListener('keydown', handleEscKey);
}

function closeMissionModal() {
  const backdrop = document.querySelector('.modal-backdrop');
  if (backdrop) {
    backdrop.remove();
    document.body.style.overflow = '';
    document.removeEventListener('keydown', handleEscKey);
  }
}

function handleEscKey(e) {
  if (e.key === 'Escape') closeMissionModal();
}

// --- Utilities ---

function escapeHtml(s) { if (!s) return ''; return String(s).replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": "&#39;" })[c]); }

// Optional: helper to inject model-viewer only when model exists
async function injectModelViewerIfAvailable(containerSelector, modelPath) {
  const container = document.querySelector(containerSelector);
  if (!container) return;
  try {
    const res = await fetch(modelPath, { method: 'HEAD' });
    if (res.ok) {
      const mv = document.createElement('model-viewer');
      mv.setAttribute('src', modelPath);
      mv.setAttribute('auto-rotate', '');
      mv.setAttribute('camera-controls', '');
      mv.setAttribute('ar', '');
      mv.setAttribute('alt', '3D model');
      mv.style.width = '100%'; mv.style.maxWidth = '900px'; mv.style.height = '500px'; mv.style.display = 'block'; mv.style.margin = '40px auto';
      container.appendChild(mv);
    }
  } catch (e) { /* ignore, model not present */ }
}
