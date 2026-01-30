package br.com.gerenciadorpedidos.processador.controller;

import br.com.gerenciadorpedidos.processador.dto.PedidoResponse;
import br.com.gerenciadorpedidos.processador.enums.StatusPedido;
import br.com.gerenciadorpedidos.processador.service.ConsultaPedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class ConsultaPedidoController {

    private static final List<String> FILTROS_PERMITIDOS = List.of("idExterno", "status", "dataCriacao");
    private static final String MENSAGEM_FILTROS_PERMITIDOS = "Por questões de performance, somente é aceito o sorter por: %s";
    private static final String MENSAGEM_LIMITE_REGISTROS = "Por questões de performance, somente é aceito 20 registros por request";
    private final ConsultaPedidoService consultaPedidoService;

    @GetMapping("/externo/{idExterno}")
    public ResponseEntity<PedidoResponse> buscarPorIdExterno(@PathVariable String idExterno) {
        PedidoResponse pedido = consultaPedidoService.buscarPorIdExterno(idExterno);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> listarTodos(@PageableDefault(size = 20, sort = "dataCriacao") Pageable pageable) {
        validarSort(pageable);
        Page<PedidoResponse> pedidos = consultaPedidoService.listarTodos(pageable);
        return ResponseEntity.ok(pedidos);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PedidoResponse>> listarPorStatus(@PathVariable StatusPedido status, @PageableDefault(size = 20, sort = "dataCriacao") Pageable pageable) {
        validarSort(pageable);
        Page<PedidoResponse> pedidos = consultaPedidoService.listarPorStatus(status, pageable);
        return ResponseEntity.ok(pedidos);
    }

    /**
     Limite de 20 registros para não sobrecarregar o banco
     Apenas campos que tem index, caso contrário vai acontecer um fullscan na tabela
     * @param pageable
     */
    private void validarSort(final Pageable pageable) {
        if (pageable.getPageSize() > 20) {
            throw new IllegalArgumentException(MENSAGEM_LIMITE_REGISTROS);
        }

        pageable.getSort().forEach(order -> {
            if (!FILTROS_PERMITIDOS.contains(order.getProperty())) {
                throw new IllegalArgumentException(MENSAGEM_FILTROS_PERMITIDOS.formatted(String.join(", ", FILTROS_PERMITIDOS)));
            }
        });
    }
}
