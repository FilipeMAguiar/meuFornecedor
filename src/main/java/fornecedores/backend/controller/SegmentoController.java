package fornecedores.backend.controller;

import fornecedores.backend.dto.SegmentoResponseDTO;
import fornecedores.backend.dto.request.SegmentoRequest;
import fornecedores.backend.dto.response.ResponseMessage;
import fornecedores.backend.entity.Segmento;
import fornecedores.backend.service.SegmentoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin
@RequestMapping("/segmento")
@AllArgsConstructor
public class SegmentoController {

    private final SegmentoService service;

    @PostMapping
    public ResponseMessage criarSegmento(@RequestBody SegmentoRequest request) {
        return this.service.criarSegmento(request);
    }

    @GetMapping
    public List<SegmentoResponseDTO> listarSegmentos(@RequestParam(required = false) Long id){
        return this.service.listarSegmentos(id);
    }
}
