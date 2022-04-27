package com.ifsc.secstor.api.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CSSShareModel {
    private List<String> shares;
    private List<String> fingerprints;
    private Integer originalLength;
    private Integer encAlgorithm;
    private List<String> encKeys;

    @Override
    public String toString() {
        return "CSSShareModel";
    }
}
