package fornecedores.backend.dto;

import fornecedores.backend.domain.TipoUsuarioEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    private Long idUsuario;
    private TipoUsuarioEnum tipoUsuarioEnum;
    private String nickUsuario;
    private String nomeUsuario;
}
