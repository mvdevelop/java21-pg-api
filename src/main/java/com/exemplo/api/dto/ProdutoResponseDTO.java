package com.exemplo.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidade;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
