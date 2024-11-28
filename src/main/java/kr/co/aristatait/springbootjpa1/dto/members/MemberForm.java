package kr.co.aristatait.springbootjpa1.dto.members;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수값 입니다.")
    private String name;

    private String city;

    private String street;

    private String zipcode;
}
