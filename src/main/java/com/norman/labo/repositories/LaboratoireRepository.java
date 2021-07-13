package com.norman.labo.repositories;


import com.norman.labo.entities.Laboratoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoireRepository extends JpaRepository<Laboratoire,Long> {

}
