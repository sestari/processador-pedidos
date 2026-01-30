package br.com.gerenciadorpedidos.processador.dto;

import br.com.gerenciadorpedidos.processador.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {

    private Long id;
    private String idExterno;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String mensagemErro;
    private List<ItemPedidoResponse> itens;
}
