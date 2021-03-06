package fornecedores.backend.repository;

import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.entity.SubSegmento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Fornecedor findByNickFornecedor(String nickname);
    Fornecedor findByEmail(String email);
}
