package ua.foxminded.bookrating.util.image;

import lombok.Getter;
import ua.foxminded.bookrating.persistance.entity.Image;

@Getter
public class ImageData {
    private final Long id = 110464L;
    private final Image image = new Image(id, "http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
            "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
            "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg");

    private final Image newImage = new Image("http://images.amazon.com/images/P/0736688390.01.THUMBZZZ.jpg",
            "http://images.amazon.com/images/P/0736688390.01.MZZZZZZZ.jpg",
            "http://images.amazon.com/images/P/0736688390.01.LZZZZZZZ.jpg");
}
