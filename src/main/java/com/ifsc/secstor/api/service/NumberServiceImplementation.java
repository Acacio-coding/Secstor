package com.ifsc.secstor.api.service;

import com.ifsc.secstor.api.model.NumberModel;
import com.ifsc.secstor.api.repository.NumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NumberServiceImplementation implements NumberService {
    private final NumberRepository numberRepository;

    @Override
    public NumberModel getNumbers(Long id) {
        return this.numberRepository.findById(id).orElse(null);
    }

    @Override
    public void saveNumber(String groupPrimeOrder, String g1, String g2, String secret) {
        var numberModel = new NumberModel();

        numberModel.setGroupPrimeOrder(groupPrimeOrder);
        numberModel.setG1(g1);
        numberModel.setG2(g2);
        numberModel.setSecret(secret);

        this.numberRepository.save(numberModel);
    }
}
