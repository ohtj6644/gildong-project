package com.ll.gildong.freeNotice;


import com.ll.gildong.DataNotFoundException;
import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswer;
import com.ll.gildong.User.SiteUser;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FreeNoticeService {

    private  final FreeNoticeRepository freeNoticeRepository;

    private Specification<FreeNotice> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<FreeNotice> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<FreeNotice, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<FreeNotice, FreeNoticeAnswer> a = q.join("answerList", JoinType.LEFT);
                Join<FreeNoticeAnswer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }


    public Page<FreeNotice> getList(int page, String kw) {
        List<Sort.Order> sorts= new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,15,Sort.by(sorts));
        Specification<FreeNotice> spec = search(kw);
        return this.freeNoticeRepository.findAll(pageable);
    }


    public void create(FreeNoticeForm articleForm, SiteUser user, MultipartFile[] files) throws IOException {
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

        FreeNotice article = new FreeNotice();
        article.setSubject(articleForm.getSubject());
        article.setContent(articleForm.getContent());
        article.setCreateDate(LocalDateTime.now());
        article.setAuthor(user);
        article.setFilenames(filenames);
        article.setFilepaths(filepaths);
        this.freeNoticeRepository.save(article);
    }



    public FreeNotice getFreeNotice(Integer id) {
        Optional<FreeNotice> article = this.freeNoticeRepository.findById(id);
        if (article.isPresent()) {
            return article.get();
        } else {
            throw new DataNotFoundException("freeNotice not found");
        }
    }

    public void viewCountUp(FreeNotice article) {

        article.setViewCount(article.getViewCount() + 1);
        this.freeNoticeRepository.save(article);
    }

}
