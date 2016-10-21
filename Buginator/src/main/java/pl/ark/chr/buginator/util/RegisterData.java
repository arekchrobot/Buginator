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
}
