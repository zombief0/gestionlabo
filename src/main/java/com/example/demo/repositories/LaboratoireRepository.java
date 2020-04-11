package com.example.demo.repositories;


import com.example.demo.entities.Laboratoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoireRepository extends JpaRepository<Laboratoire,Long> {

    Laboratoire findByIdLaboratoire(Long idType);
}
