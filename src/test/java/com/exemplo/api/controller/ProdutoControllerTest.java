package com.exemplo.api.controller;

import com.exemplo.api.dto.ProdutoDTO;
import com.exemplo.api.dto.ProdutoResponseDTO;
import com.exemplo.api.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarTodosProdutos() throws Exception {
        ProdutoResponseDTO p1 = new ProdutoResponseDTO();
        p1.setId(1L);
        p1.setNome("Product 1");
        p1.setDescricao("Description 1 with enough chars");
        p1.setPreco(new BigDecimal("99.99"));
        p1.setQuantidade(10);

        ProdutoResponseDTO p2 = new ProdutoResponseDTO();
        p2.setId(2L);
        p2.setNome("Product 2");
        p2.setDescricao("Description 2 with enough chars");
        p2.setPreco(new BigDecimal("199.99"));
        p2.setQuantidade(20);

        when(produtoService.listarTodos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Product 1"))
                .andExpect(jsonPath("$[1].nome").value("Product 2"));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(1L);
        dto.setNome("Test Product");
        dto.setDescricao("Test description with enough chars");
        dto.setPreco(new BigDecimal("99.99"));
        dto.setQuantidade(10);

        when(produtoService.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Test Product"))
                .andExpect(jsonPath("$.preco").value(99.99));
    }

    @Test
    void deveRetornar404QuandoProdutoNaoEncontrado() throws Exception {
        when(produtoService.buscarPorId(999L))
                .thenThrow(new RuntimeException("Produto não encontrado"));

        mockMvc.perform(get("/api/produtos/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"));
    }

    @Test
    void deveCriarNovoProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("New Product");
        dto.setDescricao("New description with enough chars for validation");
        dto.setPreco(new BigDecimal("149.99"));
        dto.setQuantidade(5);

        ProdutoResponseDTO response = new ProdutoResponseDTO();
        response.setId(1L);
        response.setNome(dto.getNome());
        response.setDescricao(dto.getDescricao());
        response.setPreco(dto.getPreco());
        response.setQuantidade(dto.getQuantidade());
        response.setDataCriacao(LocalDateTime.now());
        response.setDataAtualizacao(LocalDateTime.now());

        when(produtoService.criarProduto(any(ProdutoDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("New Product"));
    }

    @Test
    void deveRetornar400QuandoDadosInvalidos() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("AB");
        dto.setDescricao("desc");
        dto.setPreco(new BigDecimal("0.00"));
        dto.setQuantidade(-1);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.nome").exists());
    }

    @Test
    void deveDeletarProduto() throws Exception {
        mockMvc.perform(delete("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveBuscarProdutosPorNome() throws Exception {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(1L);
        dto.setNome("Dell Product");
        dto.setDescricao("Dell description with enough chars");
        dto.setPreco(new BigDecimal("4500.00"));
        dto.setQuantidade(10);

        when(produtoService.buscarPorNome("Dell")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/produtos/buscar")
                        .param("nome", "Dell")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Dell Product"));
    }
}
