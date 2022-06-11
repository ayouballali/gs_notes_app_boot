package com.gsnotes.services;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.InscriptionModule;

public interface IInscriptionService {
    public InscriptionAnnuelle getInscriptionAnnuelleByEtudiant(Etudiant etudiant);
    public void addInscription(InscriptionAnnuelle inscriptionAnnuelle);
    public void addInscriptionModuleDoa(InscriptionModule inscrip) ;
    public void deleteinsAnul(Long id);
    public void deleteinsMod(Long id);
}
