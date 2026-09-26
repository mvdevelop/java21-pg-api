package com.exemplo.api.service;

import com.exemplo.api.dto.ProdutoDTO;
import com.exemplo.api.dto.ProdutoResponseDTO;
import com.exemplo.api.model.Produto;
import com.exemplo.api.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        produtoService = new ProdutoService(produtoRepository);
    }

    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Test Product");
        dto.setDescricao("Test Description with enough chars");
        dto.setPreco(new BigDecimal("99.99"));
        dto.setQuantidade(10);

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome(dto.getNome());
        produtoSalvo.setDescricao(dto.getDescricao());
        produtoSalvo.setPreco(dto.getPreco());
        produtoSalvo.setQuantidade(dto.getQuantidade());
        produtoSalvo.setDataCriacao(LocalDateTime.now());
        produtoSalvo.setDataAtualizacao(LocalDateTime.now());

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        ProdutoResponseDTO response = produtoService.criarProduto(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Product", response.getNome());
        assertEquals(new BigDecimal("99.99"), response.getPreco());

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveListarTodosProdutos() {
        Produto p1 = new Produto();
        p1.setId(1L);
        p1.setNome("Product 1");

        Produto p2 = new Produto();
        p2.setId(2L);
        p2.setNome("Product 2");

        when(produtoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<ProdutoResponseDTO> response = produtoService.listarTodos();

        assertEquals(2, response.size());
        assertEquals("Product 1", response.get(0).getNome());
        assertEquals("Product 2", response.get(1).getNome());
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Test Product");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoResponseDTO response = produtoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Product", response.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoService.buscarPorId(999L));

        assertEquals("Produto não encontrado", exception.getMessage());
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(1L);

        produtoService.deletarProduto(1L);

        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoDeletarProdutoInexistente() {
        when(produtoRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produtoService.deletarProduto(999L));

        assertEquals("Produto não encontrado", exception.getMessage());
        verify(produtoRepository, never()).deleteById(999L);
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Updated Product");
        dto.setDescricao("Updated Description with enough chars");
        dto.setPreco(new BigDecimal("199.99"));
        dto.setQuantidade(20);

        Produto produtoExistente = new Produto();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Old Product");

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome(dto.getNome());
        produtoAtualizado.setDescricao(dto.getDescricao());
        produtoAtualizado.setPreco(dto.getPreco());
        produtoAtualizado.setQuantidade(dto.getQuantidade());

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        ProdutoResponseDTO response = produtoService.atualizarProduto(1L, dto);

        assertEquals(1L, response.getId());
        assertEquals("Updated Product", response.getNome());
        assertEquals(new BigDecimal("199.99"), response.getPreco());
    }
}
