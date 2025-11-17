/**
 * 3D Planet Viewer using Three.js
 * Renders a 3D rotating planet with proper coloring based on planet type
 */

class PlanetViewer {
    constructor(canvasId, planetName) {
        this.canvasId = canvasId;
        this.planetName = planetName;
        this.scene = null;
        this.camera = null;
        this.renderer = null;
        this.planet = null;
        this.init();
    }

    init() {
        const canvas = document.getElementById(this.canvasId);
        if (!canvas) return;

        const width = canvas.clientWidth;
        const height = canvas.clientHeight;

        // Scene setup
        this.scene = new THREE.Scene();
        this.scene.background = new THREE.Color(0x000000);

        // Camera setup
        this.camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
        this.camera.position.z = 2.5;

        // Renderer setup
        this.renderer = new THREE.WebGLRenderer({ 
            canvas: canvas, 
            antialias: true, 
            alpha: true 
        });
        this.renderer.setSize(width, height);
        this.renderer.setPixelRatio(window.devicePixelRatio);

        // Lighting
        const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
        this.scene.add(ambientLight);

        const sunLight = new THREE.PointLight(0xffffff, 1.5);
        sunLight.position.set(5, 5, 5);
        this.scene.add(sunLight);

        // Create planet
        this.createPlanet();

        // Add stars
        this.addStars();

        // Handle window resize
        window.addEventListener('resize', () => this.onWindowResize());

        // Start animation
        this.animate();
    }

    createPlanet() {
        const planetColors = {
            'Mercury': { color: 0x8c7853, textureUrl: null },
            'Venus': { color: 0xffc649, textureUrl: null },
            'Earth': { color: 0x4da6ff, textureUrl: null },
            'Mars': { color: 0xff6b35, textureUrl: null },
            'Jupiter': { color: 0xc88b3a, textureUrl: null },
            'Saturn': { color: 0xfad5a5, textureUrl: null },
            'Uranus': { color: 0x4fd0e7, textureUrl: null },
            'Neptune': { color: 0x4166f5, textureUrl: null }
        };

        const config = planetColors[this.planetName] || { color: 0x888888, textureUrl: null };
        
        // Create sphere geometry
        const geometry = new THREE.SphereGeometry(1, 64, 64);
        
        // Create material with proper shading
        const material = new THREE.MeshPhongMaterial({
            color: config.color,
            shininess: 10,
            emissive: new THREE.Color(0x000000)
        });

        this.planet = new THREE.Mesh(geometry, material);
        this.scene.add(this.planet);

        // Add clouds/atmosphere effect for certain planets
        if (['Earth', 'Venus', 'Jupiter', 'Saturn', 'Uranus', 'Neptune'].includes(this.planetName)) {
            this.addAtmosphere();
        }
    }

    addAtmosphere() {
        const atmosphereGeometry = new THREE.SphereGeometry(1.05, 64, 64);
        const atmosphereMaterial = new THREE.MeshPhongMaterial({
            color: 0xffffff,
            transparent: true,
            opacity: 0.1,
            shininess: 5
        });
        const atmosphere = new THREE.Mesh(atmosphereGeometry, atmosphereMaterial);
        this.scene.add(atmosphere);
    }

    addStars() {
        const starsGeometry = new THREE.BufferGeometry();
        const starCount = 1000;
        const positions = new Float32Array(starCount * 3);

        for (let i = 0; i < starCount * 3; i += 3) {
            positions[i] = (Math.random() - 0.5) * 100;
            positions[i + 1] = (Math.random() - 0.5) * 100;
            positions[i + 2] = (Math.random() - 0.5) * 100;
        }

        starsGeometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        const starsMaterial = new THREE.PointsMaterial({
            color: 0xffffff,
            size: 0.1
        });

        const stars = new THREE.Points(starsGeometry, starsMaterial);
        this.scene.add(stars);
    }

    animate() {
        requestAnimationFrame(() => this.animate());

        // Rotate planet
        if (this.planet) {
            this.planet.rotation.y += 0.001;
        }

        // Rotate scene for better view
        this.scene.rotation.z += 0.0002;

        this.renderer.render(this.scene, this.camera);
    }

    onWindowResize() {
        const canvas = document.getElementById(this.canvasId);
        if (!canvas) return;

        const width = canvas.clientWidth;
        const height = canvas.clientHeight;

        this.camera.aspect = width / height;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(width, height);
    }
}

// Initialize when page loads
document.addEventListener('DOMContentLoaded', function() {
    const planetName = document.body.getAttribute('data-planet');
    if (planetName) {
        const canvas = document.getElementById('planet-canvas');
        if (canvas && typeof THREE !== 'undefined') {
            new PlanetViewer('planet-canvas', planetName);
        }
    }
});
