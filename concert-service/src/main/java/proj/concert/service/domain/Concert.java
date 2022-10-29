package proj.concert.service.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.dto.PerformerDTO;

/**
 * Represents a Concert Object
 * id          the generated id of the concert
 * title       the name of the concert
 * imageName   the name of the image file
 * blurb       a description of the concert
 * dates       the dates of the concert
 * performers  the Performers performing at the concert
 */

@Entity
@Table(name = "CONCERTS")
public class Concert {

    @Id
    @GeneratedValue
    private Long id;
    private String title;

    @Column(name="image_name")
    private String imageName;
    @Column(name = "BLURB", columnDefinition = "LONGTEXT")
    private String blurb;



    @ElementCollection
    @CollectionTable(name="CONCERT_DATES", joinColumns = @JoinColumn(name="CONCERT_ID"))
    @Column(name="DATE")
    private Set<LocalDateTime> dates = new HashSet<>();

    @ManyToMany
    @JoinTable(name="CONCERT_PERFORMER",
    joinColumns = @JoinColumn(name="CONCERT_ID", referencedColumnName = "id"),
    inverseJoinColumns =  @JoinColumn(name="PERFORMER_ID", referencedColumnName = "id"))
    private Set<Performer> performers = new HashSet<>();

    public Concert() {
    }

    public Concert(Long id, String title, String imageName, String blurb, Set<LocalDateTime> dates, Set<Performer> performers){
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
        this.dates = dates;
        this.performers = performers;
    }

    public Concert(Long id, String title, String imageName, String blurb) {
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
    }

    public Concert(String title, String imageName) {
        this.title = title;
        this.imageName = imageName;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBlurb() {
        return blurb;
    }
    public void setBlurb(String blrb) {
        this.blurb = blrb;
    }

    public Set<Performer> getPerformers() {
        return performers;
    }
    public void setPerformers(Set<Performer> performers) {
        this.performers = performers;
    }


    public Set<LocalDateTime> getDates() {
        return dates;
    }
    public void setDates(Set<LocalDateTime> dates) {
        this.dates = dates;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Concert))
            return false;
        if (other == this)
            return true;

        Concert rhs = (Concert) other;
        return new EqualsBuilder().
                append(id, rhs.getId()).
                append(title, rhs.getTitle()).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(getClass().getName()).
                append(id).
                append(title).
                toHashCode();
    }
}
