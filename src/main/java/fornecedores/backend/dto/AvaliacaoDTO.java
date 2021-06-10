package fornecedores.backend.dto;

import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.entity.Usuario;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AvaliacaoDTO {

    private Long idAvaliacao;
    private Integer atendimento;
    private Integer precos;
    private Integer confiabilidade;
    private Integer qualidadeProduto;
    private String nickFornecedor;
    private Long idUsuario;
}
