package fornecedores.backend.entity;

import fornecedores.backend.domain.TipoUsuarioEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TB_FORNECEDOR")
public class Fornecedor implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idFornecedor;

    @Size(max = 50)
    @Column(name = "NICK_FORNECEDOR")
    private String nickFornecedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_USUARIO")
    private TipoUsuarioEnum tipoUsuarioEnum;

    @Column(name = "CNPJ_FORNECEDOR")
    @Size(max = 20)
    private String cnpj;

    @Size(max = 100)
    @Column(name = "NOME_FORNECEDOR")
    private String nomeFornecedor;

    @Column(name = "DESCRICAO_FORNECEDOR")
    @Size(max = 1000)
    private String descricaoFornecedor;

    @Size(max = 200)
    @Column(name = "EMAIL_FORNECEDOR")
    private String email;

    @Size(max = 20)
    @Column(name = "SENHA_FORNECEDOR")
    private String senha;

    @Column(name = "PAIS_ENDERECO")
    @Size(max = 30)
    private String pais;

    @Column(name = "UF_FORNECEDOR")
    @Size(max = 100)
    private String estado;

    @Column(name = "CIDADE_FORNECEDOR")
    @Size(max = 100)
    private String cidade;

    @Column(name = "CEP_FORNECEDOR")
    @Size(max = 20)
    private String cep;

    @Column(name = "BAIRRO_FORNECEDOR")
    @Size(max = 50)
    private String bairro;

    @Column(name = "RUA_FORNECEDOR")
    @Size(max = 100)
    private String rua;

    @Column(name = "NUMERO_FORNECEDOR")
    @Size(max = 10)
    private String numero;

    @Column(name = "COMPLEMENTO_FORNECEDOR")
    @Size(max = 100)
    private String complemento;

    @Column(name = "TELEFONE_FORNECEDOR")
    @Size(max = 20)
    private String telefone;

    @Column(name = "INSTAGRAM_FORNECEDOR")
    @Size(max = 50)
    private String instagram;

    @Column(name = "SITE_FORNECEDOR")
    @Size(max = 300)
    private String site;

    @Column(name = "WHATSAPP_FORNECEDOR")
    @Size(max = 100)
    private String whatsapp;

    @Size(max = 200)
    @Column(name = "EMAIL_CONTATO_FORNECEDOR")
    private String emailContato;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "TB_SUB_SEGMENTO")
    private List<SubSegmento> subSegmento;

    @OneToMany
    @JoinTable(name = "TB_AVALIACAO")
    private List<Avaliacao> avaliacao;

}