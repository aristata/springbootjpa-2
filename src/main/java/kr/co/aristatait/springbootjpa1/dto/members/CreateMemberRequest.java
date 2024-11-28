package kr.co.aristatait.springbootjpa1.dto.members;

import lombok.*;

@Data
public class CreateMemberRequest {
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
