package com.ninjaone.dundie_awards;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AwardsCacheTests {

    @Autowired
    private AwardsCache cache;

    @Test
    public void testAddAward() {
        final int totalAward = cache.getTotalAwards();

        cache.addOneAward();

        assertEquals(totalAward + 1, cache.getTotalAwards());
    }

}
