package fornecedores.backend.repository;

import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.entity.Segmento;
import fornecedores.backend.entity.SubSegmento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubSegmentoRepository extends JpaRepository<SubSegmento, Long> {
}
