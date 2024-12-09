package kr.co.aristatait.springbootjpa1.api;

import kr.co.aristatait.springbootjpa1.domain.Order;
import kr.co.aristatait.springbootjpa1.dto.OrderSearch;
import kr.co.aristatait.springbootjpa1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * xToOne(ManyToOne, OneToOne)
     * Order
     * Order -> Member
     * Order -> OrderItems
     * Order -> Delivery
     * <p>
     * 1차 이슈
     * - 무한 루프
     * - Order -> Member -> List<Order> -> Member -> ...
     * - 이를 방지하기 위해 양방향 연관관계에 @JsonIgnore 를 붙여야 한다
     * <p>
     * 2차 이슈
     * - 하이버네이트5모듈
     * - 최신 라이브러리에서는 포함되어 있지만, 예전 라이브러리에서는 수동으로 넣어줘야 했다
     * - Lazy Loading 이 적용되어 있는 경우, 해당 엔티티를 프록시 객체로 만들어 일단 할당하는데, 이때 에러가 터짐
     * - 모듈의 설정으로 강제로 모든 레이지 로딩을 초기화하거나
     * - 반복문을 사용해서 필요한 엔티티만 강제로 레이지 초기화를 진행하는 방법으로 해결할 수 있음
     * <p>
     * 이런 이슈들은 근본적으로 엔티티를 반환하려고 하기 때문에 생기는 이슈
     */
    @GetMapping("/v1/orders")
    public List<Order> ordersV1() {
        return orderRepository.findAll(new OrderSearch());
    }

    // 지연로딩을 피하기 위해 즉시로딩을 사용하는건 좋은 방법이 아니다
}
