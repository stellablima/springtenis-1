package com.stellablima.tenis150720.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stellablima.tenis150720.model.Organizador;

@Repository
public interface OrganizadorRepository extends JpaRepository<Organizador, String>{
	Organizador findById(long id);
	Organizador findByEmail(String email);
}
