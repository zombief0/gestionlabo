package com.example.demo.repositories;


import com.example.demo.entities.TypeExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeExamenRepository extends JpaRepository<TypeExamen,Long> {

    TypeExamen findByIdTypeExamen(Long idType);
}
