package kr.co.aristatait.springbootjpa1.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class Result<T> {
    private int count;
    private T data;
}
