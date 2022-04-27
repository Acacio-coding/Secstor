package com.ifsc.secstor.api.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PSSShareModel {
    private List<String> shares;
    private List<String[]> macKeys;
    private List<String[]> macs;

    @Override
    public String toString() {
        return "PSSShareModel";
    }
}
