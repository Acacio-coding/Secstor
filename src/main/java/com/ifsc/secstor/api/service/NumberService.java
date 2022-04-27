package com.ifsc.secstor.api.service;

import com.ifsc.secstor.api.model.NumberModel;

public interface NumberService {
    NumberModel getNumbers(Long id);

    void saveNumber(String groupPrimeOrder, String g1, String g2, String secret);
}
