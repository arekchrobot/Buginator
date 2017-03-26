package pl.ark.chr.buginator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Arek on 2016-09-26.
 */
@Entity
@Table(name = "payment_option")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentOption extends BaseEntity {

    private static final long serialVersionUID = -1453981529749167170L;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "price")
    private Double price;

    @Column(name = "name", length = 75, unique = true)
    private String name;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
