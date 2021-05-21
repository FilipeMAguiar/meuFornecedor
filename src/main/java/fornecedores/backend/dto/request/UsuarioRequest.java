package fornecedores.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UsuarioRequest {
    public String nomeUsuario;
    public String nickname;
    public String telefone;
    public String email;
    public String senha;
}
