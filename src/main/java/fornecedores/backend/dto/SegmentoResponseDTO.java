package fornecedores.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter

public class SegmentoResponseDTO {
    private String idSegmento;
    private String nomeSegmento;
    private List<SubSegmentoDTO> subSegmentos;
}
