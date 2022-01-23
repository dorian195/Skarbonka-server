package pl.polsl.skarbonka.query;

import pl.polsl.skarbonka.model.User;

import java.math.BigDecimal;

public class DonatorsQueryResult {
    private final User user;
    private final BigDecimal amount;

    public DonatorsQueryResult(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
