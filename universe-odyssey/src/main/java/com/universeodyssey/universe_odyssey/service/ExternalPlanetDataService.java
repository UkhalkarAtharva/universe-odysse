package com.universeodyssey.universe_odyssey.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.universeodyssey.universe_odyssey.dto.PlanetDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@Service
public class ExternalPlanetDataService {

    private static final String JPL_PHYS_API = "https://ssd-api.jpl.nasa.gov/phys.api?body=";
    private static final String NASA_IMAGES_API = "https://images-api.nasa.gov/search";

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PlanetDetailResponse fetchPlanet(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        final String key = name.trim().toLowerCase();
        PlanetDetailResponse dto = new PlanetDetailResponse();
        dto.setName(capitalize(name.trim()));

        try {
            // 1) Fetch JPL physical parameters (text response)
            String physUrl = JPL_PHYS_API + URLEncoder.encode(name.trim(), StandardCharsets.UTF_8);
            String physBody = restTemplate.getForObject(physUrl, String.class);
            if (physBody != null) {
                parseJplPhys(physBody, dto);
            }
        } catch (Exception e) {
            System.err.println("Error fetching JPL phys data: " + e.getMessage());
        }

        try {
            // 2) Fetch NASA images API (JSON)
            String imagesUrl = NASA_IMAGES_API + "?q=" + URLEncoder.encode(name.trim(), StandardCharsets.UTF_8) + "&media_type=image";
            String imagesBody = restTemplate.getForObject(imagesUrl, String.class);
            if (imagesBody != null) {
                JsonNode root = objectMapper.readTree(imagesBody);
                JsonNode items = root.path("collection").path("items");
                if (items.isArray()) {
                    for (JsonNode item : items) {
                        JsonNode links = item.path("links");
                        if (links.isArray() && links.size() > 0) {
                            String href = links.get(0).path("href").asText(null);
                            if (href != null && !href.isEmpty()) {
                                dto.setNasaImageUrl(href);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching NASA images: " + e.getMessage());
        }

        // model url and existence check
        String modelPath = "/models/" + key + ".glb";
        dto.setModelUrl(modelPath);
        dto.setModelExists(checkModelExists(key));

        // basic overview from name
        dto.setOverview(dto.getOverview() == null ? (dto.getName() + " — planetary body data from NASA APIs.") : dto.getOverview());

        return dto;
    }

    @Cacheable(value = "planets", key = "#name.toLowerCase()")
    public PlanetDetailResponse getPlanetData(String name) {
        PlanetDetailResponse dto = fetchPlanet(name);
        if (dto == null) return new PlanetDetailResponse(name);
        dto.setFound(true);
        return dto;
    }

    private boolean checkModelExists(String key) {
        try {
            // check local static models folder (works in dev). In packaged JAR this may differ.
            File f = new File("src/main/resources/static/models/" + key + ".glb");
            return f.exists() && f.isFile();
        } catch (Exception e) {
            return false;
        }
    }

    private void parseJplPhys(String body, PlanetDetailResponse dto) {
        // JPL phys API returns a text response with lines like 'mean_radius=6371.0'
        // We will parse key/value pairs where possible.
        try {
            String[] lines = body.split("\n");
            for (String l : lines) {
                String line = l.trim();
                if (line.isEmpty() || line.startsWith("#") || !line.contains("=")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length < 2) continue;
                String k = parts[0].trim().toLowerCase();
                String v = parts[1].trim();
                if (v.equalsIgnoreCase("null") || v.equals("")) continue;
                try {
                    switch (k) {
                        case "mean_radius":
                        case "radius":
                            dto.setRadius(Double.parseDouble(v));
                            break;
                        case "mass":
                            // mass may be in scientific format
                            dto.setMass(Double.parseDouble(v));
                            break;
                        case "gravity":
                            dto.setGravity(Double.parseDouble(v));
                            break;
                        case "mean_density":
                        case "density":
                            dto.setDensity(Double.parseDouble(v));
                            break;
                        case "escape":
                        case "escape_velocity":
                            dto.setEscapeVelocity(Double.parseDouble(v));
                            break;
                        case "sideral_orbit":
                        case "orbital_period":
                            dto.setOrbitalPeriod(Double.parseDouble(v));
                            break;
                        case "sideral_rotation":
                        case "rotation_period":
                            dto.setRotationPeriod(Double.parseDouble(v));
                            break;
                        case "mean_temperature":
                        case "temperature":
                            dto.setTemperature(Double.parseDouble(v));
                            break;
                        case "moons":
                            try {
                                dto.setMoons(Integer.parseInt(v));
                            } catch (NumberFormatException ex) { }
                            break;
                        case "atmosphere":
                        case "atmosphere_composition":
                            // split by commas
                            String[] comps = v.split(",");
                            List<String> list = new ArrayList<>();
                            for (String s : comps) { if (!s.trim().isEmpty()) list.add(s.trim()); }
                            dto.setAtmosphere(list);
                            break;
                        default:
                            // ignore
                    }
                } catch (Exception e) {
                    // ignore parse errors per-field
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing JPL phys response: " + e.getMessage());
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
