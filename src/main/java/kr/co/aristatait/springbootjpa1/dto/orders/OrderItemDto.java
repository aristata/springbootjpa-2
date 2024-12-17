package kr.co.aristatait.springbootjpa1.dto.orders;

import kr.co.aristatait.springbootjpa1.domain.OrderItem;
import lombok.Data;

@Data
public class OrderItemDto {

    private String itemName;
    private int itemPrice;
    private int itemQuantity;

    public OrderItemDto(OrderItem orderItem) {
        this.itemName = orderItem.getItem()
                                 .getName();
        this.itemPrice = orderItem.getOrderPrice();
        this.itemQuantity = orderItem.getCount();
    }
}
