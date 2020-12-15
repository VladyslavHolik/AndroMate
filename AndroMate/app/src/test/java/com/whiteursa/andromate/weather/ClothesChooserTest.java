package com.whiteursa.andromate.weather;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClothesChooserTest {
    ClothesChooser chooser;

    @Before
    public void setUp() throws Exception {
        chooser = new ClothesChooser();
    }
    @After
    public void tearDown() throws Exception {
        chooser = null;
    }

    @Test
    public void testGetClothes() {
        String[] result = chooser.getClothes(26, true);

        assertNotNull(result);
        assertTrue(result[0].equals("Обычная футболка"));
    }
}