package kr.co.aristatait.springbootjpa1.api;

import jakarta.validation.*;
import kr.co.aristatait.springbootjpa1.domain.*;
import kr.co.aristatait.springbootjpa1.dto.*;
import kr.co.aristatait.springbootjpa1.dto.members.*;
import kr.co.aristatait.springbootjpa1.service.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse postMember(
            @RequestBody @Valid Member member
    ) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse postMemberV2(
            @RequestBody @Valid CreateMemberRequest request
    ) {
        Member member = new Member();
        member.setName(request.getName());
        member.setAddress(new Address(request.getCity(), request.getStreet(), request.getZipcode()));

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{memberId}")
    public UpdateMemberResponse putMember(
            @RequestBody @Valid UpdateMemberRequest request,
            @PathVariable Long memberId
    ) {

        // 커맨드(등록,수정,삭제)와 쿼리(조회)를 분리한다 는 원칙
        memberService.update(memberId, request.getName());
        Member foundMember = memberService.findOne(memberId);
        return new UpdateMemberResponse(foundMember.getId(), foundMember.getName());
    }

    /* Entity 를 Response 로 사용하면 안되는 이유
     * 1. API 마다 요구하는 Response 가 다를 수 있다
     * 2. Entity 가 변경될 때 API Spec 이 같이 바뀐다
     * 3. Entity 의 모든 값이 노출된다
     * 4. 응답 스펙을 맞추기 위해 로직이 추가된다 (@JsonIgnore, 별도의 뷰 로직 등)
     * */
    @GetMapping("/api/v1/members")
    public List<Member> getMembers() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result<List<ReadMemberResponse>> getMembersV2() {
        List<Member> foundMembers = memberService.findMembers();
        List<ReadMemberResponse> collect =
                foundMembers.stream()
                            .map(m -> new ReadMemberResponse(m.getId(), m.getName()))
                            .toList();

        return new Result<>(collect.size(), collect);
    }
}
