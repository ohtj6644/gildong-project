package com.ll.gildong.photoBookAnswer;


import com.ll.gildong.DataNotFoundException;
import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswer;
import com.ll.gildong.User.SiteUser;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.photoBook.PhotoBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoBookAnswerService {

    private final PhotoBookAnswerRepository photoBookAnswerRepository;


    public void create(PhotoBook photoBook, String content, SiteUser author) {
        PhotoBookAnswer answer = new PhotoBookAnswer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setPhotoBook(photoBook);
        answer.setAuthor(author);
        this.photoBookAnswerRepository.save(answer);
    }

    public PhotoBookAnswer getAnswer(Integer id) {
        Optional<PhotoBookAnswer> answer = this.photoBookAnswerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(PhotoBookAnswer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.photoBookAnswerRepository.save(answer);
    }

    public void delete(PhotoBookAnswer answer) {
        this.photoBookAnswerRepository.delete(answer);
    }


}
