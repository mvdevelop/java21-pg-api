package com.exemplo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para registro de novo usuário.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 200)
    private String nomeCompleto;

    @NotBlank(message = "Password é obrigatório")
    @Size(min = 6, message = "Password deve ter no mínimo 6 caracteres")
    private String password;
}
