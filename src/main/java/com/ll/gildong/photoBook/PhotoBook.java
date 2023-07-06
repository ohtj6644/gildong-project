package com.ll.gildong.photoBook;


import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswer;
import com.ll.gildong.User.SiteUser;
import com.ll.gildong.photoBookAnswer.PhotoBookAnswer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class PhotoBook {

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



    @OneToMany(mappedBy = "photoBook", cascade = CascadeType.REMOVE)
    private List<PhotoBookAnswer> answerList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private SiteUser author;





    @ElementCollection
    @CollectionTable(name = "photoBook_filenames", joinColumns = @JoinColumn(name = "photoBook_id"))
    @Column(name = "filename")
    private List<String> filenames;

    @ElementCollection
    @CollectionTable(name = "photoBook_filepaths", joinColumns = @JoinColumn(name = "photoBook_id"))
    @Column(name = "filepath")
    private List<String> filepaths;
}
