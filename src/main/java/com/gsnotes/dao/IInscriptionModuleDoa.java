package com.gsnotes.dao;

import com.gsnotes.bo.InscriptionModule;
import com.gsnotes.bo.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInscriptionModuleDoa extends JpaRepository<InscriptionModule,Long> {
}
