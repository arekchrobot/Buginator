package pl.ark.chr.buginator.util;

import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.User;

/**
 * Created by Arek on 2016-10-21.
 */
public class RegisterData {

    private Company company;

    private User user;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Register data: ")
                .append("company: ")
                .append(company.getName())
                .append(", ")
                .append(company.getAddress())
                .append("user: ")
                .append(user.getEmail())
                .append(", ")
                .append(user.getName());
        return sb.toString();
    }
}
