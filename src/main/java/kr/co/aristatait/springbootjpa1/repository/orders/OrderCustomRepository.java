package kr.co.aristatait.springbootjpa1.repository.orders;

import jakarta.persistence.EntityManager;
import kr.co.aristatait.springbootjpa1.dto.orders.SimpleOrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 재활용성이 떨어지는 전용 쿼리의 경우 OrderRepository 와 분리된 파일에 작성한다
 * 이렇게 함으로써 다른 개발자에게 이 쿼리는 화면과 매칭되는 전용 쿼리라는것을 알려줄 수 있다
 */
@Repository
@RequiredArgsConstructor
public class OrderCustomRepository {

    private final EntityManager em;

    public List<SimpleOrderQueryDto> findAllWithDto() {
        return em.createQuery("""
                                  select new kr.co.aristatait.springbootjpa1.dto.orders.SimpleOrderQueryDto(
                                      o.id,
                                      m.name,
                                      o.orderDate,
                                      o.status,
                                      d.address
                                  )
                                  from Order o
                                  join o.member m
                                  join o.delivery d
                              """, SimpleOrderQueryDto.class)
                 .getResultList();
    }
}

