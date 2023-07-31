package com.ll.gildong.freeNotice;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ll.gildong.DataNotFoundException;
import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswer;
import com.ll.gildong.User.SiteUser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,20,Sort.by(sorts));
        Specification<FreeNotice> spec = search(kw);
        return this.freeNoticeRepository.findAll(spec,pageable);
    }

    public Page<FreeNotice> getList(int page, String kw,String sortkey) {
        List<Sort.Order> sorts= new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,20,Sort.by(sorts));
        Specification<FreeNotice> spec = searchCategory(sortkey);
        return this.freeNoticeRepository.findAll(spec,pageable);
    }
    public Specification<FreeNotice> searchCategory(String sortkey) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Season 컬럼을 기준으로 검색 조건 생성
            if (sortkey != null) {
                Path<String> seasonPath = root.get("category");
                Predicate seasonPredicate = criteriaBuilder.equal(seasonPath, sortkey);
                predicates.add(seasonPredicate);
            }

            // 다른 조건들을 추가하고 싶다면 여기에 추가

            // 검색 조건들을 조합하여 최종 검색 조건 생성
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
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

    public void create(FreeNoticeForm freeNoticeForm, SiteUser user, MultipartFile[] files) throws IOException {
    String projectPath = "/home/file"; // 변경된 외부 경로

    List<String> filenames = new ArrayList<>();
    List<String> filepaths = new ArrayList<>();

    for (MultipartFile file : files) {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        String filePath = "/files/" + fileName;

        // 변경된 파일 저장 경로
        String savePath = Paths.get(projectPath, fileName).toString();
        Files.createDirectories(Paths.get(savePath).getParent());
        file.transferTo(Paths.get(savePath));

        filenames.add(fileName);
        filepaths.add(filePath);
    }

    FreeNotice article = new FreeNotice();
    article.setSubject(freeNoticeForm.getSubject());
    article.setContent(freeNoticeForm.getContent());
    article.setCreateDate(LocalDate.now());
    article.setAuthor(user);
    article.setCategory(freeNoticeForm.getCategory());
    article.setFilenames(filenames);
    article.setFilepaths(filepaths);
    this.freeNoticeRepository.save(article);
}




    public void modify(FreeNotice article, String subject, String content, String category) {
        article.setSubject(subject);
        article.setContent(content);
        article.setCategory(category);
        article.setModifyDate(LocalDate.now());
        this.freeNoticeRepository.save(article);

    }

    public void delete(FreeNotice article) {
        this.freeNoticeRepository.delete(article);
    }

    public Page<FreeNotice> getList5(int page, String kw) {
        List<Sort.Order> sorts= new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,5,Sort.by(sorts));
        Specification<FreeNotice> spec = search(kw);
        return this.freeNoticeRepository.findAll(pageable);
    }
}
