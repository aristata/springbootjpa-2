package kr.co.aristatait.springbootjpa1.repository;

import jakarta.persistence.EntityManager;
import kr.co.aristatait.springbootjpa1.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // 스프링에서 알아서 JPA 의 em 을 주입해준다
//    @PersistenceContext
//    private EntityManager em;
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                 .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                 .setParameter("name", name)
                 .getResultList();
    }
}
