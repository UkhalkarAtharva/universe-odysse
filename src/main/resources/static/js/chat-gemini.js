// chat-gemini.js
// Frontend → calls server-side Gemini proxy endpoint.

// User-requested title/subtitle initialization (run immediately)
const planet = new URLSearchParams(location.search).get('planet') || 'Unknown';
const titleEl = document.getElementById("pageTitle");
const subtitleEl = document.getElementById("pageSubtitle");
if (titleEl) titleEl.innerText = `Cosmos AI — Want to know more about ${planet}?`;
if (subtitleEl) subtitleEl.innerText = `Ask anything about ${planet}!`;

// Grab DOM elements for chat UI
const pageTitle = titleEl;
const pageSubtitle = subtitleEl;
const messagesEl = document.getElementById('messages');
const chatForm = document.getElementById('chatForm');
const chatInput = document.getElementById('chatInput');
const sendBtn = document.getElementById('sendBtn');
const modelArea = document.getElementById('modelArea');

// Update placeholder text based on planet param
if (chatInput) {
  chatInput.placeholder = planet && planet !== 'Unknown' ? `Ask Cosmos anything about ${planet}...` : `Ask Cosmos anything...`;
}

const API_URL = "/api/gemini/chat";

function scrollToBottom() {
  messagesEl.scrollTop = messagesEl.scrollHeight;
}

function renderMessage(type, text) {
  const wrapper = document.createElement('div');
  wrapper.className = `msg ${type}`;

  const bubble = document.createElement('div');
  bubble.className = 'bubble';
  bubble.innerText = text;

  wrapper.appendChild(bubble);
  messagesEl.appendChild(wrapper);
  scrollToBottom();
}

function showTyping() {
  const t = document.createElement('div');
  t.className = 'msg ai typing';
  t.id = 'typingIndicator';
  t.innerHTML = `<div class="bubble">
      <span class="dot"></span><span class="dot"></span><span class="dot"></span>
  </div>`;
  messagesEl.appendChild(t);
  scrollToBottom();
}

function removeTyping() {
  const t = document.getElementById('typingIndicator');
  if (t) t.remove();
}

async function sendToGemini(question) {
  try {
    const payload = { question, planet };

    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      removeTyping();
      return renderMessage("ai", "Cosmos encountered an error. Try again.");
    }

    const data = await response.json();
    removeTyping();

    let answer = null;
    if (data?.candidates?.length > 0) {
      const parts = data.candidates[0].content?.parts;
      if (parts && parts.length > 0) {
        answer = parts.map(p => p.text).join("\n\n");
      }
    }

    renderMessage("ai", answer || "Cosmos could not answer right now.");
  } catch (err) {
    console.error("Gemini error:", err);
    removeTyping();
    renderMessage("ai", "Cosmos encountered an error. Try again.");
  }
}

chatForm.addEventListener('submit', async (e) => {
  e.preventDefault();

  const text = chatInput.value.trim();
  if (!text) return;

  renderMessage('user', text);
  chatInput.value = '';
  showTyping();

  await sendToGemini(text);
});

chatInput.addEventListener('keydown', (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    e.preventDefault();
    sendBtn.click();
  }
});

// ======================================
// 3D MODEL LOADER WITH FALLBACK
// ======================================
async function initPreview() {
  // Clear any existing content first
  modelArea.innerHTML = '';
  
  const modelName = planet.toLowerCase();
  const modelUrl = `/models/${modelName}.glb`;

  // Show loading state
  modelArea.innerHTML = `
    <div class="model-placeholder" style="text-align:center;padding-top:80px;opacity:0.7;">
      <p>Loading ${planet} preview…</p>
    </div>
  `;

  // 1️⃣ Try to load 3D GLB model
  try {
    const headResponse = await fetch(modelUrl, { 
      method: "HEAD",
      cache: "no-store"
    });

    if (headResponse.ok) {
      // GLB file exists, render with model-viewer
      modelArea.innerHTML = '';
      const modelViewer = document.createElement('model-viewer');
      modelViewer.setAttribute('src', modelUrl);
      modelViewer.setAttribute('auto-rotate', '');
      modelViewer.setAttribute('camera-controls', '');
      modelViewer.setAttribute('exposure', '1.2');
      modelViewer.setAttribute('shadow-intensity', '1');
      modelViewer.setAttribute('alt', `3D model of ${planet}`);
      modelViewer.style.width = '100%';
      modelViewer.style.height = '380px';
      modelViewer.style.borderRadius = '14px';
      modelViewer.style.background = 'rgba(0,0,0,0.3)';
      modelArea.appendChild(modelViewer);
      console.log(`✓ Loaded 3D model for ${planet}`);
      return;
    }
  } catch (err) {
    console.log(`ℹ No GLB model found for ${planet}, trying NASA images...`);
  }

  // 2️⃣ Fallback to NASA Images API
  try {
    const searchQuery = encodeURIComponent(planet);
    const nasaUrl = `https://images-api.nasa.gov/search?q=${searchQuery}&media_type=image&page=1`;
    
    const nasaResponse = await fetch(nasaUrl);
    if (!nasaResponse.ok) throw new Error('NASA API error');
    
    const json = await nasaResponse.json();
    const items = json?.collection?.items || [];
    
    // Find first valid image
    let imageUrl = null;
    for (const item of items) {
      if (item.links && item.links.length > 0 && item.links[0].href) {
        imageUrl = item.links[0].href;
        break;
      }
    }

    if (imageUrl) {
      // Clear and render image
      modelArea.innerHTML = '';
      const img = document.createElement('img');
      img.src = imageUrl;
      img.alt = `${planet} preview from NASA`;
      img.style.width = '100%';
      img.style.height = 'auto';
      img.style.borderRadius = '14px';
      img.style.objectFit = 'cover';
      img.style.maxHeight = '380px';
      modelArea.appendChild(img);
      console.log(`✓ Loaded NASA image for ${planet}`);
      return;
    }
  } catch (err) {
    console.log(`ℹ NASA image load failed: ${err.message}`);
  }

  // 3️⃣ Final fallback - show placeholder
  modelArea.innerHTML = `
    <div class="model-placeholder" style="text-align:center;padding-top:120px;">
      <p style="opacity:0.6;">Preview not available for ${planet}</p>
    </div>
  `;
  console.log(`ℹ No preview available for ${planet}`);
}

// Initialize preview on page load
document.addEventListener('DOMContentLoaded', () => {
  initPreview();
});

// Fallback if DOM is already loaded
if (document.readyState !== 'loading') {
  initPreview();
}

// Ensure we don't cache previews in-memory across navigations (no internal cache retained)

