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

    @URL
    @NotBlank(message = "Image URL Small is required")
    @Column(name = "image_url_small")
    private String imageUrlSmall;

    @URL
    @NotBlank(message = "Image URL Medium is required")
    @Column(name = "image_url_medium")
    private String imageUrlMedium;

    @URL
    @NotBlank(message = "Image URL Large is required")
    @Column(name = "image_url_large")
    private String imageUrlLarge;

    public Image(String imageUrlSmall, String imageUrlMedium, String imageUrlLarge) {
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlMedium = imageUrlMedium;
        this.imageUrlLarge = imageUrlLarge;
    }
}