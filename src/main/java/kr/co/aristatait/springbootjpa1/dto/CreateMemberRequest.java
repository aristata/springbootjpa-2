package kr.co.aristatait.springbootjpa1.dto;

import lombok.Data;

@Data
public class CreateMemberRequest {
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
