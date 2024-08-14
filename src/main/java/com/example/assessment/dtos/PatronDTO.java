package com.example.assessment.dtos;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PatronDTO {

    private Long id;

    private String name;

    private String contactInfo;
}
