package school.sptech.Chat.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class IaService {

    public String responder(String pergunta) {

        String contexto = """
                Cadastro de Produto:
                1 - Acesse Produtos
                2 - Clique em Novo Produto
                3 - Preencha nome, preço e quantidade
                4 - Salve
                
                Consulta de Estoque:
                - Acesse Produtos
                - Busque o produto
                - Veja a quantidade disponível
                """;

        String prompt = """
                Você é um assistente de sistema.
                
                Responda SOMENTE em JSON válido no formato:
                
                {
                  "titulo": "string",
                  "response": ["string"]
                }
                
                Regras:
                - NÃO crie objetos dentro do array
                - NÃO use passo1, step_1 ou similares
                - Cada item deve ser uma STRING simples
                - NÃO numere os passos
                - Não escreva nada fora do JSON
                
                Contexto:
                %s
                
                Pergunta: %s
                """.formatted(contexto, pergunta);

        return chamarOllama(prompt);
    }

    private String chamarOllama(String prompt) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", "phi3");
            payload.put("prompt", prompt);
            payload.put("stream", false);
            payload.put("format", "json");

            payload.put("options", Map.of("temperature", 2));

            String json = mapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode node = mapper.readTree(response.body());
            String resposta = node.get("response").asText();

            return resposta;

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao chamar IA: " + e.getMessage();
        }
    }
}