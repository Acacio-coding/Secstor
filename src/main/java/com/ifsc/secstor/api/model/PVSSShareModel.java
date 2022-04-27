package com.ifsc.secstor.api.model;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PVSSShareModel {
    private List<String> shares;
    private String key;
    private BigInteger modulus;
}
