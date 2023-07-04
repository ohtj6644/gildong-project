package com.ll.gildong.FreeNoticeAnswer;


import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.ll.gildong.DataNotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FreeNoticeAnswerService {
    private final FreeNoticeAnswerRepository freeNoticeAnswerRepository;


    public void create(FreeNotice freeNotice, String content, SiteUser author) {
        FreeNoticeAnswer answer = new FreeNoticeAnswer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setArticle(freeNotice);
        answer.setAuthor(author);
        this.freeNoticeAnswerRepository.save(answer);
    }

    public FreeNoticeAnswer getAnswer(Integer id) {
        Optional<FreeNoticeAnswer> answer = this.freeNoticeAnswerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(FreeNoticeAnswer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.freeNoticeAnswerRepository.save(answer);
    }

    public void delete(FreeNoticeAnswer answer) {
        this.freeNoticeAnswerRepository.delete(answer);
    }

}