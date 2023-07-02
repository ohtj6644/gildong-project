package com.ll.gildong.freeNotice;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class FreeNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 50)
    private String subject;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;


    @Column(columnDefinition = "text")
    private String content;

    private int viewCount;

    private String category;


//    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
//    private List<Answer> answerList;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "author_id")
//    private SiteUser author;

    private int likeCount;

//    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
//    private List<Answer> answerList;

}
