package nazenov.utils;

// Define a class to hold image data (URL and description)
public class ImageData {
    private final String imageUrl;
    private final String description;

    public ImageData(String imageUrl, String description) {
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}