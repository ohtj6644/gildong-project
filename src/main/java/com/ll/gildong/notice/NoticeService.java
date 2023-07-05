package com.ll.gildong.notice;


import com.ll.gildong.DataNotFoundException;
import com.ll.gildong.User.SiteUser;
import com.ll.gildong.freeNotice.FreeNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;


    public Page<Notice> getList(int page) {
        List<Sort.Order> sorts= new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,20,Sort.by(sorts));
        return this.noticeRepository.findAll(pageable);

    }

    public void create(NoticeForm noticeForm, SiteUser siteUser) {
        Notice notice = new Notice();
        notice.setSubject(noticeForm.getSubject());
        notice.setContent(noticeForm.getContent());
        notice.setAuthor(siteUser);
        notice.setCreateDate(LocalDate.now());
        this.noticeRepository.save(notice);


    }

    public Notice getNotice(Integer id) {
        Optional<Notice> article = this.noticeRepository.findById(id);
        if (article.isPresent()) {
            return article.get();
        } else {
            throw new DataNotFoundException("freeNotice not found");
        }
    }


    public void viewCountUp(Notice article) {

        article.setViewCount(article.getViewCount() + 1);
        this.noticeRepository.save(article);
    }

    public void delete(Notice article) {

        this.noticeRepository.delete(article);
    }


    public void modify(Notice article, String subject, String content) {
        article.setSubject(subject);
        article.setContent(content);
        article.setModifyDate(LocalDate.now());
        this.noticeRepository.save(article);

    }


    public Page<Notice> getList5(int page) {
        List<Sort.Order> sorts= new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,5,Sort.by(sorts));
        return this.noticeRepository.findAll(pageable);
    }
}
