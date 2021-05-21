package fornecedores.backend.service;

import fornecedores.backend.dto.request.CriaSubSegmentoRequest;
import fornecedores.backend.dto.request.FornecedorRequest;
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
    private SegmentoRepository segmentoRepository;

    public List<SubSegmento> adicionaFornecedorAoSubSegmento(Fornecedor fornecedor, FornecedorRequest request) {
        List<SubSegmento> subSegmentoList = new ArrayList<>();
        List<Fornecedor> fornecedorList = new ArrayList<>();
        fornecedorList.add(fornecedor);
        for (String idSubSegmento : request.getSubSegmentoList().getIdSubSegmento()) {
            SubSegmento subSegmento = repository.findById(Long.valueOf(idSubSegmento)).get();
            subSegmento.setFornecedor(fornecedorList);
            subSegmentoList.add(subSegmento);
        }
        this.repository.saveAll(subSegmentoList);
        return subSegmentoList;
    }

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

    public List<SubSegmentoResponseDTO> listarSubSegmentos(Long id) {
        List<SubSegmentoResponseDTO> responseDTOList = new ArrayList<>();
        SubSegmentoResponseDTO responseDTO = new SubSegmentoResponseDTO();
        if (!ObjectUtils.isEmpty(id)) {
            populaSubSegmento(id, responseDTOList, responseDTO);
            return responseDTOList;
        } else {
            List<SubSegmento> subSegmentoList = repository.findAll();
            for (SubSegmento subSegmento : subSegmentoList) {
                populaListaSubSegmento(responseDTOList, subSegmento);
            }
            return responseDTOList;
        }
    }

    private void populaListaSubSegmento(List<SubSegmentoResponseDTO> responseDTOList, SubSegmento subSegmento) {
        SubSegmentoResponseDTO responseList = new SubSegmentoResponseDTO();
        List<FornecedorResponseDTO> fornecedorResponseDTOS = new ArrayList<>();
        FornecedorResponseDTO fornecedorResponseDTO = new FornecedorResponseDTO();
        Segmento segmento = segmentoRepository.findById(subSegmento.getSegmento().getIdSegmento()).get();
        responseList.setIdSegmento(subSegmento.getSegmento().getIdSegmento().toString());
        PopulaSubFornecedor(responseDTOList, responseList, fornecedorResponseDTOS, fornecedorResponseDTO, subSegmento, segmento);
    }

    private void populaSubSegmento(Long id, List<SubSegmentoResponseDTO> responseDTOList, SubSegmentoResponseDTO responseDTO) {
        List<FornecedorResponseDTO> fornecedorResponseDTOS = new ArrayList<>();
        FornecedorResponseDTO fornecedorResponseDTO = new FornecedorResponseDTO();
        SubSegmento subSegmento = repository.findById(id).get();
        Segmento segmento = segmentoRepository.findById(subSegmento.getSegmento().getIdSegmento()).get();
        responseDTO.setIdSegmento(segmento.getIdSegmento().toString());
        PopulaSubFornecedor(responseDTOList, responseDTO, fornecedorResponseDTOS, fornecedorResponseDTO, subSegmento, segmento);
    }

    private void PopulaSubFornecedor(List<SubSegmentoResponseDTO> responseDTOList, SubSegmentoResponseDTO responseDTO, List<FornecedorResponseDTO> fornecedorResponseDTOS, FornecedorResponseDTO fornecedorResponseDTO, SubSegmento subSegmento, Segmento segmento) {
        responseDTO.setIdSubSegmento(subSegmento.getIdSubSegmento());
        responseDTO.setNomeSubSegmento(subSegmento.getNomeSubSegmento());
        responseDTO.setNomeSegmento(segmento.getNomeSegmento());
        if (!ObjectUtils.isEmpty(subSegmento.getFornecedor())) {
            for (Fornecedor f: subSegmento.getFornecedor()) {
                fornecedorResponseDTO.setIdFornecedor(f.getIdFornecedor().toString());
                fornecedorResponseDTO.setNickFornecedor(f.getNickFornecedor());
                fornecedorResponseDTO.setNomeFornecedor(f.getNomeFornecedor());
                fornecedorResponseDTO.setEmailContato(f.getEmailContato());
                fornecedorResponseDTO.setPais(f.getPais());
                fornecedorResponseDTOS.add(fornecedorResponseDTO);
                responseDTO.setFornecedores(fornecedorResponseDTOS);
            }
        }
        responseDTOList.add(responseDTO);
    }
}
