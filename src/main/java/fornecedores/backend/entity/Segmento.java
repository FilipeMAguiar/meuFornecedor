package fornecedores.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "TB_SEGMENTO")
public class Segmento implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @Column(name = "ID_SEGMENTO")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idSegmento;

    @Size(max = 50)
    @Column(name = "NOME_SEGMENTO")
    private String nomeSegmento;

    @Column(name = "ID_SUB_SEGMENTO")
    private Long idSubSegmento;

}
