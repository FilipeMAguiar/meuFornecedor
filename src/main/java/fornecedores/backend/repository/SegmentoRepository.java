package fornecedores.backend.repository;

import fornecedores.backend.entity.Segmento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SegmentoRepository extends JpaRepository<Segmento, Long> {
}
