package fornecedores.backend.controller;

import fornecedores.backend.dto.request.AtualizarSenhaRequest;
import fornecedores.backend.dto.request.FornecedorRequest;
import fornecedores.backend.dto.response.FornecedorResponseDTO;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.exception.BusinessException;
import fornecedores.backend.service.FornecedorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin
@RequestMapping("/fornecedor")
@AllArgsConstructor
public class FornecedorController {

    private final FornecedorService service;

    @GetMapping
    public List<FornecedorResponseDTO> listarFornecedor(@RequestParam(required = false) Long id) {
        return this.service.listarFornecedor(id);
    }

    @PostMapping
    public ResponseMessage criarFornecedor(@RequestBody FornecedorRequest request) throws BusinessException {
        return this.service.criarFornecedor(request);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deletarFornecedor(@PathVariable Long id) {
        return this.service.deletarFornecedor(id);
    }

    @PutMapping("/{id}")
    public ResponseMessage atualizarFornecedor(@PathVariable Long id, @RequestBody FornecedorRequest request) throws BusinessException {
        return this.service.atualizarFornecedor(id, request);
    }

    @PutMapping("/senha")
    public ResponseMessage atualizarSenha(@RequestBody AtualizarSenhaRequest request) throws BusinessException {
        return this.service.atualizarSenha(request);
    }
}

