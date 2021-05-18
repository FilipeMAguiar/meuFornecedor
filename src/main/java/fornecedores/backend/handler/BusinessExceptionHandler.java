package fornecedores.backend.handler;

import fornecedores.backend.dto.ErroDetalhesDTO;
import fornecedores.backend.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> erroDeNegocioExceptionHandler(BusinessException erroDeNegocio) {
        ErroDetalhesDTO erroNegocioDetail = ErroDetalhesDTO.builder()
                .titulo("Erro de negócio!")
                .mensagem(erroDeNegocio.getMessage())
                .build();
        return new ResponseEntity<>(erroNegocioDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}