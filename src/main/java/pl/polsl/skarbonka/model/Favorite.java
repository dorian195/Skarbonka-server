package pl.polsl.skarbonka.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "FAVORITES")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fav_id")
    private Long id;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fav_usr_id")
    private User user;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fav_fun_id")
    private Fundraising fundraising;

    public Favorite() {

    }

    public Favorite(User user, Fundraising fundraising) {
        this.user = user;
        this.fundraising = fundraising;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Fundraising getFundraising() {
        return fundraising;
    }

    public void setFundraising(Fundraising fundraising) {
        this.fundraising = fundraising;
    }
}
