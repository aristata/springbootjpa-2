package kr.co.aristatait.springbootjpa1.repository.orders;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import kr.co.aristatait.springbootjpa1.domain.Order;
import kr.co.aristatait.springbootjpa1.domain.*;
import kr.co.aristatait.springbootjpa1.dto.orders.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static kr.co.aristatait.springbootjpa1.domain.QMember.member;
import static kr.co.aristatait.springbootjpa1.domain.QOrder.order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    /**
     * 주문 저장
     */
    public void save(Order order) {
        em.persist(order);
    }

    /**
     * 주문 단건 조회
     */
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    /**
     * 동적쿼리를 처리하는 첫번째 방법
     * => JPQL 로 처리
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /**
     * 동적쿼리를 처리하는 두번째 방법
     * => JPA Criteria로 처리
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq)
                .setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    /**
     * 동적쿼리를 처리하는 가장 좋은 방법
     * => QueryDsl 을 사용
     * 주문 목록 조회
     * - 필터 포함
     * - 주문자 명
     * - 주문 상태
     */
    public List<Order> findAll(OrderSearch orderSearch) {

        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }


    private BooleanExpression statusEq(OrderStatus status) {

        if (status == null) {
            return null;
        }
        return order.status.eq(status);
    }

    private BooleanExpression nameLike(String name) {

        if (!StringUtils.hasText(name)) {
            return null;
        }
        return member.name.like(name);
    }

    /**
     * 페치 조인 최적화
     */
    public List<Order> findAllWithMemberAndDelivery() {
        return em.createQuery("""
                            select o
                            from Order o
                            join fetch o.member m
                            join fetch o.delivery d
                        """, Order.class)
                .getResultList();
    }

    /**
     * 컬렉션 페치 조인 최적화
     */
    public List<Order> findAllWithMemberAndDeliveryAndOderItems() {
        return em.createQuery("""
                              select distinct o
                              from Order o
                              join fetch o.member m
                              join fetch o.delivery d
                              join fetch o.orderItems oi
                              join fetch oi.item i
                        """, Order.class)
                .getResultList();
    }

    /**
     * 컬렉션 페치 조인 한계돌파
     */
    public List<Order> findAllWithMemberAndDeliveryAndPaging(int offset, int limit) {
        return em.createQuery("""
                            select o
                            from Order o
                            join fetch o.member m
                            join fetch o.delivery d
                        """, Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
