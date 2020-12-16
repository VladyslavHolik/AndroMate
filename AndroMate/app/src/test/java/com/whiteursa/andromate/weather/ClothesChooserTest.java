package com.whiteursa.andromate.weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClothesChooserTest {
    private ClothesChooser chooser;

    @Before
    public void setUp() {
        chooser = new ClothesChooser();
    }
    @After
    public void tearDown() {
        chooser = null;
    }

    @Test
    public void testGetClothes() {
        String[] result = chooser.getClothes(26, true);

        assertNotNull(result);
        assertEquals("Regular T-shirt", result[0]);
    }
}