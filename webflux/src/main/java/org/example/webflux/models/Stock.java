package org.example.webflux.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String symbol;
    private Integer price;

    @CreatedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created_date;

    @LastModifiedDate
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updated_date;
}
