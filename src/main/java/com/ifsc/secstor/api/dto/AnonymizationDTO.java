package com.ifsc.secstor.api.dto;

import com.ifsc.secstor.api.model.AnonymizationModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnonymizationDTO {

    private List<AnonymizationModel> data;
}
