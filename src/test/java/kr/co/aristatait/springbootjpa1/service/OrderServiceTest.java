package kr.co.aristatait.springbootjpa1.service;

import jakarta.persistence.EntityManager;
import kr.co.aristatait.springbootjpa1.domain.Address;
import kr.co.aristatait.springbootjpa1.domain.Member;
import kr.co.aristatait.springbootjpa1.domain.Order;
import kr.co.aristatait.springbootjpa1.domain.OrderStatus;
import kr.co.aristatait.springbootjpa1.domain.item.Book;
import kr.co.aristatait.springbootjpa1.domain.item.Item;
import kr.co.aristatait.springbootjpa1.dto.orders.OrderSearch;
import kr.co.aristatait.springbootjpa1.exception.NotEnoughStockException;
import kr.co.aristatait.springbootjpa1.repository.orders.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("부산 JPA", 15000, 100);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태값은 ORDER");
        assertEquals(1, getOrder.getOrderItems()
                                .size(), "주문한 상품 종류 수가 정확해야 한다");
        assertEquals(15000 * 2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량 이다");
        assertEquals(98, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("aris");
        member.setAddress(new Address("부산", "중앙대로", "1616"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;

        //when
        //then
        Assertions.assertThrows(
                NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount),
                "재고 수량 부족 예외가 발생해야 한다."
        );

    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다");
        Assertions.assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다");

    }


    @Test
    public void 주문검색() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("부산 JPA", 15000, 100);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setOrderStatus(OrderStatus.ORDER);
        orderSearch.setMemberName("aris");
        List<Order> getOrders = orderRepository.findAll(orderSearch);

        //then
        Assertions.assertEquals("aris", getOrders.get(0)
                                                 .getMember()
                                                 .getName(), "발견된 회원 이름은 aris 여야 합니다.");

    }
}