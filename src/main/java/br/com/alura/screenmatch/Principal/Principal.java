package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.Model.DadosEpisodio;
import br.com.alura.screenmatch.Model.DadosSerie;
import br.com.alura.screenmatch.Model.DadosTemporada;
import br.com.alura.screenmatch.Service.ConsumoAPI;
import br.com.alura.screenmatch.Service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3e9710df";



    public void exibirMenu(){


        System.out.printf("Digite o nome da serie: ");
        var nomeSerie = sc.nextLine();

        var json = consumoApi.obterDados(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);


        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);



        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i = 1; i <= dados.totalTemporadas(); i++){
            json = consumoApi.obterDados(
                    ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY);;
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);

        }

        temporadas.forEach(System.out::println);

//        for(int i = 0; i < dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for(int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));



    }
}
