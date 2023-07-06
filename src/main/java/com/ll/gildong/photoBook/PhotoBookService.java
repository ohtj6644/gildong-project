package com.ll.gildong.photoBook;


import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswer;
import com.ll.gildong.User.SiteUser;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.freeNotice.FreeNoticeForm;
import com.ll.gildong.photoBookAnswer.PhotoBookAnswer;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoBookService {
    private final PhotoBookRepository photoBookRepository;
    private Specification<PhotoBook> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<PhotoBook> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<PhotoBook, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<PhotoBook, FreeNoticeAnswer> a = q.join("answerList", JoinType.LEFT);
                Join<PhotoBookAnswer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public Page<PhotoBook> getList(int page, String kw) {
        List<Sort.Order> sorts= new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,8,Sort.by(sorts));
        Specification<PhotoBook> spec = search(kw);
        return this.photoBookRepository.findAll(spec,pageable);
    }

    public void create(PhotoBookForm photoBookForm, SiteUser user, MultipartFile[] files) throws IOException {
        String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

        List<String> filenames = new ArrayList<>();
        List<String> filepaths = new ArrayList<>();

        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            String filePath = "/files/" + fileName;

            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            filenames.add(fileName);
            filepaths.add(filePath);
        }

        PhotoBook article = new PhotoBook();
        article.setSubject(photoBookForm.getSubject());
        article.setContent(photoBookForm.getContent());
        article.setCreateDate(LocalDate.now());
        article.setAuthor(user);
        article.setFilenames(filenames);
        article.setFilepaths(filepaths);
        this.photoBookRepository.save(article);
    }


}
