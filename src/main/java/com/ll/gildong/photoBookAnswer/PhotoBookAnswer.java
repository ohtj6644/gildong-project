package com.ll.gildong.photoBookAnswer;


import com.ll.gildong.User.SiteUser;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.photoBook.PhotoBook;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PhotoBookAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "text")
    private String content;


    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne
    private PhotoBook photoBook;

    @ManyToOne
    private SiteUser author;

}
