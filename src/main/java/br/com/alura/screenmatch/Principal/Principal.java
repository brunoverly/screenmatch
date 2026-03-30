package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.Model.*;
import br.com.alura.screenmatch.Service.ConsumoAPI;
import br.com.alura.screenmatch.Service.ConverteDados;
import br.com.alura.screenmatch.Service.Gemini;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    @Value("${OMDB_API_KEY}")
    private String API_KEY;
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    @Autowired
    private Gemini gemini;
    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBuscada = Optional.empty();


    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {
        System.out.println("===============================================================");
        System.out.println("""
                1 - Buscar séries
                2 - Buscar episódios
                3-  Listar séries buscadas
                4-  Buscar séries por título
                5-  Buscar séries por ator
                6-  Top 5 séries por avaliação
                7-  Top séries por categoria
                8-  Buscar séries por temporada e avaliação
                9-  Buscar episodios por trecho
                10- Buscar top episódios por série
                11- Buscar episódios por data
                0 - Sair
                """);
        System.out.println("===============================================================");

        var opcao = sc.nextInt();
        sc.nextLine();

        switch (opcao) {
            case 1:
                buscarSerieWeb();
                break;
            case 2:
                buscarEpisodioPorSerie();
                break;
            case 3:
                listarSeriesBuscadas();
                break;
            case 4:
                buscarSeriePorTitulo();
                break;
            case 5:
                buscarSeriePorAtor();
                break;
            case 6:
                buscarTopSeriesAvaliadas();
                break;
            case 7:
                buscarSeriesPorCategoria();
                break;
            case 8:
                buscarSeriePorTemporadaAvaliacao();
                break;
            case 9:
                buscarEpisodioPorTrecho();
                break;
            case 10:
                buscarTopEpisodiosAvaliados();
                break;
            case 11:
                buscarEpisodiosPorData();
                break;

            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida");
        }
    }

    private void buscarEpisodiosPorData() {
        buscarSeriePorTitulo();
        if(serieBuscada.isPresent()) {
            System.out.println("Digite a partir de qual ano deseja filtrar: ");
            var ano = sc.nextInt();
            sc.nextLine();

            List<Episodio> episodiosFiltrados = repositorio.episodioPorSerieAno(serieBuscada.get(), ano);
            episodiosFiltrados.forEach(System.out::println);
        }

        exibeMenu();
    }

    private void buscarTopEpisodiosAvaliados() {
        buscarSeriePorTitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e-> System.out.printf("""
                    \n
                    Série: %s
                    Temporada: %s
                    Título: %s
                    Avaliação: %.2f
                    Duração: %s
                    \n """, e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getAvaliacao(), e.getDataLancamento()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite nome do episódio para busca:");
        var nomeEpisodio = sc.nextLine();
        List<Episodio> episodiosFiltrados = repositorio.episodiosPorTrecho(nomeEpisodio);
        System.out.println("Episódios com o título "+ nomeEpisodio.trim().toLowerCase() +" : ");
        episodiosFiltrados.forEach(e-> System.out.printf("""
                \n
                Série: %s
                Temporada: %s
                Título: %s
                Avaliação: %.2f
                Duração: %s
                \n """, e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getAvaliacao(), e.getDataLancamento()));
    }

    private void buscarSeriePorTemporadaAvaliacao() {
        System.out.println("Digite o número mínimo de temporadas:");
        int minTemporadas = sc.nextInt();
        sc.nextLine();
        System.out.println("Digite a avaliação mínima:");
        double minAvaliacao = sc.nextDouble();
        sc.nextLine();

        List<Serie> seriesFiltradas = repositorio.seriePorTemporadaAvaliacao(minTemporadas, minAvaliacao);
        seriesFiltradas.forEach(s-> System.out.printf("""
                \n
                Título: %s
                Total de Temporadas: %s
                Avaliação: %.2f
                Gênero: %s
                \n """, s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero())
        );
    }

    private void buscarSeriesPorCategoria() {
        System.out.printf("Digite a categoria para busca: ");
        var categoria = sc.nextLine();
        Categoria categoriaEnum = Categoria.fromPortugues(categoria);
        List<Serie> SeriesPorCategoria = repositorio.findByGenero(categoriaEnum);
        System.out.println("Séries do gênero " + categoriaEnum + ": ");
        SeriesPorCategoria.forEach(s-> System.out.printf("""
                \n
                Título: %s
                Total de Temporadas: %s
                Avaliação: %.2f
                Gênero: %s
                \n """, s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero())
        );
    }

    private void buscarTopSeriesAvaliadas() {
        List<Serie> topSeriesAvaliadas = repositorio.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("Top 5 séries mais bem avaliadas: ");
        int ranking = 1;
        topSeriesAvaliadas.forEach(s-> System.out.printf("""
                \n
                Título: %s
                Total de Temporadas: %s
                Avaliação: %.2f
                Gênero: %s
                \n """, s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero())
        );
    }


    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator:");
        var nomeAtor = sc.nextLine();
        List<Serie> serieEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThan(nomeAtor,8.8);
        System.out.println("Séries em que o ator(a) "+ nomeAtor +" participou: ");
        serieEncontradas.forEach(s-> System.out.printf("""
                \n
                Título: %s
                Total de Temporadas: %s
                Avaliação: %.2f
                Gênero: %s
                \n """, s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero()));
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o titulo da série a ser buscada:");
        var titulo = sc.nextLine();

        serieBuscada = repositorio.findByTituloContainingIgnoreCase(titulo);
        if(serieBuscada.isEmpty()){
            System.out.println("Série não encontrada. Verifique a grafia e tente novamente.");
        }else{
            System.out.println("Dados da série: "+ serieBuscada.get());
        }



    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        serie.setSinopse(gemini.traduzirTexto(serie.getSinopse()));
        repositorio.save(serie);
        exibeMenu();
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = sc.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        System.out.println("Escolha uma série pelo nome: ");
        listarSeriesBuscadas();
        var nomeSerie = sc.nextLine();

        Optional <Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isEmpty()){
            System.out.println("Série não encontrada. Verifique a grafia e tente novamente.");
        }
        else{
            var serieEscolhida = serieBuscada.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEscolhida.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO + serieEscolhida.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream().flatMap(d -> d.episodios().stream()
                    .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEscolhida.setEpisodios(episodios);
            repositorio.save(serieEscolhida);
        }
    }

    private void listarSeriesBuscadas(){
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getTitulo))
                .forEach(System.out::println);

    }
}
