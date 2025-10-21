package com.example.security_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security_project.dto.ItemDto;
import com.example.security_project.service.ItemService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    
    // 상품 목록 조회
    @GetMapping("/items")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<ItemDto>> getItems() {
        
        List<ItemDto> items = itemService.retrieveItems();

        return ResponseEntity.status(HttpStatus.OK).body(items);

    }
    
}
