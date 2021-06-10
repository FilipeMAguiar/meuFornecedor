package fornecedores.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "TB_AVALIACAO")
public class Avaliacao implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_AVALIACAO")
    private Long idAvaliacao;

    @Column(name = "ATENDIMENTO_AVALIACAO")
    private Integer atendimento;

    @Column(name = "PRECO_AVALIACAO")
    private Integer precos;

    @Column(name = "CONFIABILIDADE_AVALIACAO")
    private Integer confiabilidade;

    @Column(name = "QUALIDADE_AVALIACAO")
    private Integer qualidadeProduto;

    @ManyToOne
    private Fornecedor fornecedor;

    @ManyToOne
    private Usuario idUsuario;
}