package com.therearenotasksforus.videohostingapi.security.jwt.services.implementation;

import com.therearenotasksforus.videohostingapi.security.jwt.services.InvalidJwtsService;
import com.therearenotasksforus.videohostingapi.security.models.InvalidJwts;
import com.therearenotasksforus.videohostingapi.security.repositories.InvalidJwtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvalidJwtsServiceImplementation implements InvalidJwtsService {

    private final InvalidJwtRepository invalidJwtRepository;

    @Autowired
    public InvalidJwtsServiceImplementation(InvalidJwtRepository invalidJwtRepository) {
        this.invalidJwtRepository = invalidJwtRepository;
    }

    @Override
    public String findById(Long id) {
        InvalidJwts token = invalidJwtRepository.findById(id).orElse(null);

        return token != null ? token.getToken() : null;
    }

    @Override
    public List<String> getAll() {
        List<InvalidJwts> tokens = invalidJwtRepository.findAll();

        List<String> invalidTokens = new ArrayList<>();

        for(InvalidJwts token : tokens) {
            invalidTokens.add(token.getToken());
        }

        return invalidTokens;
    }

    @Override
    public void addNew(String token) {
        InvalidJwts invalidJwt = new InvalidJwts();
        invalidJwt.setToken(token);
        invalidJwtRepository.save(invalidJwt);
    }

    @Override
    public boolean findByToken(String token) {
        return invalidJwtRepository.findByToken(token) != null;
    }

    @Override
    public void delete(Long id) {
        invalidJwtRepository.deleteById(id);
    }
}
