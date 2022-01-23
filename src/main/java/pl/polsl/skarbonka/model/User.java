package pl.polsl.skarbonka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
public class User {
    public enum Role {
        USER("ROLE_USER"),
        ADMIN("ROLE_ADMIN");

        Role(String name) {
            this.name = name;
        }

        private final String name;

        public String getName() {
            return name;
        }

        public static Role fromName(String name) {
            if (name  == null || name.isEmpty()) {
                return null;
            }
            return Stream.of(User.Role.values())
                    .filter(c -> c.getName().equals(name))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    private Long id;

    @Column(name = "usr_role")
    @JsonIgnore
    private Role role;

    @Column(name = "usr_first_name")
    private String firstName;

    @Column(name = "usr_last_name")
    private String lastName;

    @Column(name = "usr_gender")
    private String gender;

    @Column(name = "usr_birth_date")
    private Date birthDate;

    @Column(name = "usr_password")
    @JsonIgnore
    private String password;

    @Column(name = "usr_email")
    private String email;

    @Column(name = "usr_modification_date")
    private Date modificationDate;

    @Column(name = "usr_delete_date")
    @JsonIgnore
    private Date deleteDate;

    @OneToMany(mappedBy = "user", targetEntity = Donation.class)
    private List<Donation> listOfDonations;

    @OneToMany(mappedBy = "user", targetEntity = Fundraising.class)
    private List<Fundraising> listOfFundraisings;

    @OneToMany(mappedBy = "user", targetEntity = Comment.class)
    private List<Comment> listOfComments;

    public User() {
        this.id = 0L;
        this.modificationDate = null;
        this.deleteDate = null;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
}
