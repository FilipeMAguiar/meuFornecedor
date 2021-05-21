package fornecedores.backend.service;

import fornecedores.backend.domain.TipoUsuarioEnum;
import fornecedores.backend.dto.UsuarioDTO;
import fornecedores.backend.dto.request.AtualizarSenhaRequest;
import fornecedores.backend.dto.request.AvaliacaoRequest;
import fornecedores.backend.dto.request.UsuarioRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.entity.Usuario;
import fornecedores.backend.exception.BusinessException;
import fornecedores.backend.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final FornecedorService fornecedorService;

    public List<UsuarioDTO> listarUsuario(Long id) {
        List<UsuarioDTO> list = new ArrayList<>();
        if (Objects.nonNull(id)){
            Usuario usuario = repository.findById(id).orElse(null);
            if (!ObjectUtils.isEmpty(usuario)) {
                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setIdUsuario(id);
                usuarioDTO.setNickUsuario(usuario.getNickUsuario());
                usuarioDTO.setNomeUsuario(usuario.getNomeUsuario());
                usuarioDTO.setTipoUsuarioEnum(usuario.getTipoUsuarioEnum());
                list.add(usuarioDTO);
            }
            return list;
        } else {
            List<Usuario> usuarios = this.repository.findAll();
            for (Usuario u: usuarios) {
                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setIdUsuario(u.getIdUsuario());
                usuarioDTO.setNickUsuario(u.getNickUsuario());
                usuarioDTO.setNomeUsuario(u.getNomeUsuario());
                usuarioDTO.setTipoUsuarioEnum(u.getTipoUsuarioEnum());
                list.add(usuarioDTO);
            }
            return list;
        }
    }

    public ResponseMessage criarUsuario(UsuarioRequest request) throws BusinessException {
        Usuario usuario = new Usuario();
        ResponseMessage response = new ResponseMessage();

        populaUsuario(request, usuario);

        this.repository.save(usuario);
        response.setMessage("Usuário " + request.getNomeUsuario() + " com o id " + usuario.getIdUsuario() + " criado com sucesso!");
        return response;
    }

    public ResponseMessage deletarUsuario(Long id) {
        ResponseMessage response = new ResponseMessage();
        this.repository.deleteById(id);
        response.setMessage("Usuario deletado com sucesso.");
        return response;
    }

    public ResponseMessage atualizarUsuario(Long id, UsuarioRequest request) throws BusinessException {
        ResponseMessage response = new ResponseMessage();
        Usuario usuario = this.repository.findById(id).orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        checkCamposAndUpdate(request, usuario);
        this.repository.save(usuario);
        response.setMessage("Usuário atualizado com sucesso!");
        return response;
    }

    public ResponseMessage avaliarFornecedor(AvaliacaoRequest request) throws BusinessException {
        ResponseMessage responseMessage = new ResponseMessage();
        this.fornecedorService.avaliarFornecedor(request);
        responseMessage.setMessage("Avaliação concluída com sucesso.");
        return responseMessage;
    }

    private void populaUsuario(UsuarioRequest request, Usuario usuario) throws BusinessException {
        usuario.setNomeUsuario(request.getNomeUsuario());
        usuario.setNickUsuario(validaNickname(request));
        usuario.setTelefone(request.getTelefone());
        usuario.setEmail(validaEmail(request));
        usuario.setSenha(request.getSenha());
        usuario.setTipoUsuarioEnum(TipoUsuarioEnum.USUARIO);
    }

    private void checkCamposAndUpdate(UsuarioRequest request, Usuario usuario) throws BusinessException {
        if (!ObjectUtils.isEmpty(request.getNomeUsuario())) {
            usuario.setNomeUsuario(request.getNomeUsuario());
        }
        if (!ObjectUtils.isEmpty(request.getEmail())){
            validaEmail(request);
        }
        if (!ObjectUtils.isEmpty(request.getTelefone())){
            usuario.setTelefone(request.getTelefone());
        }
        if (!ObjectUtils.isEmpty(request.getNickname())){
            validaNickname(request);
        }
    }

    private String validaEmail(UsuarioRequest request) throws BusinessException {
        Usuario emailExistente = this.repository.findByEmail(request.getEmail());
        if (Objects.nonNull(emailExistente)) {
            throw new BusinessException("Este email já está em uso. Tente outro");
        } else {
            return request.getEmail();
        }
    }

    private String validaNickname(UsuarioRequest request) throws BusinessException {
        Usuario nickExistente = repository.findByNickUsuario(request.getNickname());
        if (Objects.nonNull(nickExistente)) {
            throw new BusinessException("Este Nickname já está em uso. Tente outro");
        } else {
            return request.getNickname();
        }
    }

    public ResponseMessage atualizarSenha(AtualizarSenhaRequest request) throws BusinessException {
        Usuario usuario = this.repository.findByEmail(request.getEmail());
        ResponseMessage response = new ResponseMessage();
        if (!ObjectUtils.isEmpty(usuario)){
            if (request.getNovaSenha().equals(usuario.getSenha())){
                throw new BusinessException("Senha igual a anterior.");
            } else {
                usuario.setSenha(request.getNovaSenha());
                this.repository.save(usuario);
                response.setMessage("Senha atualizada com sucesso!");
            }
        } else {
            throw new BusinessException("Usuário não encontrado.");
        }
        return response;
    }
}
