package com.example.catalogservice.dto;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class CatalogDto implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String productName;
    private Integer stock;
    private Date createdAt;


    public static CatalogDto of(CatalogEntity entity) {
        return CatalogDto.builder()
                .productId(entity.getProductId())
                .unitPrice(entity.getUnitPrice())
                .productName(entity.getProductName())
                .stock(entity.getStock())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static ResponseCatalog toResponse(CatalogDto dto) {
        return ResponseCatalog.builder()
                .productId(dto.getProductId())
                .productName(dto.getProductName())
                .unitPrice(dto.getUnitPrice())
                .stock(dto.getStock())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}