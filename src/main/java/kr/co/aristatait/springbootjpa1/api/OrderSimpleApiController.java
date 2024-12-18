package kr.co.aristatait.springbootjpa1.api;

import kr.co.aristatait.springbootjpa1.domain.Order;
import kr.co.aristatait.springbootjpa1.dto.orders.OrderSearch;
import kr.co.aristatait.springbootjpa1.dto.orders.SimpleOrderDto;
import kr.co.aristatait.springbootjpa1.dto.orders.SimpleOrderQueryDto;
import kr.co.aristatait.springbootjpa1.repository.orders.OrderCustomRepository;
import kr.co.aristatait.springbootjpa1.repository.orders.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderCustomRepository orderCustomRepository;

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
     * - 이때 그럼 EAGER 를 쓰면되지 하는 사람들도 있는데, 지연로딩을 피하기 위해 즉시로딩을 사용하는건 좋은 방법이 아니다
     * <p>
     * 이런 이슈들은 근본적으로 엔티티를 반환하려고 하기 때문에 생기는 이슈
     */
    @GetMapping("/v1/simple-orders")
    public List<Order> ordersV1() {
        return orderRepository.findAll(new OrderSearch());
    }

    /**
     * Entity 대신 Dto 를 반환하게 수정
     * - 이것 또한 문제가 아직 남아 있다
     * - N + 1 문제!! 사실 1 + N 문제라고 표현하는게 더 적합
     */
    @GetMapping("/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // 오더 목록 조회 = 쿼리 1번 실행
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        // 오더를 DTO 로 변환하는 과정에서 Lazy loading 초기화
        // 오더 목록 조회 결과가 2개라면 멤버 조회 2번, 딜리버리 조회 2번 실행됨
        List<SimpleOrderDto> result = orders.stream()
                                            .map(SimpleOrderDto::new)
                                            .toList();

        // 총 1 + 2 + 2 실행
        // 1 + N -> N + 1 문제라고 표현
        return result;
    }

    /**
     * N+1 문제를 해결하기 위해 페치 조인(fetch join) 을 사용한다
     * 페치 조인을 사용하면 레이지 로딩에 관계 없이 딱 한번의 쿼리만 발생한다
     */
    @GetMapping("/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        return orderRepository.findAllWithMemberAndDelivery()
                              .stream()
                              .map(SimpleOrderDto::new)
                              .toList();
    }

    /**
     * DTO 를 변환하는 과정을 생략해보자
     * 장단점이 있다
     * - 장점: 원하는 데이터만 선택해서 가져올 수 있다
     * 성능 최적화
     * - 단점: 재사용성이 떨어진다
     * 코드가 지저분하다
     */
    @GetMapping("/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4() {
        return orderCustomRepository.findAllWithDto();
    }

    /*
      쿼리 방식 선택 권장 순서
      1. 우선 엔티티를 DTO 로 변환하는 방법을 선택한다 (v2)
      2. 필요하면 페치 조인으로 성능을 최적화 한다 (v3)
      3. 그래도 안되면 DTO 로 직접 조회하는 방법을 사용한다 (v4)
      4. 최후의 방법은 JPA 가 제공하는 네이티브 SQL 이나 스프링 JDBC Template 을 사용해서 SQL을 직접 사용한다
     */
}
