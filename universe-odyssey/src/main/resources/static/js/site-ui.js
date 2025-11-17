// site-ui.js - helpers for animations, mission rendering, and model-viewer conditional logic
document.addEventListener('DOMContentLoaded', () => {
  initFadeUpObserver();
  initMissionLoader();
});

function initFadeUpObserver(){
  const obs = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if(entry.isIntersecting){
        entry.target.classList.add('visible');
        obs.unobserve(entry.target);
      }
    });
  },{threshold:0.12});

  document.querySelectorAll('.spec-card, .mission-card, .fade-up').forEach(el => obs.observe(el));
}

async function initMissionLoader(){
  const container = document.getElementById('missionsContainer');
  if(!container) return;

  // Attempt to load from /api/missions, fallback to static sample
  let missions = [];
  try{
    const res = await fetch('/api/missions');
    if(res.ok){ missions = await res.json(); }
  }catch(e){ console.warn('missions API not available, using fallback'); }

  if(!missions || missions.length===0){
    missions = getSampleMissions();
  }

  renderMissions(container, missions);
}

function renderMissions(container, missions){
  // Remove duplicates by mission id or title
  const seen = new Set();
  const unique = missions.filter(m => {
    const key = (m.id || m.missionName || '').toString();
    if(seen.has(key)) return false; seen.add(key); return true;
  });

  container.innerHTML = '';
  unique.forEach(m => {
    const card = document.createElement('article');
    card.className = 'mission-card fade-up';
    const title = m.missionName || m.title || 'Mission';
    const desc = m.description || m.objective || 'No description available';
    const image = m.image || '/assets/planet-placeholder.jpg';
    card.innerHTML = `
      <div class="mission-media"><img src="${image}" alt="${escapeHtml(title)}" onerror="this.src='/assets/planet-placeholder.jpg'"></div>
      <div class="mission-body">
        <div class="mission-status">${escapeHtml(m.status || 'Unknown')}</div>
        <h3 class="mission-title">${escapeHtml(title)}</h3>
        <div class="mission-desc">${escapeHtml(desc)}</div>
        <div class="mission-footer">
          <small class="muted">${formatDate(m.launchDate)}</small>
          <button class="mission-btn" onclick="onViewMission('${encodeURIComponent(m.id||m.missionName)}')">View Details</button>
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

function onViewMission(id){
  // For now navigate to a details page if exists or alert
  const decoded = decodeURIComponent(id);
  // Prefer a mission details route if server provides one
  // Navigate to canonical mission detail route /mission/{id}
  try {
    window.location.href = `/mission/${decoded}`;
  } catch (e) {
    alert('Open mission: ' + decoded);
  }
}

function formatDate(d){ if(!d) return ''; try{ const dt=new Date(d); return dt.toLocaleDateString(); }catch(e){return d}}

function escapeHtml(s){ if(!s) return ''; return String(s).replace(/[&<>"']/g, c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"})[c]); }

function getSampleMissions(){
  return [
    { id:'apollo11', title:'Apollo 11', status:'Completed', objective:'First crewed Moon landing', launchDate:'1969-07-16', image:'/assets/apollo11.jpg' },
    { id:'voyager1', title:'Voyager 1', status:'Active', objective:'Interstellar probe to study outer planets', launchDate:'1977-09-05', image:'/assets/voyager1.jpg' },
    { id:'juno', title:'Juno', status:'Active', objective:'Study Jupiter interior and magnetosphere', launchDate:'2011-08-05', image:'/assets/juno.jpg' }
  ];
}

// Optional: helper to inject model-viewer only when model exists
async function injectModelViewerIfAvailable(containerSelector, modelPath){
  const container = document.querySelector(containerSelector);
  if(!container) return;
  try{
    const res = await fetch(modelPath, {method:'HEAD'});
    if(res.ok){
      const mv = document.createElement('model-viewer');
      mv.setAttribute('src', modelPath);
      mv.setAttribute('auto-rotate','');
      mv.setAttribute('camera-controls','');
      mv.setAttribute('ar','');
      mv.setAttribute('alt','3D model');
      mv.style.width='100%'; mv.style.maxWidth='900px'; mv.style.height='500px'; mv.style.display='block'; mv.style.margin='40px auto';
      container.appendChild(mv);
    }
  }catch(e){ /* ignore, model not present */ }
}
