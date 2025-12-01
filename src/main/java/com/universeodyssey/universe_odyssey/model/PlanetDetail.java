package com.universeodyssey.universe_odyssey.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class PlanetDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double mass;          // in 10^24 kg or keep units in description
    private Double gravity;       // m/s^2
    private Integer moons;
    private Double radius;        // km
    private Double distanceFromSun; // million km
    private Double orbitalPeriod; // days
    private Double surfaceTemp;   // °C (average)
    private String imageFilename; // path under /assets/...
    @Lob
    private String shortDescription;
    @Lob
    private String longDescription;
    private String atmosphere;    // simple string e.g., "CO2,N2,O2"

    // getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Double getGravity() {
        return gravity;
    }

    public void setGravity(Double gravity) {
        this.gravity = gravity;
    }

    public Integer getMoons() {
        return moons;
    }

    public void setMoons(Integer moons) {
        this.moons = moons;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getDistanceFromSun() {
        return distanceFromSun;
    }

    public void setDistanceFromSun(Double distanceFromSun) {
        this.distanceFromSun = distanceFromSun;
    }

    public Double getOrbitalPeriod() {
        return orbitalPeriod;
    }

    public void setOrbitalPeriod(Double orbitalPeriod) {
        this.orbitalPeriod = orbitalPeriod;
    }

    public Double getSurfaceTemp() {
        return surfaceTemp;
    }

    public void setSurfaceTemp(Double surfaceTemp) {
        this.surfaceTemp = surfaceTemp;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(String atmosphere) {
        this.atmosphere = atmosphere;
    }
}
