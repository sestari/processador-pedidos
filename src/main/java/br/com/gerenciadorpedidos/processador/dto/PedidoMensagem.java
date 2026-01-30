package br.com.gerenciadorpedidos.processador.dto;

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
public class PedidoMensagem implements Serializable {

    private String idExterno;
    private LocalDateTime dataRecebimento;
    private List<ItemMensagem> itens;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemMensagem implements Serializable {
        private String idProduto;
        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal valorUnitario;
    }
}
