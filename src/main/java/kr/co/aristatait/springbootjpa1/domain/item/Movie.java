package kr.co.aristatait.springbootjpa1.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@DiscriminatorValue("영화")
public class Movie extends Item {

    private String director;

    private String actor;

}
