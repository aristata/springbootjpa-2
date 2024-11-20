package kr.co.aristatait.springbootjpa1.domain.item;

import jakarta.persistence.*;
import kr.co.aristatait.springbootjpa1.domain.Category;
import kr.co.aristatait.springbootjpa1.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    // 예제 샘플로 만들었지만, 실무에서는 거의 사용 안함
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /*
     * 엔티티 자체적으로 해결할 수 있는 비즈니스 로직은 엔티티 내부에 작성하는게 객체지향적이다
     * - Setter 를 사용하기보단 이와 같이 객체 내부의 비즈니스 로직을 활용하여 객체를 수정하는게 좋다
     * */

    /**
     * 재고 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
