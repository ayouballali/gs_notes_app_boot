package com.gsnotes.services.impl;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionModule;
import com.gsnotes.dao.IInscriptionAnnuelleDoa;

import com.gsnotes.dao.IInscriptionModuleDoa;
import com.gsnotes.dao.IUtilisateurDao;
import com.gsnotes.services.IInscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class InscriptionService implements IInscriptionService {

    @Autowired
    private IInscriptionModuleDoa inscriptionModuleDoa;


    @Autowired
    private IInscriptionAnnuelleDoa inscriptionAnnuellDoa;

//    @Override
//    public List<InscriptionAnnuelle> findAll() {
//        return null;
//    }
//
//    @Override
//    public List<InscriptionAnnuelle> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<InscriptionAnnuelle> findAll(Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public List<InscriptionAnnuelle> findAllById(Iterable<Long> iterable) {
//        return null;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//
//    }
//
//    @Override
//    public void delete(InscriptionAnnuelle inscriptionAnnuelle) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> iterable) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends InscriptionAnnuelle> iterable) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> S save(S s) {
//        return null;
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> List<S> saveAll(Iterable<S> iterable) {
//        return null;
//    }
//
//    @Override
//    public Optional<InscriptionAnnuelle> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> S saveAndFlush(S s) {
//        return null;
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> List<S> saveAllAndFlush(Iterable<S> iterable) {
//        return null;
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<InscriptionAnnuelle> iterable) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> iterable) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public InscriptionAnnuelle getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public InscriptionAnnuelle getById(Long aLong) {
//        return getById(aLong);
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends InscriptionAnnuelle> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override

    public void deleteinsAnul(Long id){
        inscriptionAnnuellDoa.deleteById(id);
    }

    public void deleteinsMod(Long id){
        inscriptionModuleDoa.deleteById(id);
    }

    public void addInscriptionModuleDoa(InscriptionModule inscrip) {
        inscriptionModuleDoa.save(inscrip);
    }

    public void addInscription(InscriptionAnnuelle inscriptionAnnuelle) {
    inscriptionAnnuellDoa.save(inscriptionAnnuelle);
}

    public InscriptionAnnuelle getInscriptionAnnuelleByEtudiant(Etudiant etudiant) {
        return inscriptionAnnuellDoa.getInscriptionAnnuelleByEtudiant(etudiant);
    }
}
