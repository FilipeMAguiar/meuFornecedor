package fornecedores.backend.service;

import fornecedores.backend.domain.TipoUsuarioEnum;
import fornecedores.backend.dto.AvaliacaoDTO;
import fornecedores.backend.dto.BuscaCepDTO;
import fornecedores.backend.dto.request.AtualizarSenhaRequest;
import fornecedores.backend.dto.request.AvaliacaoRequest;
import fornecedores.backend.dto.request.FornecedorRequest;
import fornecedores.backend.dto.response.FornecedorDTO;
import fornecedores.backend.dto.response.FornecedorResponseDTO;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.entity.*;
import fornecedores.backend.exception.BusinessException;
import fornecedores.backend.repository.*;
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
        fornecedor.setTipoUsuarioEnum(TipoUsuarioEnum.FORNECEDOR);
        fornecedor.setNickFornecedor(validaNickname(request));
        fornecedor.setCnpj(!ObjectUtils.isEmpty(request.getCnpj())? request.getCnpj() : "");
        fornecedor.setNomeFornecedor(request.getNome());
        fornecedor.setDescricaoFornecedor(!ObjectUtils.isEmpty(request.getDescricao())? request.getDescricao() : "");
        fornecedor.setEmail(validaEmail(request));
        fornecedor.setSenha(request.getSenha());
        SubSegmento subSegmento = subSegmentoRepository.findById(Long.valueOf(request.getIdSubSegmento())).get();
        fornecedor.setSubSegmento(subSegmento);
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

    private void populaFornecedorId(Long id, List<FornecedorResponseDTO> response, FornecedorResponseDTO responseList) {
        Fornecedor fornecedor = this.repository.findById(id).get();
        responseList.setIdFornecedor(id.toString());
        responseList.setNickFornecedor(fornecedor.getNickFornecedor());
        responseList.setNomeFornecedor(fornecedor.getNomeFornecedor());
        responseList.setEmailContato(fornecedor.getEmailContato());
        responseList.setCidade(fornecedor.getCidade());
        responseList.setNumero(!ObjectUtils.isEmpty(fornecedor.getNumero()) ? fornecedor.getNumero() : "Não informado");
        responseList.setInstagram(!ObjectUtils.isEmpty(fornecedor.getInstagram()) ? fornecedor.getInstagram() : "Não informado");
        responseList.setSite(!ObjectUtils.isEmpty(fornecedor.getSite()) ? fornecedor.getSite() : "Não informado");
        buildAvaliacaoId(responseList, fornecedor);
        responseList.setNota(fornecedor.getNota().toString());
        response.add(responseList);
    }

    private void populaFornecedores(List<FornecedorResponseDTO> responseDTOList, Fornecedor f) {
        FornecedorResponseDTO responseList = new FornecedorResponseDTO();
        responseList.setIdFornecedor(f.getIdFornecedor().toString());
        responseList.setNickFornecedor(f.getNickFornecedor());
        responseList.setNomeFornecedor(f.getNomeFornecedor());
        responseList.setEmailContato(f.getEmailContato());
        responseList.setCidade(f.getCidade());
        responseList.setNumero(!ObjectUtils.isEmpty(f.getNumero()) ? f.getNumero() : "Não informado");
        responseList.setInstagram(!ObjectUtils.isEmpty(f.getInstagram()) ? f.getInstagram() : "Não informado");
        responseList.setSite(!ObjectUtils.isEmpty(f.getSite()) ? f.getSite() : "Não informado");
        buildAvaliacao(f, responseList);
        responseList.setNota(f.getNota().toString());
        responseDTOList.add(responseList);
    }

    private void populaAvaliacao(AvaliacaoRequest request, Avaliacao avaliacao) {
        Usuario usuario = usuarioRepository.findById(Long.valueOf(request.getIdUsuario())).get();
        avaliacao.setIdUsuario(usuario);
        avaliacao.setAtendimento(Integer.valueOf(request.getAtendimento()));
        avaliacao.setConfiabilidade(Integer.valueOf(request.getConfiabilidade()));
        avaliacao.setPrecos(Integer.valueOf(request.getPrecos()));
        avaliacao.setQualidadeProduto(Integer.valueOf(request.getQualidadeProduto()));
        Fornecedor fornecedor = this.repository.findById(Long.valueOf(request.getIdFornecedor())).orElse(null);
        avaliacao.setFornecedor(fornecedor);
    }

    private void buildAvaliacaoId(FornecedorResponseDTO responseList, Fornecedor fornecedor) {
        List<AvaliacaoDTO> avaliacaoDTOList = new ArrayList<>();
        fornecedor.setNota(calculaMedia(fornecedor.getAvaliacao()));
        for (Avaliacao av : fornecedor.getAvaliacao()) {
            AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
            avaliacaoDTO.setAtendimento(av.getAtendimento());
            avaliacaoDTO.setConfiabilidade(av.getConfiabilidade());
            avaliacaoDTO.setPrecos(av.getPrecos());
            avaliacaoDTO.setQualidadeProduto(av.getQualidadeProduto());
            avaliacaoDTO.setIdAvaliacao(av.getIdAvaliacao());
            avaliacaoDTO.setNickFornecedor(av.getFornecedor().getNickFornecedor());
            avaliacaoDTO.setIdUsuario(av.getIdUsuario().getIdUsuario());
        }
        responseList.setAvaliacoes(avaliacaoDTOList);
    }

    private void buildAvaliacao(Fornecedor f, FornecedorResponseDTO responseList) {
        List<AvaliacaoDTO> avaliacaoDTOList = new ArrayList<>();
        f.setNota(calculaMedia(f.getAvaliacao()));
        for (Avaliacao av : f.getAvaliacao()) {
            AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
            avaliacaoDTO.setAtendimento(av.getAtendimento());
            avaliacaoDTO.setConfiabilidade(av.getConfiabilidade());
            avaliacaoDTO.setPrecos(av.getPrecos());
            avaliacaoDTO.setQualidadeProduto(av.getQualidadeProduto());
            avaliacaoDTO.setIdAvaliacao(av.getIdAvaliacao());
            avaliacaoDTO.setNickFornecedor(av.getFornecedor().getNickFornecedor());
            avaliacaoDTO.setIdUsuario(av.getIdUsuario().getIdUsuario());
            avaliacaoDTOList.add(avaliacaoDTO);
        }
        responseList.setAvaliacoes(avaliacaoDTOList);
    }

    private Integer calculaMedia(List<Avaliacao> avaliacoes) {
        List<Integer> medias = new ArrayList<>();
        int soma = 0;
        int avaliacoesSize = avaliacoes.size();
        for (Avaliacao avaliacao: avaliacoes) {
            Integer atendimento = avaliacao.getAtendimento();
            Integer confiabilidade = avaliacao.getConfiabilidade();
            Integer precos = avaliacao.getPrecos();
            Integer qualidadeProd = avaliacao.getQualidadeProduto();

            int media = (atendimento + confiabilidade + precos + qualidadeProd) / 4;
            medias.add(media);
        }

        for (Integer n : medias){
            soma += n;
        }

        return soma / avaliacoesSize;
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
