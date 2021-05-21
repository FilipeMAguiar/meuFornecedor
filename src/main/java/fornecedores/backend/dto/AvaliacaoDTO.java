package fornecedores.backend.dto;

import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.entity.Usuario;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AvaliacaoDTO {

    private Long idAvaliacao;
    private Long atendimento;
    private Long precos;
    private Long confiabilidade;
    private Long qualidadeProduto;
    private String nickFornecedor;
    private Long idUsuario;
}
