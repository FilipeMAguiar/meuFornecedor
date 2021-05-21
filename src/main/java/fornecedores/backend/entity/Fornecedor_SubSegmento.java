package fornecedores.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "FORNECEDOR_SUBSEGMENTO")
public class Fornecedor_SubSegmento {

    @Id
    @Column(name = "ID_FORNECEDOR")
    private Long idFornecedor;

    @Column(name = "ID_SUB_SEGMENTO")
    private Long idSubSegmento;
}
