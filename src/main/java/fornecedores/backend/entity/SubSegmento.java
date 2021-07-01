package fornecedores.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TB_SUB_SEGMENTO")
public class SubSegmento implements Serializable {

    private static final long seriaVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_SUB_SEGMENTO")
    private Long idSubSegmento;

    @Size(max = 50)
    @Column(name = "NOME_SUB_SEGMENTO")
    private String nomeSubSegmento;

    @ManyToOne
    private Segmento segmento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subSegmento")
    @OrderBy("nota desc")
    private List<Fornecedor> fornecedor;
}
