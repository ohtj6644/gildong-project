package com.ll.gildong.freeNotice;


import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswer;
import com.ll.gildong.User.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class FreeNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 50)
    private String subject;

    private LocalDate createDate;

    private LocalDate modifyDate;


    @Column(columnDefinition = "text")
    private String content;

    private int viewCount;

    private String category;


    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<FreeNoticeAnswer> answerList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private SiteUser author;





    @ElementCollection
    @CollectionTable(name = "freeNotice_filenames", joinColumns = @JoinColumn(name = "freeNotice_id"))
    @Column(name = "filename")
    private List<String> filenames;

    @ElementCollection
    @CollectionTable(name = "freeNotice_filepaths", joinColumns = @JoinColumn(name = "freeNotice_id"))
    @Column(name = "filepath")
    private List<String> filepaths;

}
