package fornecedores.backend.controller;

import fornecedores.backend.dto.UsuarioDTO;
import fornecedores.backend.dto.request.AtualizarSenhaRequest;
import fornecedores.backend.dto.request.AvaliacaoRequest;
import fornecedores.backend.dto.request.UsuarioRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.exception.BusinessException;
import fornecedores.backend.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin
@RequestMapping("/usuario")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public List<UsuarioDTO> listarUsuario(@RequestParam(required = false) Long id) {
        return this.service.listarUsuario(id);
    }

    @PostMapping
    public ResponseMessage criarUsuario(@RequestBody UsuarioRequest request) throws BusinessException {
        return this.service.criarUsuario(request);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deletarUsuario(@PathVariable Long id){
        return this.service.deletarUsuario(id);
    }

    @PutMapping("/{id}")
    public ResponseMessage atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request) throws BusinessException {
        return this.service.atualizarUsuario(id, request);
    }

    @PostMapping("/avaliacao")
    public ResponseMessage avaliarFornecedor(@RequestBody AvaliacaoRequest request) throws BusinessException {
        return this.service.avaliarFornecedor(request);
    }

    @CrossOrigin(origins = "https://meufornecedor.herokuapp.com")
    @PutMapping("/senha")
    public ResponseMessage atualizarSenha(@RequestBody AtualizarSenhaRequest request) throws BusinessException {
        return this.service.atualizarSenha(request);
    }
}
