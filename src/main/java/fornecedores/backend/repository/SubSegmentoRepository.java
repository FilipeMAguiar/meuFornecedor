package fornecedores.backend.repository;

import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.entity.Segmento;
import fornecedores.backend.entity.SubSegmento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubSegmentoRepository extends JpaRepository<SubSegmento, Long> {
    //@Query(value = "insert into SubSegmento values (:idSubSegmento, :nomeSubSegmento, :idFornecedor, :idSubSegmento)")
    //void adicionarFornecedorAoSubSegmento(@Param("idSubSegmento") Long idSubsegmento, @Param("nomeSubSegmento") String nomeSubSegmento, @Param("idFornecedor") Long idFornecedor, @Param("idSubSegmento") Long idSegmento);
}
