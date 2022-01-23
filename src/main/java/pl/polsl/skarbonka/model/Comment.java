package pl.polsl.skarbonka.model;

import javax.persistence.*;

@Entity
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id")
    private Long id;

    @Column(name = "com_text")
    private String text;

    @ManyToOne()
    @JoinColumn(name = "com_fun_id")
    private Fundraising fundraising;

    @ManyToOne()
    @JoinColumn(name = "com_usr_id")
    private User user;

    public Comment() {
    }

    public Comment(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fundraising getFundraising() {
        return fundraising;
    }

    public void setFundraising(Fundraising fundraising) {
        this.fundraising = fundraising;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
