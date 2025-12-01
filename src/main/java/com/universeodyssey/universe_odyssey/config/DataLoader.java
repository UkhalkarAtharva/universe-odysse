package com.universeodyssey.universe_odyssey.config;

import com.universeodyssey.universe_odyssey.model.PlanetDetail;
import com.universeodyssey.universe_odyssey.repository.PlanetDetailRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Optional;
import com.universeodyssey.universe_odyssey.repository.AdminUserRepository;
import com.universeodyssey.universe_odyssey.model.AdminUser;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initPlanets(PlanetDetailRepository repo) {
        return args -> {
            // check if Mercury exists first
            Optional<PlanetDetail> maybeMercury = repo.findByNameIgnoreCase("Mercury");
            if (maybeMercury.isEmpty()) {
                PlanetDetail mercury = new PlanetDetail();
                mercury.setName("Mercury");
                mercury.setMass(0.330); // 10^24 kg
                mercury.setRadius(2439.7);
                mercury.setGravity(3.7);
                mercury.setMoons(0);
                mercury.setDistanceFromSun(57.9); // million km
                mercury.setOrbitalPeriod(88.0);
                mercury.setSurfaceTemp(167.0);
                mercury.setImageFilename("/assets/Mercury_planet_realistic_render_fed6db22-CIWe9eNA.png"); // adapt to your assets path
                mercury.setShortDescription("The smallest planet in our solar system.");
                mercury.setLongDescription("Mercury is the smallest planet and nearest the Sun. " +
                        "Surface is heavily cratered and has extremes of temperature.");
                mercury.setAtmosphere("None");
                repo.save(mercury);
            }

            // Venus
            if (repo.findByNameIgnoreCase("Venus").isEmpty()) {
                PlanetDetail v = new PlanetDetail();
                v.setName("Venus");
                v.setMass(4.87);
                v.setRadius(6051.8);
                v.setGravity(8.87);
                v.setMoons(0);
                v.setDistanceFromSun(108.2);
                v.setOrbitalPeriod(224.7);
                v.setSurfaceTemp(464.0);
                v.setImageFilename("/assets/Venus_planet_realistic_render_22a4a679-C7SpMfxJ.png");
                v.setShortDescription("A thick CO2 atmosphere that traps heat.");
                v.setLongDescription("Venus spins slowly in the opposite direction from most planets. " +
                        "It has a runaway greenhouse effect and is the hottest planet.");
                v.setAtmosphere("CO2,N2");
                repo.save(v);
            }

            // Earth
            if (repo.findByNameIgnoreCase("Earth").isEmpty()) {
                PlanetDetail e = new PlanetDetail();
                e.setName("Earth");
                e.setMass(5.97);
                e.setRadius(6371.0);
                e.setGravity(9.8);
                e.setMoons(1);
                e.setDistanceFromSun(149.6);
                e.setOrbitalPeriod(365.25);
                e.setSurfaceTemp(15.0);
                e.setImageFilename("/assets/Earth_planet_realistic_render_1399d254-DqWgWjsS.png");
                e.setShortDescription("Our home planet.");
                e.setLongDescription("Earth is the only known planet to support life, with large bodies of water and an oxygen-rich atmosphere.");
                e.setAtmosphere("N2,O2,Ar,CO2");
                repo.save(e);
            }

            // Mars
            if (repo.findByNameIgnoreCase("Mars").isEmpty()) {
                PlanetDetail mars = new PlanetDetail();
                mars.setName("Mars");
                mars.setMass(0.642);
                mars.setRadius(3389.5);
                mars.setGravity(3.71);
                mars.setMoons(2);
                mars.setDistanceFromSun(227.9);
                mars.setOrbitalPeriod(687.0);
                mars.setSurfaceTemp(-65.0);
                mars.setImageFilename("/assets/Mars_planet_realistic_render.png");
                mars.setShortDescription("The red planet known for potential past water and life exploration.");
                mars.setLongDescription("Mars is the fourth planet from the Sun and a primary target for human exploration. " +
                        "Evidence suggests liquid water once flowed on its surface. It has two small moons: Phobos and Deimos. " +
                        "The planet's red color comes from iron oxide (rust) in its soil.");
                mars.setAtmosphere("CO2,N2,Ar");
                repo.save(mars);
            }

            // Jupiter
            if (repo.findByNameIgnoreCase("Jupiter").isEmpty()) {
                PlanetDetail jupiter = new PlanetDetail();
                jupiter.setName("Jupiter");
                jupiter.setMass(1898.0);
                jupiter.setRadius(69911.0);
                jupiter.setGravity(24.79);
                jupiter.setMoons(95);
                jupiter.setDistanceFromSun(778.5);
                jupiter.setOrbitalPeriod(4333.0);
                jupiter.setSurfaceTemp(-110.0);
                jupiter.setImageFilename("/assets/Jupiter_planet_realistic_render.png");
                jupiter.setShortDescription("The largest planet in our solar system with a Great Red Spot storm.");
                jupiter.setLongDescription("Jupiter is a gas giant and the most massive planet in the solar system. " +
                        "It has a strong magnetic field and at least 95 known moons, including the four large Galilean moons. " +
                        "The Great Red Spot is a persistent storm larger than Earth that has raged for at least 300 years.");
                jupiter.setAtmosphere("H2,He,CH4,NH3,H2O");
                repo.save(jupiter);
            }

            // Saturn
            if (repo.findByNameIgnoreCase("Saturn").isEmpty()) {
                PlanetDetail saturn = new PlanetDetail();
                saturn.setName("Saturn");
                saturn.setMass(568.0);
                saturn.setRadius(58232.0);
                saturn.setGravity(10.44);
                saturn.setMoons(146);
                saturn.setDistanceFromSun(1434.0);
                saturn.setOrbitalPeriod(10759.0);
                saturn.setSurfaceTemp(-140.0);
                saturn.setImageFilename("/assets/Saturn_planet_realistic_render.png");
                saturn.setShortDescription("The ringed giant with a spectacular ring system visible from Earth.");
                saturn.setLongDescription("Saturn is famous for its extensive ring system made of ice and rock particles. " +
                        "It is the second-largest planet in the solar system. Saturn has at least 146 known moons, " +
                        "with Titan being the largest and the only moon known to have a thick atmosphere. " +
                        "Saturn's density is so low it would float in water.");
                saturn.setAtmosphere("H2,He,CH4,NH3");
                repo.save(saturn);
            }

            // Uranus
            if (repo.findByNameIgnoreCase("Uranus").isEmpty()) {
                PlanetDetail uranus = new PlanetDetail();
                uranus.setName("Uranus");
                uranus.setMass(86.8);
                uranus.setRadius(25362.0);
                uranus.setGravity(8.87);
                uranus.setMoons(28);
                uranus.setDistanceFromSun(2871.0);
                uranus.setOrbitalPeriod(30688.0);
                uranus.setSurfaceTemp(-195.0);
                uranus.setImageFilename("/assets/Uranus_planet_realistic_render.png");
                uranus.setShortDescription("An ice giant that rotates on its side with a faint ring system.");
                uranus.setLongDescription("Uranus is an ice giant with a unique property: it rotates on its side, " +
                        "likely due to a collision early in the solar system's formation. " +
                        "It has a faint ring system and at least 28 known moons. " +
                        "Uranus has the coldest planetary atmosphere in the solar system and appears as a featureless blue-green sphere.");
                uranus.setAtmosphere("H2,He,CH4,NH3,H2O");
                repo.save(uranus);
            }

            // Neptune
            if (repo.findByNameIgnoreCase("Neptune").isEmpty()) {
                PlanetDetail neptune = new PlanetDetail();
                neptune.setName("Neptune");
                neptune.setMass(102.4);
                neptune.setRadius(24622.0);
                neptune.setGravity(11.15);
                neptune.setMoons(16);
                neptune.setDistanceFromSun(4495.0);
                neptune.setOrbitalPeriod(60182.0);
                neptune.setSurfaceTemp(-200.0);
                neptune.setImageFilename("/assets/Neptune_planet_realistic_render.png");
                neptune.setShortDescription("The windy blue ice giant at the edge of the solar system.");
                neptune.setLongDescription("Neptune is the eighth and farthest planet from the Sun in the solar system. " +
                        "Despite being far from the Sun, it has the fastest winds in the solar system, reaching speeds up to 2,100 km/h. " +
                        "It has 16 known moons, with Triton being the largest and geologically active. " +
                        "Neptune's striking blue color comes from methane in its atmosphere.");
                neptune.setAtmosphere("H2,He,CH4,NH3");
                repo.save(neptune);
            }
        };
    }

    @Bean
    CommandLineRunner initAdmin(AdminUserRepository adminRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            String defaultAdmin = "admin";
            if (adminRepo.findByUsername(defaultAdmin) == null) {
                AdminUser a = new AdminUser();
                a.setUsername(defaultAdmin);
                a.setPassword(passwordEncoder.encode("Admin@123"));
                a.setRole("ROLE_ADMIN");
                adminRepo.save(a);
            }
        };
    }

    @Bean
    CommandLineRunner resetAdminPassword(AdminUserRepository adminRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                String adminName = "admin";
                String newPassword = "Bond@700"; // temporary reset requested by user
                AdminUser existing = adminRepo.findByUsername(adminName);
                if (existing != null) {
                    existing.setPassword(passwordEncoder.encode(newPassword));
                    adminRepo.save(existing);
                    System.out.println("[DataLoader] Reset password for admin user '" + adminName + "'.");
                } else {
                    AdminUser a = new AdminUser();
                    a.setUsername(adminName);
                    a.setPassword(passwordEncoder.encode(newPassword));
                    a.setRole("ROLE_ADMIN");
                    adminRepo.save(a);
                    System.out.println("[DataLoader] Created admin user '" + adminName + "' with new password.");
                }
            } catch (Exception ex) {
                System.err.println("[DataLoader] Failed to reset admin password: " + ex.getMessage());
            }
        };
    }
}
