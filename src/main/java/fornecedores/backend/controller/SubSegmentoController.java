package fornecedores.backend.controller;

import fornecedores.backend.dto.request.CriaSubSegmentoRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.dto.response.SubSegmentoResponseDTO;
import fornecedores.backend.dto.response.SubSegmentoWrapperResponseDTO;
import fornecedores.backend.entity.SubSegmento;
import fornecedores.backend.service.SubSegmentoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/subsegmento")
@AllArgsConstructor
public class SubSegmentoController {

    private final SubSegmentoService service;

    @PostMapping
    public ResponseMessage criarSubSegmento(@RequestBody CriaSubSegmentoRequest request) {
        return this.service.criarSubSegmento(request);
    }

    @GetMapping
    public List<SubSegmentoResponseDTO> listarSubSegmentos(@RequestParam(required = false) Long id){
        return this.service.listarSubSegmentos(id);
    }
}
