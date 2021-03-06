package pl.ark.chr.buginator.domain.auth;

import pl.ark.chr.buginator.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Representation of possible payment options for access to the portal
 */
@Entity
@Table(name = "buginator_payment_option")
public class PaymentOption extends BaseEntity<PaymentOption> {

    private static final long serialVersionUID = -1453981529749167170L;

    public static final long TRIAL = 1L;

    /**
     * Represents how long in days the payment is valid
     */
    @Column(name = "duration")
    private int duration;

    @Column(name = "max_users")
    private int maxUsers;

    @Column(name = "price")
    private double price;

    @Column(name = "name", length = 75, unique = true)
    private String name;

    public int getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PaymentOption getPaymentOption(long id) {
        var paymentOption = new PaymentOption();
        paymentOption.setId(PaymentOption.TRIAL);
        return paymentOption;
    }
}
