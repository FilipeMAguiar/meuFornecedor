package fornecedores.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubSegmentoWrapperResponseDTO {
    private List<SubSegmentoResponseDTO> subSegmentos;
}
