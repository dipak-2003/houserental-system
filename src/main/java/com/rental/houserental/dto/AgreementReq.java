package com.rental.houserental.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgreementReq {

    private boolean ownerSign;
    private boolean agreed;
    private List<String> terms;
}