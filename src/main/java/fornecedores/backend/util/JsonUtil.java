package fornecedores.backend.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {

    private JsonUtil() {
    }

    public static String objectToJson(Object o) throws RuntimeException {
        try {
            ObjectMapper mapper = createObjectMapper();
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Não foi possível processar o arquivo. Erro ao gerar JSON.");
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) throws RuntimeException {
        try {
            ObjectMapper mapper = createObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Não foi possível processar o arquivo. Erro ao gerar o objeto.");
        }
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}