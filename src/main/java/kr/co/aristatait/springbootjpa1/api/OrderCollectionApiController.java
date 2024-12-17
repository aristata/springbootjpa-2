package kr.co.aristatait.springbootjpa1.api;

import kr.co.aristatait.springbootjpa1.domain.Order;
import kr.co.aristatait.springbootjpa1.domain.OrderItem;
import kr.co.aristatait.springbootjpa1.dto.orders.OrderCollectionDto;
import kr.co.aristatait.springbootjpa1.dto.orders.OrderSearch;
import kr.co.aristatait.springbootjpa1.repository.orders.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 주문 상품 정보 조회
 * 일대다 관계 (OneToMany) 를 조회하고 최적화 하는 방법
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderCollectionApiController {

    private final OrderRepository orderRepository;

    /**
     * v1 엔티티 직접 노출
     * - 엔티티가 변하면 API 스펙이 변한다
     * - 트랜잭션 안에서 지연 로딩이 필요하다
     * - 양방향 연관관계 문제
     */
    @GetMapping("/v1/collection-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember()
                 .getName();
            order.getDelivery()
                 .getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.getItem()
                                                     .getName());
        }
        return all;
    }

    /**
     * v2 엔티티 대신 DTO 로 변환
     * - orderItem 까지 DTO 로 작성해 줘야 함
     * - n + 1 문제 때문에 너무 많은 SQL 이 발생하는 단점이 있음
     */
    @GetMapping("/v2/collection-orders")
    public List<OrderCollectionDto> ordersV2() {
        return orderRepository.findAllByString(new OrderSearch())
                              .stream()
                              .map(OrderCollectionDto::new)
                              .toList();
    }
}
