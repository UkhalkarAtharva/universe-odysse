package com.universeodyssey.universe_odyssey.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanetDetailResponse {
    private String name;
    private Double mass;
    private Double gravity;
    private Double radius;
    private Double density;
    private Double escapeVelocity;
    private Double orbitalPeriod;
    private Double rotationPeriod;
    private Double distanceFromSun;
    private Double temperature;
    private List<String> atmosphere;
    private String overview;
    private String shortDescription;
    private String longDescription;
    private String nasaImageUrl;
    private String modelUrl;
    private Integer moons;
    private String imageFilename;
    private Boolean found;
    private Boolean modelExists;

    public PlanetDetailResponse() {
        this.found = false;
    }

    public PlanetDetailResponse(String name) {
        this.name = name;
        this.found = false;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getMass() { return mass; }
    public void setMass(Double mass) { this.mass = mass; }

    public Double getGravity() { return gravity; }
    public void setGravity(Double gravity) { this.gravity = gravity; }

    public Double getRadius() { return radius; }
    public void setRadius(Double radius) { this.radius = radius; }

    public Double getDensity() { return density; }
    public void setDensity(Double density) { this.density = density; }

    public Double getEscapeVelocity() { return escapeVelocity; }
    public void setEscapeVelocity(Double escapeVelocity) { this.escapeVelocity = escapeVelocity; }

    public Double getOrbitalPeriod() { return orbitalPeriod; }
    public void setOrbitalPeriod(Double orbitalPeriod) { this.orbitalPeriod = orbitalPeriod; }

    public Double getRotationPeriod() { return rotationPeriod; }
    public void setRotationPeriod(Double rotationPeriod) { this.rotationPeriod = rotationPeriod; }

    public Double getDistanceFromSun() { return distanceFromSun; }
    public void setDistanceFromSun(Double distanceFromSun) { this.distanceFromSun = distanceFromSun; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public List<String> getAtmosphere() { return atmosphere; }
    public void setAtmosphere(List<String> atmosphere) { this.atmosphere = atmosphere; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getLongDescription() { return longDescription; }
    public void setLongDescription(String longDescription) { this.longDescription = longDescription; }

    public String getNasaImageUrl() { return nasaImageUrl; }
    public void setNasaImageUrl(String nasaImageUrl) { this.nasaImageUrl = nasaImageUrl; }

    public String getModelUrl() { return modelUrl; }
    public void setModelUrl(String modelUrl) { this.modelUrl = modelUrl; }

    public Integer getMoons() { return moons; }
    public void setMoons(Integer moons) { this.moons = moons; }

    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }

    public Boolean getFound() { return found; }
    public void setFound(Boolean found) { this.found = found; }

    public Boolean getModelExists() { return modelExists; }
    public void setModelExists(Boolean modelExists) { this.modelExists = modelExists; }
}
