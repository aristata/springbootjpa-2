package kr.co.aristatait.springbootjpa1.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("도서")
public class Book extends Item {

    private String author;

    private String isbn;

}
