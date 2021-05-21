package fornecedores.backend.entity;

import fornecedores.backend.domain.TipoUsuarioEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TB_USUARIO")
public class Usuario implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @Column(name = "ID_USUARIO")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_USUARIO")
    private TipoUsuarioEnum tipoUsuarioEnum;

    @Size(max = 50)
    @Column(name = "NICK_USUARIO", unique = true)
    private String nickUsuario;

    @Size(max = 50)
    @Column(name = "NOME_USUARIO")
    private String nomeUsuario;

    @Size(max = 12)
    @Column(name = "TELEFONE_USUARIO")
    private String telefone;

    @Size(max = 200)
    @Column(name = "EMAIL_USUARIO")
    private String email;

    @Size(max = 20)
    @Column(name = "SENHA_USUARIO")
    private String senha;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<Avaliacao> avaliacoes;
}
