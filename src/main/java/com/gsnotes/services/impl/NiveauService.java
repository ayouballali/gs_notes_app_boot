package com.gsnotes.services.impl;

import com.gsnotes.bo.Niveau;
import com.gsnotes.dao.IniveauDoa;
import com.gsnotes.services.IniveauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class NiveauService implements IniveauService {
    @Autowired
    private IniveauDoa niveau;




    public boolean existsById(Long aLong) {
        return niveau.existsById(aLong);
    }


    public Niveau getById(Long aLong) {
        return niveau.getById(aLong) ;
    }


}
