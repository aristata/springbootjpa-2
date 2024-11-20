package kr.co.aristatait.springbootjpa1.service;

import kr.co.aristatait.springbootjpa1.domain.item.Item;
import kr.co.aristatait.springbootjpa1.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    void 상품저장() {
        //given
        Item item = new Item();
        item.setName("testItem");
        item.setPrice(1500);
        item.addStock(100);

        //when
        Long savedItemId = itemService.saveItem(item);

        //then
        Assertions.assertEquals(item, itemRepository.findOne(savedItemId));
    }

    @Test
    void 상품목록조회() {
        //given
        Item item = new Item();
        item.setName("testItem1");
        itemService.saveItem(item);

        Item item2 = new Item();
        item2.setName("testItem2");
        itemService.saveItem(item2);

        //when
        List<Item> foundItems = itemService.findItems();

        //then
        Assertions.assertEquals(2, foundItems.size());
    }

    @Test
    void 상품조회() {
        //given
        Item item = new Item();
        item.setName("testItem");
        Long savedItemId = itemService.saveItem(item);

        //when
        Item foundItem = itemService.findOne(savedItemId);

        //then
        Assertions.assertEquals("testItem", foundItem.getName());
    }
}