package fornecedores.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarSenhaRequest {
    private String email;
    private String novaSenha;
}
