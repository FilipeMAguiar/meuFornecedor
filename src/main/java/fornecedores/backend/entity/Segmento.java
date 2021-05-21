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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "segmento")
    private List<SubSegmento> subSegmentos;

}
