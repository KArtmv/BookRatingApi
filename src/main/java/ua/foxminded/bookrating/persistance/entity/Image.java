package ua.foxminded.bookrating.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Image {

    @Column(name = "image_url_small")
    private String imageUrlSmall;

    @Column(name = "image_url_medium")
    private String imageUrlMedium;

    @Column(name = "image_url_large")
    private String imageUrlLarge;

    public Image(String imageUrlSmall, String imageUrlMedium, String imageUrlLarge) {
        this.imageUrlSmall = imageUrlSmall;
        this.imageUrlMedium = imageUrlMedium;
        this.imageUrlLarge = imageUrlLarge;
    }
}