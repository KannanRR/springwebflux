package org.example.webflux.services;

import lombok.AllArgsConstructor;
import org.example.webflux.dto.StockDto;
import org.example.webflux.models.Stock;
import org.example.webflux.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    private StockRepository stockRepository;

    @Autowired
    StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Optional<Stock> GetPrice() {
        return stockRepository.findById(1L);
    }

    public Stock AddStock(StockDto stockDto) {
        Stock newstock = Stock.builder().symbol(stockDto.getSynbol())
                .price(stockDto.getPrice())
                .build();

        return stockRepository.save(newstock);
    }
}
