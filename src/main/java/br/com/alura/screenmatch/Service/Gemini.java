package br.com.alura.screenmatch.Service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
public class Gemini {

    public static String traduzirTexto(String texto) {
        Client client = Client.builder()
                .apiKey(System.getenv("GEMINI_API_KEY"))
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
