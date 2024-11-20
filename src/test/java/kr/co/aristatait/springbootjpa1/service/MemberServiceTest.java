package kr.co.aristatait.springbootjpa1.service;

import kr.co.aristatait.springbootjpa1.domain.Member;
import kr.co.aristatait.springbootjpa1.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("aris");

        //when
        Long savedMemberId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedMemberId));
    }

    @Test
    public void 회원가입_중복체크() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("aris");

        Member member2 = new Member();
        member2.setName("aris");

        //when
        memberService.join(member1);

//        try {
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member2), "중복된 멤버를 등록할 수 없습니다.");
    }
}