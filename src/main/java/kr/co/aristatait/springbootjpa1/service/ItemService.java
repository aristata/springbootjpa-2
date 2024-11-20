package kr.co.aristatait.springbootjpa1.service;

import kr.co.aristatait.springbootjpa1.domain.item.Item;
import kr.co.aristatait.springbootjpa1.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item foundItem = itemRepository.findOne(itemId);
        foundItem.setName(name);
        foundItem.setPrice(price);
        foundItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
