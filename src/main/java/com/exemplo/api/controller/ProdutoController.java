package com.exemplo.api.controller;

import com.exemplo.api.dto.ProdutoDTO;
import com.exemplo.api.dto.ProdutoResponseDTO;
import com.exemplo.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @Operation(summary = "Criar um novo produto", 
               description = "Cria um novo produto no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponseDTO produto = produtoService.criarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos", 
               description = "Retorna uma lista de todos os produtos cadastrados")
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        List<ProdutoResponseDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", 
               description = "Retorna um produto específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar produtos por nome", 
               description = "Busca produtos cujo nome contenha o termo fornecido")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", 
               description = "Atualiza os dados de um produto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable Long id, 
            @Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponseDTO produto = produtoService.atualizarProduto(id, dto);
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto", 
               description = "Remove um produto do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
