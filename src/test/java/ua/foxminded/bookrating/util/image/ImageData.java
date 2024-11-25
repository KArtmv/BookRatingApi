package ua.foxminded.bookrating.util.image;

import lombok.Getter;
import ua.foxminded.bookrating.persistance.entity.Image;

@Getter
public class ImageData {
    private final Long id = 110464L;
    private final String imageUrlS = "http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg";
    private final String imageUrlM = "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg";
    private final String imageUrlL = "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg";
    private final Image image = new Image(id, imageUrlS, imageUrlM, imageUrlL);
    private final Image newImage = new Image(imageUrlS, imageUrlM, imageUrlL);
}
