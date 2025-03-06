package org.example.webflux.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StockDto {
    private Integer price;
    private String synbol;
}
