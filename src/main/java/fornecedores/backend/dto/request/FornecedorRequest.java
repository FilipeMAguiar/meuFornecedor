package fornecedores.backend.dto.request;

import fornecedores.backend.dto.ContatoDTO;
import fornecedores.backend.dto.EnderecoDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorRequest {
    private String nickname;
    private String cnpj;
    private String nome;
    private String descricao;
    private String email;
    private String senha;
    private EnderecoDTO endereco;
    private ContatoDTO contato;
    private Long idSubSegmento;
}
