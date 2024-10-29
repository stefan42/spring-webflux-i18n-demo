package com.example.demo;

import jakarta.validation.constraints.Size;

public class DemoDTO {

    @Size(min = 1, max = 3)
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "DemoDTO{" +
                "field='" + field + '\'' +
                '}';
    }
}
