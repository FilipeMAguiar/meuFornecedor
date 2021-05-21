package fornecedores.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FornecedorResponseDTO {
    private String idFornecedor;
    private String nickFornecedor;
    private String nomeFornecedor;
    private String emailContato;
    private String pais;
}
