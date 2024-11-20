package kr.co.aristatait.springbootjpa1.service;

import kr.co.aristatait.springbootjpa1.domain.Member;
import kr.co.aristatait.springbootjpa1.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

/*
  1. filed injection
  권장하지 않는다고 경고가 나옴

    @Autowired
    private final MemberRepository memberRepository;
 */

/*
  2. setter injection
  단점: 애플리케이션 동작중에 바꿀일은 사실 없음

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

 */

/*
  3. 생성자 injection

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    참고로 @Autowired 생략해도 스프링이 알아서 주입해줌
 */

/*
  4. lombok 을 사용할 경우

  @AllArgsConstructor -> 모든 필드로 생성자를 자동으로 만들어 줌
  @RequiredArgsConstructor -> 모든 필수 필드로 생성자를 자동으로 만들어 줌
    여기서 필수 필드는 final 붙은 상수들

    롬복을 사용할 경우 다음과 같이 상수 필드만 선언하고 어노테이션만 붙여주면 초간단
 */
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        // 중복 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 한명 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


}
