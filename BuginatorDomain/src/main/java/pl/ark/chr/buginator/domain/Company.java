package pl.ark.chr.buginator.domain;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Arek on 2016-09-25.
 */
@Entity
@Table(name = "company")
@SequenceGenerator(name = "default_gen", sequenceName = "company_seq", allocationSize = 1)
public class Company extends BaseEntity {

    private static final long serialVersionUID = 2863610043925637330L;

    @Column(name = "unique_key")
    private String uniqueKey;

    @Column(name = "token")
    private String token;

    @Column(name = "user_limit")
    private Integer userLimit;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_option_id", nullable = false)
    private PaymentOption selectedPaymentOption;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer userLimit) {
        this.userLimit = userLimit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
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

    public void setSelectedPaymentOption(PaymentOption selectedPaymentOption) {
        this.selectedPaymentOption = selectedPaymentOption;
    }
}
