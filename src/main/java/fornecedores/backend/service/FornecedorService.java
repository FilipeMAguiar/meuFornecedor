package fornecedores.backend.service;

import fornecedores.backend.domain.TipoUsuarioEnum;
import fornecedores.backend.dto.BuscaCepDTO;
import fornecedores.backend.dto.request.AtualizarSenhaRequest;
import fornecedores.backend.dto.request.AvaliacaoRequest;
import fornecedores.backend.dto.request.FornecedorRequest;
import fornecedores.backend.dto.response.FornecedorResponseDTO;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.entity.Avaliacao;
import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.entity.SubSegmento;
import fornecedores.backend.entity.Usuario;
import fornecedores.backend.exception.BusinessException;
import fornecedores.backend.repository.AvaliacaoRepository;
import fornecedores.backend.repository.FornecedorRepository;
import fornecedores.backend.repository.SubSegmentoRepository;
import fornecedores.backend.repository.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;
    private SubSegmentoRepository subSegmentoRepository;

    public List<FornecedorResponseDTO> listarFornecedor(Long id) {
        List<FornecedorResponseDTO> responseDTOList = new ArrayList<>();
        FornecedorResponseDTO responseDTO = new FornecedorResponseDTO();
        if (Objects.nonNull(id)){
            populaFornecedorId(id, responseDTOList, responseDTO);
            return responseDTOList;
        } else {
            List<Fornecedor> fornecedorList = this.repository.findAll();
            for (Fornecedor f: fornecedorList) {
                populaFornecedores(responseDTOList, f);
            }
            return responseDTOList;
        }
    }

    public ResponseMessage criarFornecedor(FornecedorRequest request) throws BusinessException {
        ResponseMessage response = new ResponseMessage();
        Fornecedor fornecedor = new Fornecedor();

        populaFornecedor(request, fornecedor);
        populaEndereco(request, fornecedor);
        populaContato(request, fornecedor);

        this.repository.save(fornecedor);
        //List<SubSegmento> subSegmentoList = this.subSegmentoService.adicionaFornecedorAoSubSegmento(fornecedorSalvo, request);
        //fornecedorSalvo.setSubSegmentos(subSegmentoList);
        //this.repository.save(fornecedorSalvo);

        response.setMessage("Fornecedor " + request.getNome() + " com o id " + fornecedor.getIdFornecedor() + " criado com sucesso!");
        return response;
    }

    public ResponseMessage atualizarFornecedor(Long id, FornecedorRequest request) throws BusinessException {
        ResponseMessage response = new ResponseMessage();
        Fornecedor fornecedor = this.repository.findById(id).orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        checkCamposAndUpdate(request, fornecedor);

        this.repository.save(fornecedor);
        response.setMessage("Usuário atualizado com sucesso!");
        return response;
    }

    public void avaliarFornecedor(AvaliacaoRequest request) throws BusinessException {
        List<Avaliacao> avaliacaoList = new ArrayList<>();
        Fornecedor fornecedor = this.repository.findById(Long.valueOf(request.getIdFornecedor())).orElseThrow(() -> new BusinessException("Usuário não encontrado."));
        Avaliacao avaliacao = new Avaliacao();
        populaAvaliacao(request, avaliacao);
        avaliacaoRepository.save(avaliacao);
        avaliacaoList.add(avaliacao);
        fornecedor.setAvaliacao(avaliacaoList);
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
        List<SubSegmento> subSegmentoList = new ArrayList<>();
        fornecedor.setTipoUsuarioEnum(TipoUsuarioEnum.FORNECEDOR);
        fornecedor.setNickFornecedor(validaNickname(request));
        fornecedor.setCnpj(!ObjectUtils.isEmpty(request.getCnpj())? request.getCnpj() : "");
        fornecedor.setNomeFornecedor(request.getNome());
        fornecedor.setDescricaoFornecedor(!ObjectUtils.isEmpty(request.getDescricao())? request.getDescricao() : "");
        fornecedor.setEmail(validaEmail(request));
        fornecedor.setSenha(request.getSenha());
        for (String idSubsegmento: request.getSubSegmentoList().getIdSubSegmento()) {
            SubSegmento subSegmento = subSegmentoRepository.findById(Long.valueOf(idSubsegmento)).orElse(null);
            subSegmentoList.add(subSegmento);
        }
        fornecedor.setSubSegmentos(subSegmentoList);
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

    private void populaFornecedorId(Long id, List<FornecedorResponseDTO> response, FornecedorResponseDTO responseDTO) {
        Fornecedor fornecedor = this.repository.findById(id).get();
        responseDTO.setIdFornecedor(id.toString());
        responseDTO.setNickFornecedor(fornecedor.getNickFornecedor());
        responseDTO.setNomeFornecedor(fornecedor.getNomeFornecedor());
        responseDTO.setEmailContato(fornecedor.getEmailContato());
        responseDTO.setPais(fornecedor.getPais());
        response.add(responseDTO);
    }


    private void populaFornecedores(List<FornecedorResponseDTO> responseDTOList, Fornecedor f) {
        FornecedorResponseDTO responseList = new FornecedorResponseDTO();
        responseList.setIdFornecedor(f.getIdFornecedor().toString());
        responseList.setNickFornecedor(f.getNickFornecedor());
        responseList.setNomeFornecedor(f.getNomeFornecedor());
        responseList.setEmailContato(f.getEmailContato());
        responseList.setPais(f.getPais());
        responseDTOList.add(responseList);
    }

    private void populaAvaliacao(AvaliacaoRequest request, Avaliacao avaliacao) {
        Usuario usuario = usuarioRepository.findById(Long.valueOf(request.getIdUsuario())).get();
        avaliacao.setIdUsuario(usuario);
        avaliacao.setAtendimento(Long.valueOf(request.getAtendimento()));
        avaliacao.setConfiabilidade(Long.valueOf(request.getConfiabilidade()));
        avaliacao.setPrecos(Long.valueOf(request.getPrecos()));
        avaliacao.setQualidadeProduto(Long.valueOf(request.getQualidadeProduto()));
        //avaliacao.setIdFornecedor(Long.valueOf(request.getIdFornecedor()));
    }

    private void checkCamposAndUpdate(FornecedorRequest request, Fornecedor fornecedor) throws BusinessException {
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
