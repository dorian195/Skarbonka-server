package pl.polsl.skarbonka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "DONATIONS")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "don_id", insertable = false, updatable = false)
    private Long id;
    @Column(name = "don_name")
    private String name;
    @Column(name = "don_anonymous")
    private boolean anonymous;

    @ManyToOne
    @JoinColumn(name = "don_usr_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "don_fun_id")
    private Fundraising fundraising;

    @Column(name = "don_ammount")
    private BigDecimal ammount;
    @Column(name = "don_created_date")
    private Date createdDate;

    public Donation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
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

    public BigDecimal getAmmount() {
        return ammount;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
