package br.com.gerenciadorpedidos.processador.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProcessadoEvent implements Serializable {

    private UUID pedidoId;
    private String idExterno;
    private BigDecimal valorTotal;
    private LocalDateTime dataProcessamento;
}
