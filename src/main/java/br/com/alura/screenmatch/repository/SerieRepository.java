package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.Model.Categoria;
import br.com.alura.screenmatch.Model.Episodio;
import br.com.alura.screenmatch.Model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long>{
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThan(String nomeAtor, double notaMinima);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    @Query("select s from Serie s where s.totalTemporadas < :totalTemporadas and s.avaliacao > :avaliacao")
    List<Serie> seriePorTemporadaAvaliacao(int totalTemporadas, double avaliacao);


    @Query("select e from Episodio e where e.titulo ilike %:nomeEpisodio%")
    List<Episodio> episodiosPorTrecho(String nomeEpisodio);


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC limit 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);


@Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :ano")
    List<Episodio> episodioPorSerieAno(Serie serie, int ano);
}
