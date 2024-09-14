package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "image")
@SequenceGenerator(name = "default_gen", sequenceName = "image_id_seq", allocationSize = 1)
public class Image extends BaseEntity {

    private String imageUrlSmall;

    private String imageUrlMedium;

    private String imageUrlLarge;

    public Image(String imageUrlSmall, String imageUrlMedium, String imageUrlLarge) {
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlMedium = imageUrlMedium;
        this.imageUrlLarge = imageUrlLarge;
    }
}