package com.stellablima.tenis150720.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.stellablima.tenis150720.model.Clube;
import com.stellablima.tenis150720.model.Torneio;

@Repository
public interface TorneioRepository extends CrudRepository<Torneio, String>{
    Torneio findById(long id);
    Iterable<Torneio> findByClube(Clube clube);

}
