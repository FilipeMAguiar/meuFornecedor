package fornecedores.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorDTO {
    private String idFornecedor;
    private String nickFornecedor;
    private String nomeFornecedor;
    private String descricao;
    private String cnpj;
    private String emailContato;
    private String cidade;
    private String instagram;
    private String numero;
    private String site;
    private Integer nota;
}
