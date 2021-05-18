package fornecedores.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoRequest {

    private Long idUsuario;

    private Long atendimento;

    private Long precos;

    private Long confiabilidade;

    private Long qualidadeProduto;
}
