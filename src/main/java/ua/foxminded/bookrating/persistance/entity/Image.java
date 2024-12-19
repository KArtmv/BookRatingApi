package ua.foxminded.bookrating.persistance.entity;

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

    @NotBlank(message = "Image URL Small is required")
    @URL(protocol = "http", message = "Image URL Small must be a valid HTTP URL.")
    @Column(name = "image_url_small")
    private String imageUrlSmall;

    @NotBlank(message = "Image URL Medium is required")
    @URL(protocol = "http", message = "Image URL Medium must be a valid HTTP URL.")
    @Column(name = "image_url_medium")
    private String imageUrlMedium;

    @NotBlank(message = "Image URL Large is required")
    @URL(protocol = "http", message = "Image URL Large must be a valid HTTP URL.")
    @Column(name = "image_url_large")
    private String imageUrlLarge;

    public Image(String imageUrlSmall, String imageUrlMedium, String imageUrlLarge) {
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlMedium = imageUrlMedium;
        this.imageUrlLarge = imageUrlLarge;
    }
}