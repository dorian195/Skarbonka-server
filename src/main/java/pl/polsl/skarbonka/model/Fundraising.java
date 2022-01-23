package pl.polsl.skarbonka.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FUNDRAISINGS")
public class Fundraising {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fun_id", insertable = false, updatable = false)
    private Long id;
    @Column(name = "fun_name")
    private String name;
    @Column(name = "fun_created_date")
    private Date createdDate;
    @Column(name = "fun_end_date")
    private Date endDate;
    @Column(name = "fun_description")
    private String description;
    @Column(name = "fun_account_balance")
    private BigDecimal accountBalance;
    @Column(name = "fun_modification_date")
    private Date modificationDate;
    @Column(name = "fun_delete_date")
    private Date deleteDate;
   // @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "fun_usr_id")
    private User user;
   // @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "fun_cat_id")
    private Category category;

    @Column(name = "fun_reports_amount")
    private Integer reportsAmount;

    @Column(name = "fun_money_goal")
    private BigDecimal moneyGoal;

    @OneToMany(mappedBy = "fundraising", targetEntity = Comment.class)
    private List<Comment> listOfComments;

    public Fundraising() {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
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

    public Integer getReportsAmount() {
        return reportsAmount;
    }

    public void setReportsAmount(Integer reportsAmount) {
        this.reportsAmount = reportsAmount;
    }

    public void addReport() {
        this.reportsAmount++;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getMoneyGoal() {
        return moneyGoal;
    }

    public void setMoneyGoal(BigDecimal moneyGoal) {
        this.moneyGoal = moneyGoal;
    }
}
