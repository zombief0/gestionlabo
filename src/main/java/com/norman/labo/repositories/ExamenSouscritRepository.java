package com.norman.labo.repositories;


import com.norman.labo.entities.ExamenSouscrit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenSouscritRepository extends JpaRepository<ExamenSouscrit,Long> {


    List<ExamenSouscrit> findAllByFacture_IdFacture(Long idFacture);

}
