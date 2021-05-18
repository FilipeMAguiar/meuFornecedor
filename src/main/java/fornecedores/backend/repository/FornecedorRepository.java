package fornecedores.backend.repository;

import fornecedores.backend.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Fornecedor findByNickFornecedor(String nickname);

    Fornecedor findByEmail(String email);
}
