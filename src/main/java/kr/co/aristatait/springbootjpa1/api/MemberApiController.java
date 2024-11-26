package kr.co.aristatait.springbootjpa1.api;

import jakarta.validation.*;
import kr.co.aristatait.springbootjpa1.domain.*;
import kr.co.aristatait.springbootjpa1.dto.*;
import kr.co.aristatait.springbootjpa1.service.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

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
}
