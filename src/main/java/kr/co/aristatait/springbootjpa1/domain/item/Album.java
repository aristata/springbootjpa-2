package kr.co.aristatait.springbootjpa1.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("앨범")
@Getter @Setter
public class Album extends Item {

    private String artist;

    private String etc;

}
