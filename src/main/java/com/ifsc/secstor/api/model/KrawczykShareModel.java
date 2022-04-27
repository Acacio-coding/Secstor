package com.ifsc.secstor.api.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KrawczykShareModel {
    private List<String> shares;
    private Integer originalLength;
    private Integer encAlgorithm;
    private List<String> encKeys;

    @Override
    public String toString() {
        return "KrawczykShareModel";
    }
}

