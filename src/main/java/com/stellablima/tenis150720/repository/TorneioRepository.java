package com.stellablima.tenis150720.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.stellablima.tenis150720.model.Clube;
import com.stellablima.tenis150720.model.Torneio;

@Repository
public interface TorneioRepository extends CrudRepository<Torneio, String>{
    Torneio findById(long id);
    List<Torneio> findByClube(Clube clube);
}
