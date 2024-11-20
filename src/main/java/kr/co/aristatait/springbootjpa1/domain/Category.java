package kr.co.aristatait.springbootjpa1.domain;

import jakarta.persistence.*;
import kr.co.aristatait.springbootjpa1.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    // 중간 테이블 설정
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 연관관계 편의 메서드 - 양방향 연관관계 일때 쓰면 편하다
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
