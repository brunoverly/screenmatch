package br.com.alura.screenmatch.Service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class Gemini {


    public static String traduzirTexto(String texto) {
        Dotenv dotenv = Dotenv.load();
        Client client = Client.builder()
                .apiKey(dotenv.get("GEMINI_API_KEY"))
                .build();

            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash-lite",
                    """
                    Para o proximo prompt, responda apenas com o texto traduzido, sem nenhum outro texto, sem nenhuma informação a mais, somente o texto pleno traduzido.
                    Irei repassar um texto em inglês, e quero que você traduza para o português. Leve em consideração que se trata da sinopse de um filme ou série. O texto é o seguinte:
                    """+ texto,
                    null
            );
            return response.text();

        }
    }

