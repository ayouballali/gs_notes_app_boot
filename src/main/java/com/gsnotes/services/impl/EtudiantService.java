package com.gsnotes.services.impl;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.dao.IEudiant;

import com.gsnotes.services.IEtudiantService;
import com.mysql.cj.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EtudiantService implements IEtudiantService {

    @Autowired
    private IEudiant etudiantDoa;

//    public List<com.gsnotes.bo.Etudiant> getAllEtudiants() {
//
//        return etudiantDoa.findAll();
//    }
//
//    public void deleteEtudiant(Long id) {
//        etudiantDoa.deleteById(id);
//
//    }

    public void addEtudiants(Iterable <Etudiant> iterable){
        etudiantDoa.saveAll(iterable);
    }

    public Etudiant getEtudiantById(Long id) {

        if (etudiantDoa.existsById(id)){
            System.out.println("yes found etudiant  id = "+ id);
            return etudiantDoa.getById(id);}
        System.out.println("not found etudint id =" + id);
        return null;

    }

    public void addEtudiant(com.gsnotes.bo.Etudiant pPerson) {

        etudiantDoa.save(pPerson);

    }

    public void deleteEtudiant(Long id){
        etudiantDoa.deleteById(id);
    }
//
//    public void updatePerson(com.gsnotes.bo.Etudiant pPerson) {
//        etudiantDoa.save(pPerson);

//    }
}
