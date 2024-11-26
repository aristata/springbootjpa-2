package kr.co.aristatait.springbootjpa1.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class UpdateMemberResponse {
    private Long id;
    private String name;
}
