package com.ifsc.secstor.facade;

import com.ifsc.secstor.api.dto.AnonymizationDTO;
import com.ifsc.secstor.api.model.AnonymizationModel;
import com.org.deidentifier.arx.*;
import com.org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import com.org.deidentifier.arx.criteria.KAnonymity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnonymizerEngine {

    private final ARXAnonymizer engine;
    private final ARXConfiguration config;

    public AnonymizerEngine() {
        this.engine = new ARXAnonymizer();
        this.config = ARXConfiguration.create();
    }


    public Object anonymize(AnonymizationDTO data) throws IOException {
        KAnonymity privacyModel = new KAnonymity(data.getData().size());
        this.config.addPrivacyModel(privacyModel);

        Data.DefaultData toBeAnonymized = new Data.DefaultData();

        toBeAnonymized.add(
                "nome", "sobrenome", "genero", "cpf", "rg", "titulo_eleitor",
                "ra_exercito", "idade", "local_de_nascimento",
                "origem_etnica", "nacionalidade", "religiao", "filiacao_politica",
                "orientacao_sexual", "biometria", "endereco", "cidade", "estado",
                "email", "telefone", "celular", "renda", "latitude", "longitude",
                "anamnese", "plano_terapeutico", "laudo_exames", "prescricao_medica",
                "evolucao_quadro_clinico", "trajetoria_clinica", "cartao_de_credito",
                "historico_pagamentos", "habitos_consumo", "preferencias_lazer"
        );

        for (AnonymizationModel object : data.getData()) {
            if (object.getNome() == null)
                object.setNome("NULL");

            if (object.getSobrenome() == null)
                object.setSobrenome("NULL");

            if (object.getGenero() == null)
                object.setGenero("NULL");

            if (object.getCpf() == null)
                object.setCpf("NULL");

            if (object.getRg() == null)
                object.setRg("NULL");

            if (object.getTitulo_eleitor() == null)
                object.setTitulo_eleitor("NULL");

            if (object.getRa_exercito() == null)
                object.setRa_exercito("NULL");

            if (object.getIdade() == null)
                object.setIdade("NULL");

            if (object.getLocal_de_nascimento() == null)
                object.setLocal_de_nascimento("NULL");

            if (object.getOrigem_etnica() == null)
                object.setOrigem_etnica("NULL");

            if (object.getNacionalidade() == null)
                object.setNacionalidade("NULL");

            if (object.getReligiao() == null)
                object.setReligiao("NULL");

            if (object.getFiliacao_politica() == null)
                object.setFiliacao_politica("NULL");

            if (object.getOrientacao_sexual() == null)
                object.setOrientacao_sexual("NULL");

            if (object.getBiometria() == null)
                object.setBiometria("NULL");

            if (object.getEndereco() == null)
                object.setEmail("NULL");

            if (object.getCidade() == null)
                object.setCidade("NULL");

            if (object.getEstado() == null)
                object.setEstado("NULL");

            if (object.getEmail() == null)
                object.setEmail("NULL");

            if (object.getTelefone() == null)
                object.setTelefone("NULL");

            if (object.getCelular() == null)
                object.setCelular("NULL");

            if (object.getRenda() == null)
                object.setRenda("NULL");

            if (object.getLatitude() == null)
                object.setLatitude("NULL");

            if (object.getLongitute() == null)
                object.setLongitute("NULL");

            if (object.getAnamnese() == null)
                object.setAnamnese("NULL");

            if (object.getPlano_terapeutico() == null)
                object.setPlano_terapeutico("NULL");

            if (object.getLaudo_exames() == null)
                object.setLaudo_exames("NULL");

            if (object.getPrescricao_medica() == null)
                object.setPrescricao_medica("NULL");

            if (object.getEvolucao_quadro_clinico() == null)
                object.setEvolucao_quadro_clinico("NULL");

            if (object.getTrajetoria_clinica() == null)
                object.setTrajetoria_clinica("NULL");

            if (object.getCartao_de_credito() == null)
                object.setCartao_de_credito("NULL");

            if (object.getHistorico_pagamentos() == null)
                object.setHistorico_pagamentos("NULL");

            if (object.getHabitos_consumo() == null)
                object.setHabitos_consumo("NULL");

            if (object.getPreferencias_lazer() == null)
                object.setPreferencias_lazer("NULL");

            toBeAnonymized.add(object.getNome(), object.getSobrenome(), object.getGenero(),
                    object.getCpf(), object.getRg(), object.getTitulo_eleitor(), object.getRa_exercito(), object.getIdade(),
                    object.getLocal_de_nascimento(), object.getOrigem_etnica(), object.getNacionalidade(),
                    object.getReligiao(), object.getFiliacao_politica(), object.getBiometria(), object.getOrientacao_sexual(),
                    object.getEndereco(), object.getCidade(), object.getEstado(), object.getEmail(), object.getTelefone(),
                    object.getCelular(), object.getRenda(), object.getLatitude(), object.getLongitute(), object.getAnamnese(),
                    object.getPlano_terapeutico(), object.getLaudo_exames(), object.getPrescricao_medica(), object.getEvolucao_quadro_clinico(),
                    object.getTrajetoria_clinica(), object.getCartao_de_credito(), object.getHistorico_pagamentos(), object.getHabitos_consumo(),
                    object.getPreferencias_lazer()
            );
        }

        toBeAnonymized.getDefinition().setAttributeType("nome", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("sobrenome", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("genero", getHierarchy());

        toBeAnonymized.getDefinition().setAttributeType("cpf", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("rg", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("titulo_eleitor", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("ra_exercito", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("idade", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("local_de_nascimento", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("origem_etnica", getHierarchy());

        toBeAnonymized.getDefinition().setAttributeType("nacionalidade", getHierarchy());

        toBeAnonymized.getDefinition().setAttributeType("religiao", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("filiacao_politica", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("biometria", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("orientacao_sexual", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("endereco", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("cidade", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("estado", getHierarchy());

        toBeAnonymized.getDefinition().setAttributeType("email", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("telefone", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("celular", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("renda", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("latitude", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("longitude", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("anamnese", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("plano_terapeutico", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("laudo_exames", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("prescricao_medica", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("evolucao_quadro_clinico", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("trajetoria_clinica", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("cartao_de_credito", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("historico_pagamentos", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("habitos_consumo", AttributeType.IDENTIFYING_ATTRIBUTE);

        toBeAnonymized.getDefinition().setAttributeType("preferencias_lazer", AttributeType.IDENTIFYING_ATTRIBUTE);

        ARXResult result = this.engine.anonymize(toBeAnonymized, this.config);

        List<AnonymizationModel> toReturn = new ArrayList<>();

        for (int i = 0; i < result.getOutput().getNumRows(); i++) {
            AnonymizationModel rowObject = new AnonymizationModel();
            for (int j = 0; j < result.getOutput().getNumColumns(); j++) {
                if (j == 0)
                    rowObject.setNome(result.getOutput().getValue(i, j));

                if (j == 1)
                    rowObject.setSobrenome(result.getOutput().getValue(i, j));

                if (j == 2)
                    rowObject.setGenero(result.getOutput().getValue(i, j));

                if (j == 3)
                    rowObject.setCpf(result.getOutput().getValue(i, j));

                if (j == 4)
                    rowObject.setRg(result.getOutput().getValue(i, j));

                if (j == 5)
                    rowObject.setTitulo_eleitor(result.getOutput().getValue(i, j));

                if (j == 6)
                    rowObject.setRa_exercito(result.getOutput().getValue(i, j));

                if (j == 7)
                    rowObject.setIdade(result.getOutput().getValue(i, j));

                if (j == 8)
                    rowObject.setLocal_de_nascimento(result.getOutput().getValue(i, j));

                if (j == 9)
                    rowObject.setOrigem_etnica(result.getOutput().getValue(i, j));

                if (j == 10)
                    rowObject.setNacionalidade(result.getOutput().getValue(i, j));

                if (j == 11)
                    rowObject.setReligiao(result.getOutput().getValue(i, j));

                if (j == 12)
                    rowObject.setFiliacao_politica(result.getOutput().getValue(i, j));

                if (j == 13)
                    rowObject.setBiometria(result.getOutput().getValue(i, j));

                if (j == 14)
                    rowObject.setOrientacao_sexual(result.getOutput().getValue(i, j));

                if (j == 15)
                    rowObject.setEndereco(result.getOutput().getValue(i, j));

                if (j == 16)
                    rowObject.setCidade(result.getOutput().getValue(i, j));

                if (j == 17)
                    rowObject.setEstado(result.getOutput().getValue(i, j));

                if (j == 18)
                    rowObject.setEmail(result.getOutput().getValue(i, j));

                if (j == 19)
                    rowObject.setTelefone(result.getOutput().getValue(i, j));

                if (j == 20)
                    rowObject.setCelular(result.getOutput().getValue(i, j));

                if (j == 21)
                    rowObject.setRenda(result.getOutput().getValue(i, j));

                if (j == 22)
                    rowObject.setLatitude(result.getOutput().getValue(i, j));

                if (j == 23)
                    rowObject.setLongitute(result.getOutput().getValue(i, j));

                if (j == 24)
                    rowObject.setAnamnese(result.getOutput().getValue(i, j));

                if (j == 25)
                    rowObject.setPlano_terapeutico(result.getOutput().getValue(i, j));

                if (j == 26)
                    rowObject.setLaudo_exames(result.getOutput().getValue(i, j));

                if (j == 27)
                    rowObject.setPrescricao_medica(result.getOutput().getValue(i, j));

                if (j == 28)
                    rowObject.setEvolucao_quadro_clinico(result.getOutput().getValue(i, j));

                if (j == 29)
                    rowObject.setTrajetoria_clinica(result.getOutput().getValue(i, j));

                if (j == 30)
                    rowObject.setCartao_de_credito(result.getOutput().getValue(i, j));

                if (j == 31)
                    rowObject.setHistorico_pagamentos(result.getOutput().getValue(i, j));

                if (j == 32)
                    rowObject.setHabitos_consumo(result.getOutput().getValue(i, j));

                if (j == 33)
                    rowObject.setPreferencias_lazer(result.getOutput().getValue(i, j));
            }
            toReturn.add(rowObject);
        }

        this.config.removeCriterion(privacyModel);
        return toReturn;
    }

    private HierarchyBuilderRedactionBased<?> getHierarchy() {
        return HierarchyBuilderRedactionBased.create(
                HierarchyBuilderRedactionBased.Order.RIGHT_TO_LEFT,
                HierarchyBuilderRedactionBased.Order.RIGHT_TO_LEFT,
                ' ',
                '*');
    }
}
