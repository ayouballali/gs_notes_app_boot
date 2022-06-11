package com.gsnotes.dao;

import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEudiant extends JpaRepository<Etudiant,Long> {

}
