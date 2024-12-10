package kr.co.aristatait.springbootjpa1.service;

import kr.co.aristatait.springbootjpa1.domain.Delivery;
import kr.co.aristatait.springbootjpa1.domain.Member;
import kr.co.aristatait.springbootjpa1.domain.Order;
import kr.co.aristatait.springbootjpa1.domain.OrderItem;
import kr.co.aristatait.springbootjpa1.domain.item.Item;
import kr.co.aristatait.springbootjpa1.dto.orders.OrderSearch;
import kr.co.aristatait.springbootjpa1.repository.ItemRepository;
import kr.co.aristatait.springbootjpa1.repository.MemberRepository;
import kr.co.aristatait.springbootjpa1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        // 주문 아이디 반환
        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {

        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancel();

        /* JPA 에서 dirty checking 을 실행하여 변경된 엔티티를 알아서 디비에 반영해 주기 때문에
         우리가 직접 업데이트 쿼리를 날릴 필요가 없다 */
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
