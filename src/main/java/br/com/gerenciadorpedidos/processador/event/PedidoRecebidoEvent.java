package br.com.gerenciadorpedidos.processador.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRecebidoEvent implements Serializable {

    private String idExterno;
    private LocalDateTime dataRecebimento;
    private List<ItemEvent> itens;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemEvent implements Serializable {
        private String idProduto;
        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal valorUnitario;
    }
}
