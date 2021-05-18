package fornecedores.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UsuarioRequest {
    @NotEmpty(message = "O campo 'nomeUsuario' deverá ser informado.")
    public String nomeUsuario;

    @NotEmpty(message = "O campo 'nickname' deverá ser informado.")
    public String nickname;

    @NotEmpty(message = "O campo 'telefone' deverá ser informado")
    public String telefone;

    @NotEmpty(message = "O campo 'email' deverá ser informado.")
    public String email;

    @NotEmpty(message = "O campo 'senha' deverá ser preenchido.")
    public String senha;
}
