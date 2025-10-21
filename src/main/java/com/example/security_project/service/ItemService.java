package com.example.security_project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.security_project.domain.Item;
import com.example.security_project.dto.ItemDto;
import com.example.security_project.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemDto> retrieveItems() {
        return itemRepository.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
    }
    
    public ItemDto entityToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
    

}
