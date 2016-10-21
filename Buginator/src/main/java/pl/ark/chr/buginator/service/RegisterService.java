package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.util.RegisterData;

import java.util.Locale;

/**
 * Created by Arek on 2016-10-21.
 */
public interface RegisterService {

    void registerUser(RegisterData registerData, Locale locale) throws ValidationException;
}
