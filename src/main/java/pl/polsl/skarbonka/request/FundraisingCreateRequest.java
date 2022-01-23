package pl.polsl.skarbonka.request;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.Date;

public class FundraisingCreateRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    @Positive
    private Integer moneyGoal;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private Date endDate;

    @NotEmpty
    private Long categoryId;

    public FundraisingCreateRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMoneyGoal() {
        return moneyGoal;
    }

    public void setMoneyGoal(Integer moneyGoal) {
        this.moneyGoal = moneyGoal;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
