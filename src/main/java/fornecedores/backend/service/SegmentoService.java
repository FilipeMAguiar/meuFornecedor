package fornecedores.backend.service;

import fornecedores.backend.dto.SegmentoResponseDTO;
import fornecedores.backend.dto.SubSegmentoDTO;
import fornecedores.backend.dto.request.SegmentoRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.dto.response.SubSegmentoResponseDTO;
import fornecedores.backend.entity.Segmento;
import fornecedores.backend.entity.SubSegmento;
import fornecedores.backend.repository.SegmentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SegmentoService {

    private final SegmentoRepository repository;

    public ResponseMessage criarSegmento(SegmentoRequest request) {
        ResponseMessage response = new ResponseMessage();
        Segmento segmento = new Segmento();

        segmento.setNomeSegmento(request.getNomeSegmento());
        this.repository.save(segmento);

        response.setMessage("Segmento salvo com sucesso!");
        return response;
    }

    public List<SegmentoResponseDTO> listarSegmentos(Long id) {
        List<SegmentoResponseDTO> response = new ArrayList<>();
        SegmentoResponseDTO responseDTO = new SegmentoResponseDTO();
        if (!ObjectUtils.isEmpty(id)){
            Segmento segmento = repository.findById(id).get();
            responseDTO.setIdSegmento(String.valueOf(segmento.getIdSegmento()));
            responseDTO.setNomeSegmento(segmento.getNomeSegmento());
            List<SubSegmento> subSegmentoList = segmento.getSubSegmentos();
            List<SubSegmentoDTO> subSegmentoDTOS = new ArrayList<>();
            for (SubSegmento s: subSegmentoList) {
                SubSegmentoDTO subSegmentos = new SubSegmentoDTO();
                subSegmentos.setIdSubSegmento(String.valueOf(s.getIdSubSegmento()));
                subSegmentos.setNomeSubSegmento(s.getNomeSubSegmento());
                subSegmentoDTOS.add(subSegmentos);
            }
            responseDTO.setSubSegmentos(subSegmentoDTOS);
            response.add(responseDTO);
            return response;
        } else {
            List<Segmento> segmentoList = this.repository.findAll();
            for (Segmento s: segmentoList) {
                SegmentoResponseDTO responseList = new SegmentoResponseDTO();
                responseList.setIdSegmento(String.valueOf(s.getIdSegmento()));
                responseList.setNomeSegmento(s.getNomeSegmento());
                List<SubSegmento> subSegmentoList = s.getSubSegmentos();
                List<SubSegmentoDTO> subSegmentoDTOS = new ArrayList<>();
                for (SubSegmento subs: subSegmentoList) {
                    SubSegmentoDTO subSegmentos = new SubSegmentoDTO();
                    subSegmentos.setIdSubSegmento(String.valueOf(subs.getIdSubSegmento()));
                    subSegmentos.setNomeSubSegmento(subs.getNomeSubSegmento());
                    subSegmentoDTOS.add(subSegmentos);
                }
                responseList.setSubSegmentos(subSegmentoDTOS);
                response.add(responseList);
            }
            return response;
        }
    }
}
