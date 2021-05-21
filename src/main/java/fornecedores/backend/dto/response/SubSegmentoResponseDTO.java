package fornecedores.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SubSegmentoResponseDTO {

    private String idSegmento;
    private Long idSubSegmento;
    private String nomeSubSegmento;
    private String nomeSegmento;
    private List<FornecedorDTO> fornecedores;
}
