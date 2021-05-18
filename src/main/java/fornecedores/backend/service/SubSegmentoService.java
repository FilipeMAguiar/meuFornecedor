package fornecedores.backend.service;

import fornecedores.backend.dto.request.SubSegmentoRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.entity.SubSegmento;
import fornecedores.backend.repository.SubSegmentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubSegmentoService {

    private SubSegmentoRepository repository;

    public List<SubSegmento> adicionarSubSegmento(Long idSubSegmento) {
        List<SubSegmento> subSegmentoList = new ArrayList<>();
        Optional<SubSegmento> subSegmento = repository.findById(idSubSegmento);

        subSegmento.ifPresent(subSegmentoList::add);

        return subSegmentoList;
    }

    public ResponseMessage criarSubSegmento(SubSegmentoRequest request) {
        ResponseMessage response = new ResponseMessage();
        SubSegmento subSegmento = new SubSegmento();

        subSegmento.setIdSegmento(Optional.of(Long.valueOf(request.getIdSegmento())).orElse(null));
        subSegmento.setIdFornecedor(Optional.of(Long.valueOf(request.getIdFornecedor())).orElse(null));
        subSegmento.setNomeSubSegmento(request.getNomeSubSegmento());
        this.repository.save(subSegmento);

        response.setMessage("Subsegmento salvo com sucesso!");
        return response;
    }

    public List<SubSegmento> listarSubSegmentos(Long id) {
        List<SubSegmento> subSegmentoList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(id)){
            Optional<SubSegmento> segmento = repository.findById(id);
            segmento.ifPresent(subSegmentoList::add);
            return subSegmentoList;
        } else {
            return this.repository.findAll();
        }
    }
}
