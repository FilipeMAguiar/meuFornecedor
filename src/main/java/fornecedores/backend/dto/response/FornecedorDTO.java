package fornecedores.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fornecedores.backend.entity.Avaliacao;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FornecedorDTO {
    private String idFornecedor;
    private String nickFornecedor;
    private String nomeFornecedor;
    private String emailContato;
    private String pais;
}
