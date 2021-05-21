package fornecedores.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriaSubSegmentoRequest {

    private String nomeSubSegmento;
    private String idSegmento;
    private String idFornecedor;
}
