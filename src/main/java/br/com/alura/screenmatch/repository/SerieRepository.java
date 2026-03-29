package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.Model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long>{
}
