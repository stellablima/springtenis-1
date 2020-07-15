package com.stellablima.tenis150720.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stellablima.tenis150720.model.Clube;



@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long> {

	List<Clube> findByNomeClube(String nomeClube);
	Clube findById(long id);
	//void save(Optional<Clube> clubeUpdate);

}
