package fornecedores.backend.service;

import fornecedores.backend.dto.request.SegmentoRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.entity.Segmento;
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

    public List<Segmento> listarSegmentos(Long id) {
        List<Segmento> segmentoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(id)){
            Optional<Segmento> segmento = repository.findById(id);
            segmento.ifPresent(segmentoList::add);
            return segmentoList;
        } else {
            return this.repository.findAll();
        }
    }
}
