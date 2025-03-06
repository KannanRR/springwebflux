package org.example.webflux.controllers;

import lombok.AllArgsConstructor;
import org.example.webflux.dto.StockDto;
import org.example.webflux.models.Stock;
import org.example.webflux.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public Optional<Stock> GetPrice() {
        return stockService.GetPrice();
    }

    @PostMapping
    public Stock AddStock(StockDto stockDto) {
        return stockService.AddStock(stockDto);
    }
}
