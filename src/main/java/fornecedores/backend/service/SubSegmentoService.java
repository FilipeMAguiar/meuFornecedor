package fornecedores.backend.service;

import fornecedores.backend.dto.SubSegmentoDTO;
import fornecedores.backend.dto.request.CriaSubSegmentoRequest;
import fornecedores.backend.dto.request.FornecedorRequest;
import fornecedores.backend.dto.response.FornecedorDTO;
import fornecedores.backend.dto.response.FornecedorResponseDTO;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.dto.response.SubSegmentoResponseDTO;
import fornecedores.backend.entity.Fornecedor;
import fornecedores.backend.entity.Segmento;
import fornecedores.backend.entity.SubSegmento;
import fornecedores.backend.repository.FornecedorRepository;
import fornecedores.backend.repository.SegmentoRepository;
import fornecedores.backend.repository.SubSegmentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SubSegmentoService {

    private SubSegmentoRepository repository;

    public ResponseMessage criarSubSegmento(CriaSubSegmentoRequest request) {
        ResponseMessage response = new ResponseMessage();
        SubSegmento subSegmento = new SubSegmento();
        Segmento segmento = new Segmento();
        segmento.setIdSegmento(Long.valueOf(request.getIdSegmento()));
        subSegmento.setSegmento(segmento);
        subSegmento.setNomeSubSegmento(request.getNomeSubSegmento());
        this.repository.save(subSegmento);

        response.setMessage("Subsegmento salvo com sucesso!");
        return response;
    }

    public List<SubSegmentoResponseDTO> listarSubSegmentos(Long id){
        List<SubSegmentoResponseDTO> responseDTOList = new ArrayList<>();
        SubSegmentoResponseDTO buildResponse = new SubSegmentoResponseDTO();
        if (!ObjectUtils.isEmpty(id)){
            SubSegmento subSegmento = buildSubSegmento(id, buildResponse);
            buildFornecedor(responseDTOList, buildResponse, subSegmento);
            return responseDTOList;
        } else {
            List<SubSegmento> subSegmentoList = repository.findAll();
            for (SubSegmento subSegmento : subSegmentoList) {
                SubSegmentoResponseDTO subSegmentos = new SubSegmentoResponseDTO();
                subSegmentos.setIdSegmento(subSegmento.getSegmento().getIdSegmento().toString());
                subSegmentos.setIdSubSegmento(subSegmento.getIdSubSegmento());
                subSegmentos.setNomeSubSegmento(subSegmento.getNomeSubSegmento());
                subSegmentos.setNomeSegmento(subSegmento.getSegmento().getNomeSegmento());
                buildFornecedor(responseDTOList, subSegmentos, subSegmento);
            }
            return responseDTOList;
        }
    }

    private void buildFornecedor(List<SubSegmentoResponseDTO> responseDTOList, SubSegmentoResponseDTO buildResponse, SubSegmento subSegmento) {
        List<Fornecedor> fornecedorList = subSegmento.getFornecedor();
        List<FornecedorDTO> fornecedorResponseList = new ArrayList<>();
        for (Fornecedor f: fornecedorList) {
            buildFornecedorSimples(fornecedorResponseList, f);
        }
        buildResponse.setFornecedores(fornecedorResponseList);
            responseDTOList.add(buildResponse);
    }

    private SubSegmento buildSubSegmento(Long id, SubSegmentoResponseDTO buildResponse) {
        SubSegmento subSegmento = this.repository.findById(id).orElse(null);
        buildResponse.setIdSegmento(subSegmento.getSegmento().getIdSegmento().toString());
        buildResponse.setIdSubSegmento(id);
        buildResponse.setNomeSubSegmento(subSegmento.getNomeSubSegmento());
        buildResponse.setNomeSegmento(subSegmento.getSegmento().getNomeSegmento());
        return subSegmento;
    }

    static void buildFornecedorSimples(List<FornecedorDTO> responseDTOList, Fornecedor f) {
        FornecedorDTO responseList = new FornecedorDTO();
        responseList.setIdFornecedor(f.getIdFornecedor().toString());
        responseList.setNickFornecedor(f.getNickFornecedor());
        responseList.setNomeFornecedor(f.getNomeFornecedor());
        responseList.setDescricao(f.getDescricaoFornecedor());
        responseList.setCnpj(!ObjectUtils.isEmpty(f.getCnpj()) ?  f.getCnpj() : "00.000.000/0000-00");
        responseList.setEmailContato(f.getEmailContato());
        responseList.setCidade(f.getCidade());
        responseList.setNumero(f.getTelefone());
        responseList.setInstagram(!ObjectUtils.isEmpty(f.getInstagram()) ? f.getInstagram() : "@meuFornecedor");
        responseList.setSite(!ObjectUtils.isEmpty(f.getSite()) ? f.getSite() : "www.meuFornecedor.com.br");
        responseList.setNota(!ObjectUtils.isEmpty(f.getNota()) ? f.getNota() : 5);
        responseDTOList.add(responseList);
    }
}
