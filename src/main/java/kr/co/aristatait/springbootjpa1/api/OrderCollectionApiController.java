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

    /**
     * v3 컬렉션 페치 조인
     * - 페치 조인을 사용하였기 때문에 SQL 이 1번만 실행되어 성능이 최적화 된다
     * - distinct 를 사용한 이유는 일대다 조인이 포함되어 있기 때문이다
     * - SQL 에서 일대다 관계의 테이블을 조인하여 쿼리를 실행하면 곱연산이 이뤄진다 = 결과물이 뻥튀기 된다
     * - SQL 에서는 distinct 를 넣어도 사실 모든 컬럼이 일치하지 않는 한 중복제거가 이뤄지지 않는다.
     * 하지만 JPA 에서는 distinct 를 넣으면, 같은 엔티티일 경우에 중복을 제거해 준다
     * - 컬렉션 페치 조인의 단점은 페이징이 불가능 한 점이다
     * 컬렉션 페치 조인이 포함된 쿼리를 페이징하려고 하면 하이버네이트는 경고 로그를 남기면서 모든 데이터를 디비에서 읽어오고,
     * 메모리에서 페이징 해버린다. 이는 매우 위험하다
     * - 또 컬렉션 페치 조인은 1개의 컬렉션에만 사용할 수 있다. 컬렉션 둘 이상에 페치 조인을 사용하면 데이터가 부정합하게 조회 될 수 있다.
     */
    @GetMapping("/v3/collection-orders")
    public List<OrderCollectionDto> ordersV3() {
        return orderRepository.findAllWithMemberAndDeliveryAndOderItems()
                              .stream()
                              .map(OrderCollectionDto::new)
                              .toList();
    }
}
