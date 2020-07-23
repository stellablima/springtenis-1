package com.stellablima.tenis150720.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stellablima.tenis150720.model.Organizador;

@Repository
public interface OrganizadorFormRepository extends JpaRepository<Organizador, String> {
	//Optional<Organizador> findById(long id);
	//Optional<Organizador> findByEmail(String email);
}
