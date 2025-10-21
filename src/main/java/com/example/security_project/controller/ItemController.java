package com.example.security_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.security_project.dto.ItemDto;
import com.example.security_project.service.ItemService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    
    // 상품 목록 조회
    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> getItems() {
        
        List<ItemDto> items = itemService.retrieveItems();

        return ResponseEntity.ok().body(items);

    }
    
}
