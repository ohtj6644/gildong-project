package com.ll.gildong.FreeNoticeAnswer;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.User.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
public class FreeNoticeAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "text")
    private String content;


    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private FreeNotice article;

    @ManyToOne
    private SiteUser author;



}