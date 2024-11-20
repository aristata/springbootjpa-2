package kr.co.aristatait.springbootjpa1.repository;

import jakarta.persistence.EntityManager;
import kr.co.aristatait.springbootjpa1.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    /**
     * 상품 저장
     */
    public Long save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }

        return item.getId();
    }

    /**
     * 상품 하나 조회
     */
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    /**
     * 상품 목록 조회
     */
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                 .getResultList();
    }
}
