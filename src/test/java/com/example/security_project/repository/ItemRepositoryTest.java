package com.example.security_project.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.security_project.domain.Item;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest // JPA 관련 컴포넌트(@Repository, @Entity 클래스만 등록)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Rollback(false)
    void testSave(){

        // Given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().name("아메리카노").build());
        items.add(Item.builder().name("카페라떼").build());
        items.add(Item.builder().name("아이스티").build());
        items.add(Item.builder().name("오미자차").build());
        items.add(Item.builder().name("플레인 요거트 스무디").build());

        // When
        List<Item> saved = itemRepository.saveAll(items);

        // Then
        assertThat(saved).hasSize(5);
    }

}
