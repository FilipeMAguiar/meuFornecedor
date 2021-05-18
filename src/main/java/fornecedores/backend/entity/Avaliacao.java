package fornecedores.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "TB_AVALIACAO")
public class Avaliacao implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idAvaliacao;

    @Column(name = "AVALIADOR_AVALIACAO")
    private Long idAvaliador;

    @Column(name = "ATENDIMENTO_AVALIACAO")
    @Size(max = 10)
    private Long atendimento;

    @Column(name = "PRECO_AVALIACAO")
    @Size(max = 10)
    private Long precos;

    @Column(name = "CONFIABILIDADE_AVALIACAO")
    @Size(max = 10)
    private Long confiabilidade;

    @Column(name = "QUALIDADE_AVALIACAO")
    @Size(max = 10)
    private Long qualidadeProduto;
}
