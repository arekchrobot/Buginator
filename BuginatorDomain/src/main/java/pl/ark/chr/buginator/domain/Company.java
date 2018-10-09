package pl.ark.chr.buginator.domain;

import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents the company that has many users and applications.
 * This class also checks if user assigned to this company can log into portal.
 */
@Entity
@Table(name = "buginator_company",
        indexes = {
                @Index(name = "token_key_index", columnList = "token,unique_key"),
                @Index(name = "name_index", columnList = "name")
        })
public class Company extends BaseEntity<Company> {

    private static final long serialVersionUID = 2863610043925637330L;
    private static final int TOKEN_LENGTH = 15;

    @Column(name = "unique_key", unique = true, nullable = false)
    private String uniqueKey;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_limit")
    private Integer userLimit;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "name", length = 100, unique = true)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_option_id", nullable = false)
    private PaymentOption selectedPaymentOption;

    protected Company() {
    }

    public Company(String name, PaymentOption selectedPaymentOption) {
        Objects.requireNonNull(name);

        updateSelectedPaymentOptions(selectedPaymentOption);
        this.uniqueKey = generateToken();
        this.token = generateToken();
        this.name = name;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    protected void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getToken() {
        return token;
    }

    protected void setToken(String token) {
        this.token = token;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    protected void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    protected void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PaymentOption getSelectedPaymentOption() {
        return selectedPaymentOption;
    }

    protected void setSelectedPaymentOption(PaymentOption selectedPaymentOption) {
        this.selectedPaymentOption = selectedPaymentOption;
    }

    public void updateSelectedPaymentOptions(PaymentOption selectedPaymentOption) {
        Objects.requireNonNull(selectedPaymentOption);

        this.selectedPaymentOption = selectedPaymentOption;
        this.userLimit = selectedPaymentOption.getMaxUsers();
        this.expiryDate = extendExpiryDate(selectedPaymentOption.getDuration());
    }

    private LocalDate extendExpiryDate(Integer duration) {
        if (this.expiryDate == null) {
            this.expiryDate = LocalDate.now();
        }
        return this.expiryDate.plusDays(duration);
    }

    private String generateToken() {
        return RandomStringUtils.random(TOKEN_LENGTH, true, true);
    }
}
