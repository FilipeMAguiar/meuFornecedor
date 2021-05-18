package fornecedores.backend.controller;

import fornecedores.backend.dto.request.SubSegmentoRequest;
import fornecedores.backend.dto.response.ResponseMessage;
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
    public ResponseMessage criarSubSegmento(@RequestBody SubSegmentoRequest request) {
        return this.service.criarSubSegmento(request);
    }

    @GetMapping
    public List<SubSegmento> listarSubSegmentos(@RequestParam(required = false) Long id){
        return this.service.listarSubSegmentos(id);
    }
}
