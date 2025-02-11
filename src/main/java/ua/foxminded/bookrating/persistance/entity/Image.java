package ua.foxminded.bookrating.persistance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Image {

    @Schema(description = "URL of the book's small-sized cover image",
            example = "http://images.amazon.com/images/P/0736688390.01.SMALL.jpg")
    @NotBlank(message = "Small image URL is required")
    @URL(protocol = "http", message = "Small image URL must be a valid HTTP URL")
    @Column(name = "image_url_small")
    private String imageUrlSmall;

    @Schema(description = "URL of the book's medium-sized cover image",
            example = "http://images.amazon.com/images/P/0736688390.01.MEDIUM.jpg")
    @NotBlank(message = "Medium image URL is required")
    @URL(protocol = "http", message = "Medium image URL must be a valid HTTP URL")
    @Column(name = "image_url_medium")
    private String imageUrlMedium;

    @Schema(description = "URL of the book's large-sized cover image",
            example = "http://images.amazon.com/images/P/0736688390.01.LARGE.jpg")
    @NotBlank(message = "Large image URL is required")
    @URL(protocol = "http", message = "Large image URL must be a valid HTTP URL")
    @Column(name = "image_url_large")
    private String imageUrlLarge;

    public Image(String imageUrlSmall, String imageUrlMedium, String imageUrlLarge) {
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlMedium = imageUrlMedium;
        this.imageUrlLarge = imageUrlLarge;
    }
}