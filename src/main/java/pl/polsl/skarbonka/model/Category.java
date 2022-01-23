package pl.polsl.skarbonka.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CATEGORIES")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Long id;
    @Column(name = "cat_name")
    private String name;

    @OneToMany(mappedBy = "category", targetEntity = Fundraising.class)
    private List<Fundraising> listOfFundraisings;

    public Category() {
    }

    public Category(String text) {
        this.name = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String text) {
        this.name = text;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
