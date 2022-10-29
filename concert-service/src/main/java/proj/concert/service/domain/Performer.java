package proj.concert.service.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.types.Genre;

/**
 * Represents a Performer Object
 * id          the generated id of the performer object
 * name        the name of the performer
 * imageName   the name of the image file
 * genre       the genre of music that the performer makes
 * blurb       a description of the performer
 */

@Entity
@Table(name="PERFORMERS")
@Embeddable
public class Performer{

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Column(name="image_name")
    private String imageName;

    @Enumerated(EnumType.STRING)
    @Column(name="GENRE")
    private Genre genre;
    @Column(name = "BLURB", columnDefinition = "LONGTEXT")
    private String blurb;

    public Performer(){}

    public Performer(long id, String name, String imageName, Genre genre, String blurb) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.genre = genre;
        this.blurb = blurb;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getBlurb() {
        return blurb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Performer))
            return false;
        if (other == this)
            return true;

        Performer p = (Performer) other;
        return new EqualsBuilder().
                append(id, p.getId()).
                append(name, p.getName()).
                isEquals();
    }
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(getClass().getName()).
                append(id).
                append(name).
                toHashCode();
    }
}