package com.sportscenter.model.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddSportServiceModel {
    private String name;
    private String description;
    private String sportImageUrl;
}
