package edu.byu.cs.superasteroids.model;

import junit.framework.TestCase;

public class ImageTest extends TestCase {

    Image image = new Image("path", 1, 2);

    public void testGetContentID() throws Exception {
        image.setContentID(3);
        assertEquals(3, image.getContentID());
    }

    public void testGetPath() throws Exception {
        assertEquals("path", image.getPath());
    }

    public void testGetWidth() throws Exception {
        assertEquals(1, image.getWidth());
    }

    public void testGetHeight() throws Exception {
        assertEquals(2, image.getHeight());
    }
}