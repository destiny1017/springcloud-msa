package com.example.catalogservice.controller;

import com.example.catalogservice.dto.CatalogDto;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/health-check")
    public String healthCheck(HttpServletRequest request) {
        return "It's working in service catalog-service on port=" + request.getServerPort();
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> catalogs() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(catalogService.getAllCatalogs()
                        .stream().map(CatalogDto::toResponse)
                        .toList());
    }
}
