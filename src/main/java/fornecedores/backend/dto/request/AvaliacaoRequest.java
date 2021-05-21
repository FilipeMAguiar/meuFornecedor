package fornecedores.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvaliacaoRequest {
    private String idFornecedor;

    private String idUsuario;

    private String atendimento;

    private String precos;

    private String confiabilidade;

    private String qualidadeProduto;
}
