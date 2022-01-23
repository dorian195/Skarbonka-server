package pl.polsl.skarbonka.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class FundraisingEditRequest {
    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    @Positive
    private Integer moneyGoal;

    @NotEmpty
    private Long categoryId;

    public FundraisingEditRequest() {}

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
}

