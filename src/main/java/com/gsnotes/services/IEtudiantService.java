package com.gsnotes.services;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Utilisateur;

public interface IEtudiantService {
    public Etudiant getEtudiantById(Long id);
    public void addEtudiant(com.gsnotes.bo.Etudiant pPerson);
    public void addEtudiants(Iterable <Etudiant> iterable);
    public void deleteEtudiant(Long id);
}
