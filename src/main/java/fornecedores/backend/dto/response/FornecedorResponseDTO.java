package fornecedores.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fornecedores.backend.dto.AvaliacaoDTO;
import fornecedores.backend.entity.Avaliacao;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FornecedorResponseDTO {
    private String idFornecedor;
    private String nickFornecedor;
    private String nomeFornecedor;
    private String emailContato;
    private String cidade;
    private String numero;
    private String instagram;
    private String site;
    private List<AvaliacaoDTO> avaliacoes;
    private String nota;
}
