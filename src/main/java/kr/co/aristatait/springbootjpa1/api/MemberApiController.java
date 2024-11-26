package kr.co.aristatait.springbootjpa1.api;

import jakarta.validation.Valid;
import kr.co.aristatait.springbootjpa1.domain.Address;
import kr.co.aristatait.springbootjpa1.domain.Member;
import kr.co.aristatait.springbootjpa1.dto.CreateMemberRequest;
import kr.co.aristatait.springbootjpa1.dto.CreateMemberResponse;
import kr.co.aristatait.springbootjpa1.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
