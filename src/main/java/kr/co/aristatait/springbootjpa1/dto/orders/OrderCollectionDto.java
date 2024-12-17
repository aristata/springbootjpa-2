package kr.co.aristatait.springbootjpa1.dto.orders;

import kr.co.aristatait.springbootjpa1.domain.Address;
import kr.co.aristatait.springbootjpa1.domain.Order;
import kr.co.aristatait.springbootjpa1.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderCollectionDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderCollectionDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember()
                         .getName();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery()
                            .getAddress();
        this.orderItems = order.getOrderItems()
                               .stream()
                               .map(OrderItemDto::new)
                               .collect(Collectors.toList());
    }
}
