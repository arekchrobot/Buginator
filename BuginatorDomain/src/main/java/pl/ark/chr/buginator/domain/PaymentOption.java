package pl.ark.chr.buginator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Arek on 2016-09-26.
 */
@Entity
@Table(name = "payment_option")
@SequenceGenerator(name = "default_gen", sequenceName = "payment_option_seq", allocationSize = 1)
public class PaymentOption extends BaseEntity {

    private static final long serialVersionUID = -1453981529749167170L;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "price")
    private Double price;

    @Column(name = "name", length = 75)
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
