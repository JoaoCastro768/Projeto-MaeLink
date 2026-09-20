package br.com.fiap.maelink.bancoleite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BancoLeiteCreateRequest(
        @NotBlank @Size(max = 160) String nome,
        @NotBlank @Size(max = 220) String endereco,
        @NotBlank @Size(max = 180) String areaAtendida,
        @NotNull @Min(0) Integer capacidade
) {}
