package kr.co.aristatait.springbootjpa1.dto.members;

import lombok.*;

@Data
public class CreateMemberResponse {
    private Long id;
    public CreateMemberResponse(Long id) {
        this.id = id;
    }
}
