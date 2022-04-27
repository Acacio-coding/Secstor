package com.ifsc.secstor.api.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.annotation.Nullable;

@Getter
@Setter
public class AnonymizationModel {

    @Nullable
    @Length(max = 50)
    private String nome;

    @Nullable
    @Length(max = 50)
    private String sobrenome;

    @Nullable
    @Length(max = 9)
    private String genero;

    @Nullable
    @Length(min = 11, max = 11)
    private String cpf;

    @Nullable
    @Length(min = 14, max = 14)
    private String rg;

    @Nullable
    @Length(min = 21, max = 21)
    private String titulo_eleitor;

    @Nullable
    @Length(min = 12, max = 12)
    private String ra_exercito;

    @Nullable
    private String idade;

    @Nullable
    @Length(max = 100)
    private String local_de_nascimento;

    @Nullable
    @Length(max = 8)
    private String origem_etnica;

    @Nullable
    @Length(max = 11)
    private String nacionalidade;

    @Nullable
    @Length(max = 20)
    private String religiao;

    @Nullable
    @Length(max = 200)
    private String filiacao_politica;

    @Nullable
    @Length(max = 13)
    private String orientacao_sexual;

    @Nullable
    @Length(max = 2000)
    private String biometria;

    @Nullable
    @Length(max = 200)
    private String endereco;

    @Nullable
    @Length(max = 30)
    private String cidade;

    @Nullable
    @Length(max = 20)
    private String estado;

    @Nullable
    @Length(max = 300)
    private String email;

    @Nullable
    @Length(max = 10)
    private String telefone;

    @Nullable
    @Length(max = 11)
    private String celular;

    @Nullable
    private String renda;

    @Nullable
    private String latitude;

    @Nullable
    private String longitute;

    @Nullable
    @Length(max = 2000)
    private String anamnese;

    @Nullable
    @Length(max = 2000)
    private String plano_terapeutico;

    @Nullable
    @Length(max = 2000)
    private String laudo_exames;

    @Nullable
    @Length(max = 2000)
    private String prescricao_medica;

    @Nullable
    @Length(max = 2000)
    private String evolucao_quadro_clinico;

    @Nullable
    @Length(max = 2000)
    private String trajetoria_clinica;

    @Nullable
    @Length(max = 16)
    private String cartao_de_credito;

    @Nullable
    @Length(max = 2000)
    private String historico_pagamentos;

    @Nullable
    @Length(max = 2000)
    private String habitos_consumo;

    @Nullable
    @Length(max = 2000)
    private String preferencias_lazer;
}
