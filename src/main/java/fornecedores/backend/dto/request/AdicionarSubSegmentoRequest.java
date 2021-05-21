package fornecedores.backend.dto.request;

import fornecedores.backend.entity.SubSegmento;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdicionarSubSegmentoRequest {
    private Long idFornecedor;
    private List<SubSegmento> subSegmentoList;
}
