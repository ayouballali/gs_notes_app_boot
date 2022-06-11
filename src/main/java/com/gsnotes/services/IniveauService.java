package com.gsnotes.services;

import com.gsnotes.bo.Niveau;

public interface IniveauService {
    public boolean existsById(Long aLong);
    public Niveau getById(Long aLong);
}
