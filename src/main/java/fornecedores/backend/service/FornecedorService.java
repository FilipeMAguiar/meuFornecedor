package fornecedores.backend.service;

import fornecedores.backend.domain.TipoUsuarioEnum;
import fornecedores.backend.dto.BuscaCepDTO;
import fornecedores.backend.dto.request.AtualizarSenhaRequest;
import fornecedores.backend.dto.request.AvaliacaoRequest;
import fornecedores.backend.dto.request.FornecedorRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.entity.Avaliacao;
import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.exception.BusinessException;
import fornecedores.backend.repository.AvaliacaoRepository;
import fornecedores.backend.repository.FornecedorRepository;
import fornecedores.backend.util.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FornecedorService {
    
    private FornecedorRepository repository;
    private AvaliacaoRepository avaliacaoRepository;
    private SubSegmentoService subSegmentoService;

    public List<Fornecedor> listarFornecedor(Long id) {
        List<Fornecedor> fornecedors = new ArrayList<>();
        if (Objects.nonNull(id)){
            Optional<Fornecedor> fornecedor = repository.findById(id);
            fornecedor.ifPresent(fornecedors::add);
            return fornecedors;
        } else {
            return this.repository.findAll();
        }
    }

    public ResponseMessage criarFornecedor(FornecedorRequest request) throws BusinessException {
        Fornecedor fornecedor = new Fornecedor();
        ResponseMessage response = new ResponseMessage();

        populaFornecedor(request, fornecedor);
        populaEndereco(request, fornecedor);
        populaContato(request, fornecedor);

        Fornecedor fornecedorSalvo = this.repository.save(fornecedor);
        populaSubSegmento(request, fornecedorSalvo);

        response.setMessage("Fornecedor " + request.getNome() + " com o id " + fornecedor.getIdFornecedor() + " criado com sucesso!");
        return response;
    }

    public ResponseMessage atualizarFornecedor(Long id, FornecedorRequest request) throws BusinessException {
        ResponseMessage response = new ResponseMessage();
        Fornecedor fornecedor = this.repository.findById(id).orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        if (!ObjectUtils.isEmpty(request.getNome())) {
            fornecedor.setNomeFornecedor(request.getNome());
        }
        if (!ObjectUtils.isEmpty(request.getDescricao())){
            fornecedor.setDescricaoFornecedor(request.getDescricao());
        }
        if (!ObjectUtils.isEmpty(request.getEmail())){
            fornecedor.setEmail(validaEmail(request));
        }
        if (!ObjectUtils.isEmpty(request.getNickname())){
            fornecedor.setNickFornecedor(validaNickname(request));
        }
        if (!ObjectUtils.isEmpty(request.getCnpj())){
            fornecedor.setCnpj(request.getCnpj());
        }
        if (!ObjectUtils.isEmpty(request.getEndereco().getPais())){
            fornecedor.setPais(request.getEndereco().getPais());
        }
        if (!ObjectUtils.isEmpty(request.getEndereco().getCep())){
            fornecedor.setCep(request.getEndereco().getCep());
        }
        if (!ObjectUtils.isEmpty(request.getEndereco().getNumero())){
            fornecedor.setNumero(request.getEndereco().getNumero());
        }
        if (!ObjectUtils.isEmpty(request.getEndereco().getComplemento())){
            fornecedor.setComplemento(request.getEndereco().getComplemento());
        }
        if (!ObjectUtils.isEmpty(request.getContato().getWhatsapp())){
            fornecedor.setWhatsapp(request.getContato().getWhatsapp());
        }
        if (!ObjectUtils.isEmpty(request.getContato().getSite())){
            fornecedor.setSite(request.getContato().getSite());
        }
        if (!ObjectUtils.isEmpty(request.getContato().getInstagram())){
            fornecedor.setInstagram(request.getContato().getInstagram());
        }
        if (!ObjectUtils.isEmpty(request.getContato().getEmailContato())){
            fornecedor.setEmailContato(request.getContato().getEmailContato());
        }
        if (!ObjectUtils.isEmpty(request.getContato().getTelefone())){
            fornecedor.setTelefone(request.getContato().getTelefone());
        }

        this.repository.save(fornecedor);
        response.setMessage("Usuário atualizado com sucesso!");
        return response;
    }

    public void avaliarFornecedor(Long idFornecedor, AvaliacaoRequest request) throws BusinessException {
        Fornecedor fornecedor = this.repository.findById(idFornecedor).orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        Avaliacao avaliacao = new Avaliacao();
        List<Avaliacao> avaliacaoList = new ArrayList<>();

        populaAvaliacao(request, avaliacao);
        Avaliacao avaliado = avaliacaoRepository.save(avaliacao);

        avaliacaoList.add(avaliado);
        fornecedor.setAvaliacao(avaliacaoList);
        fornecedor.setIdFornecedor(idFornecedor);

        this.repository.save(fornecedor);
    }

    public ResponseMessage atualizarSenha(AtualizarSenhaRequest request) throws BusinessException {
        ResponseMessage response = new ResponseMessage();
        Fornecedor fornecedor = this.repository.findByEmail(request.getEmail());
        if (!ObjectUtils.isEmpty(fornecedor)){
            if (request.getNovaSenha().equals(fornecedor.getSenha())){
                throw new BusinessException("Senha igual a anterior.");
            } else {
                fornecedor.setSenha(request.getNovaSenha());
                this.repository.save(fornecedor);
                response.setMessage("Senha atualizada com sucesso!");
            }
        } else {
            throw new BusinessException("Fornecedor não encontrado.");
        }
        return response;
    }

    public ResponseMessage deletarFornecedor(Long id) {
        ResponseMessage response = new ResponseMessage();
        this.repository.deleteById(id);
        response.setMessage("Fornecedor deletado com sucesso.");
        return response;
    }

    private void populaFornecedor(FornecedorRequest request, Fornecedor fornecedor) throws BusinessException {
        fornecedor.setTipoUsuarioEnum(TipoUsuarioEnum.FORNECEDOR);
        fornecedor.setNickFornecedor(validaNickname(request));
        fornecedor.setCnpj(!ObjectUtils.isEmpty(request.getCnpj())? request.getCnpj() : "");
        fornecedor.setNomeFornecedor(request.getNome());
        fornecedor.setDescricaoFornecedor(!ObjectUtils.isEmpty(request.getDescricao())? request.getDescricao() : "");
        fornecedor.setEmail(validaEmail(request));
        fornecedor.setSenha(request.getSenha());
    }

    private void populaEndereco(FornecedorRequest request, Fornecedor fornecedor) throws BusinessException {
        buscarCamposByCep(request, fornecedor);
        fornecedor.setComplemento(!ObjectUtils.isEmpty(request.getEndereco().getComplemento()) ? request.getEndereco().getComplemento() : "");
        fornecedor.setNumero(!ObjectUtils.isEmpty(request.getEndereco().getNumero()) ? request.getEndereco().getNumero() : "");
        fornecedor.setPais(!ObjectUtils.isEmpty(request.getEndereco().getPais()) ? request.getEndereco().getPais() : "");
    }

    private void populaContato(FornecedorRequest request, Fornecedor fornecedor) {
        fornecedor.setWhatsapp(Optional.ofNullable(request.getContato().getWhatsapp()).orElse(""));
        fornecedor.setSite(Optional.ofNullable(request.getContato().getSite()).orElse(""));
        fornecedor.setInstagram(Optional.ofNullable(request.getContato().getInstagram()).orElse(""));
        fornecedor.setEmailContato(Optional.ofNullable(request.getContato().getEmailContato()).orElse(""));
        fornecedor.setTelefone(Optional.ofNullable(request.getContato().getTelefone()).orElse(""));
    }

    private void populaSubSegmento(FornecedorRequest request, Fornecedor fornecedorSalvo) {
        fornecedorSalvo.setSubSegmento(subSegmentoService.adicionarSubSegmento(request.getIdSubSegmento()));
    }

    private void populaAvaliacao(AvaliacaoRequest request, Avaliacao avaliacao) {
        avaliacao.setIdAvaliador(request.getIdUsuario());
        avaliacao.setAtendimento(request.getAtendimento());
        avaliacao.setConfiabilidade(request.getConfiabilidade());
        avaliacao.setPrecos(request.getPrecos());
        avaliacao.setQualidadeProduto(request.getQualidadeProduto());
    }

    private String validaEmail(FornecedorRequest request) throws BusinessException {
        Fornecedor emailExistente = this.repository.findByEmail(request.getEmail());
        if (Objects.nonNull(emailExistente)) {
            throw new BusinessException("Este email já está em uso. Tente outro");
        } else {
            return request.getEmail();
        }
    }

    private String validaNickname(FornecedorRequest request) throws BusinessException {
        Fornecedor nickExistente = repository.findByNickFornecedor(request.getNickname());
        if (Objects.nonNull(nickExistente)) {
            throw new BusinessException("Este Nickname já está em uso. Tente outro");
        } else {
            return request.getNickname();
        }
    }

    private void buscarCamposByCep(FornecedorRequest request, Fornecedor fornecedor) throws BusinessException {
        if (!ObjectUtils.isEmpty(request.getEndereco().getCep())) {
            String json = buscarCep(request.getEndereco().getCep());
            BuscaCepDTO buscaCepDTO = JsonUtil.jsonToObject(json, BuscaCepDTO.class);
            fornecedor.setCidade(buscaCepDTO.getLocalidade());
            fornecedor.setBairro(buscaCepDTO.getBairro());
            fornecedor.setEstado(buscaCepDTO.getUf());
            fornecedor.setRua(buscaCepDTO.getLogradouro());
            fornecedor.setCep(buscaCepDTO.getCep());
        } else {
            throw new BusinessException("Cep deverá ser informado");
        }
    }

    public String buscarCep(String cep) {
        String json;
        try {
            URL url = new URL("http://viacep.com.br/ws/"+ cep +"/json");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder jsonSb = new StringBuilder();

            br.lines().forEach(l -> jsonSb.append(l.trim()));

            json = jsonSb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
