package com.stellablima.tenis150720.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stellablima.tenis150720.model.Atleta;
import com.stellablima.tenis150720.model.Clube;


@Repository
public interface AtletaRepository extends JpaRepository<Atleta,Long>{
	
	List<Atleta> findByNome(String nome);
	Atleta findById(long id);
	Iterable<Atleta> findByClube(Clube clube);
}
