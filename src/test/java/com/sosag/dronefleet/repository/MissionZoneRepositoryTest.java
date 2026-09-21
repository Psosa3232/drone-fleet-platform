package com.sosag.dronefleet.repository;

import com.sosag.dronefleet.model.MissionZone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for the {@link MissionZoneRepository}.
 *
 * <p>This test verifies that mission zone data can be persisted
 * and retrieved correctly using Spring Data JPA and PostgreSQL.</p>
 */
@SpringBootTest
@Transactional
class MissionZoneRepositoryTest {

    /**
     * Repository used to access mission zone data.
     */
    @Autowired
    private MissionZoneRepository missionZoneRepository;

    /**
     * Verifies that a mission zone can be saved to the database
     * and subsequently retrieved by its generated identifier.
     */
    @Test
    void shouldSaveAndFindMissionZone() {

        MissionZone missionZone = new MissionZone();

        missionZone.setName("Madrid Central");
        missionZone.setDescription("Urban operational zone");
        missionZone.setLatitude(new BigDecimal("40.416775"));
        missionZone.setLongitude(new BigDecimal("-3.703790"));
        missionZone.setMaxAltitude(new BigDecimal("120.00"));
        missionZone.setActive(true);
        missionZone.setCreatedAt(LocalDateTime.now());

        MissionZone savedMissionZone =
                missionZoneRepository.save(missionZone);

        assertNotNull(savedMissionZone.getMissionZoneId());

        var foundMissionZone =
                missionZoneRepository.findById(savedMissionZone.getMissionZoneId());

        assertTrue(foundMissionZone.isPresent());
        assertEquals("Madrid Central", foundMissionZone.get().getName());
        assertEquals("Urban operational zone",
                foundMissionZone.get().getDescription());
    }
}