package edu.byu.cs.superasteroids.model;

/**
 * Since images are used so often, it made sense to create a struct for them
 */
public class Image {
    /**
     * the constructor and struct for an image consist of two parts
     * @param _path         the path to the image
     * @param _image_width  the width of the image
     * @param _image_height the height of the image
     */
    public Image(String _path, int _image_width, int _image_height)
    {
        path = _path;
        image_width = _image_width;
        image_height = _image_height;
    }

    public int getContentID()
    {
        return content_id;
    }

    public void setContentID(int id)
    {
        content_id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return image_width;
    }

    public void setWidth(int image_width) {
        this.image_width = image_width;
    }

    public int getHeight() {
        return image_height;
    }

    public void setHeight(int image_height) {
        this.image_height = image_height;
    }

    /**
     * the path to the image is stored as a string
     */

    private String path;

    /**
     * the width of the image is stored as an integer
     */
    private int image_width;

    /**
     * the height of the image is also stored as an integer
     */
    private int image_height;

    private int content_id;

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        else if (this == other)
        {
            return true;
        }
        else if (getClass() != other.getClass())
        {
            return false;
        }
        else
        {
            Image other_image = (Image)other;

            if (!(getPath().equals(other_image.getPath())))
            {
                return false;
            }
            else if (getWidth() != other_image.getWidth())
            {
                return false;
            }
            else if (getHeight() != other_image.getHeight())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}
