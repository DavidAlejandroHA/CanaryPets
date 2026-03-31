package com.canarypets.backend.DTOs;

import java.util.List;

public class FilterDTO {

    private String type;
    private List<String> values;

    public FilterDTO(String type, List<String> values) {
        this.type = type;
        this.values = values;
    }

    public String getType() { return type; }
    public List<String> getValues() { return values; }
}