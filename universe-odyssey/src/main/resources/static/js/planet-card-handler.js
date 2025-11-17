/**
 * Planet Card Click Handler (restored workflow)
 * - Deterministic, lightweight event-delegation: only runs when user clicks
 * - Extracts planet name from the clicked card or its ancestors using
 *   explicit attributes/text and a trusted planet list
 * - Redirects to `/chat?planet={PlanetName}` (preserves capitalized PlanetName)
 * - No MutationObservers, no scanning of the whole DOM, no polling
 */

(function() {
    const PLANETS = ['Mercury','Venus','Earth','Mars','Jupiter','Saturn','Uranus','Neptune'];

    function findPlanetNameFromElement(el) {
        let node = el;
        while (node && node !== document.body) {
            // 1) Check explicit data attributes
            const dataName = (node.dataset && (node.dataset.planet || node.dataset.planetName || node.dataset.name));
            if (dataName) {
                const match = matchPlanetName(dataName);
                if (match) return match;
            }

            // 2) Check aria-label, title, alt
            const aria = node.getAttribute && node.getAttribute('aria-label');
            if (aria) { const match = matchPlanetName(aria); if (match) return match; }
            const title = node.getAttribute && node.getAttribute('title');
            if (title) { const match = matchPlanetName(title); if (match) return match; }

            // 3) Look for common name containers inside the card (cheap DOM ops limited to the ancestor)
            try {
                const nameEl = node.querySelector && (node.querySelector('.planet-name, .card-title, .title, .name'));
                if (nameEl && nameEl.innerText) {
                    const match = matchPlanetName(nameEl.innerText);
                    if (match) return match;
                }
            } catch (e) {
                // ignore querySelector errors on non-element nodes
            }

            node = node.parentElement;
        }
        return null;
    }

    function matchPlanetName(text) {
        if (!text) return null;
        const t = text.toString().trim();
        for (const p of PLANETS) {
            // match whole word or exact case-insensitive inclusion (deterministic)
            const re = new RegExp('\\b' + p.replace(/[-\\/\\^$*+?.()|[\]{}]/g,'\\$&') + '\\b', 'i');
            if (re.test(t)) return p; // return canonical capitalized name
        }
        return null;
    }

    function onDocumentClick(e) {
        // First: special-case "Learn More" buttons commonly emitted by the SPA build.
        // These buttons often have a data-testid like `button-learn-more-{planetKey}`.
        const learnBtn = e.target.closest && e.target.closest('[data-testid^="button-learn-more-"]');
        if (learnBtn) {
            const tid = learnBtn.getAttribute('data-testid') || '';
            const parts = tid.split('button-learn-more-');
            if (parts.length === 2 && parts[1]) {
                const key = parts[1].trim();
                const map = {
                    mercury: 'Mercury', venus: 'Venus', earth: 'Earth', mars: 'Mars',
                    jupiter: 'Jupiter', saturn: 'Saturn', uranus: 'Uranus', neptune: 'Neptune'
                };
                const planetName = map[key.toLowerCase()] || (key.charAt(0).toUpperCase() + key.slice(1));
                e.preventDefault();
                e.stopPropagation();
                window.location.href = `/chat?planet=${encodeURIComponent(planetName)}`;
                return;
            }
        }

        // If it wasn't a Learn More button, ignore clicks on unrelated controls
        const ignoredTags = ['A','INPUT','TEXTAREA','SELECT','LABEL'];
        if (ignoredTags.includes(e.target.tagName)) return;

        const planet = findPlanetNameFromElement(e.target);
        if (planet) {
            // Deterministic redirect to chat page (preserve Planet case)
            window.location.href = `/chat?planet=${encodeURIComponent(planet)}`;
        }
    }

    function onKeyDown(e) {
        // Support keyboard activation when Enter is pressed on focused card-like element
        if (e.key !== 'Enter') return;
        const active = document.activeElement;
        if (!active) return;
        // Avoid triggering from input fields
        const tag = active.tagName;
        if (['INPUT','TEXTAREA','SELECT'].includes(tag)) return;

        const planet = findPlanetNameFromElement(active);
        if (planet) {
            e.preventDefault();
            window.location.href = `/chat?planet=${encodeURIComponent(planet)}`;
        }
    }

    document.addEventListener('click', onDocumentClick, true);
    document.addEventListener('keydown', onKeyDown, true);

})();
