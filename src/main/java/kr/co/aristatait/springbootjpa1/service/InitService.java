package kr.co.aristatait.springbootjpa1.service;

import jakarta.persistence.*;
import kr.co.aristatait.springbootjpa1.domain.*;
import kr.co.aristatait.springbootjpa1.domain.item.*;
import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Component
@Transactional
@RequiredArgsConstructor
public class InitService {

    private final EntityManager entityManager;

    public void dbInit1() {
        Member member = createMember("테스트 사용자 1", "서울", "거리1", "12345");
        entityManager.persist(member);

        Book book1 = createBook("테스트 책 1", 1000, 100);
        entityManager.persist(book1);

        Book book2 = createBook("테스트 책 2", 2000, 50);
        entityManager.persist(book2);

        OrderItem orderItem1 = OrderItem.createOrderItem(book1, 1000, 1);
        OrderItem orderItem2 = OrderItem.createOrderItem(book2, 2000, 2);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
        entityManager.persist(order);
    }

    public void dbInit2() {
        Member member = createMember("테스트 사용자 2", "부산", "도로2", "22345");
        entityManager.persist(member);

        Book book1 = createBook("테스트 책 3", 3000, 150);
        entityManager.persist(book1);

        Book book2 = createBook("테스트 책 4", 4000, 500);
        entityManager.persist(book2);

        OrderItem orderItem1 = OrderItem.createOrderItem(book1, 3000, 3);
        OrderItem orderItem2 = OrderItem.createOrderItem(book2, 4000, 4);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
        entityManager.persist(order);
    }

    private Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        return book;
    }
}
