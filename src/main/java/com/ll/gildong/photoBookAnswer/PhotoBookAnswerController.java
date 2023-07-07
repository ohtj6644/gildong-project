package com.ll.gildong.photoBookAnswer;


import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswer;
import com.ll.gildong.FreeNoticeAnswer.FreeNoticeAnswerForm;
import com.ll.gildong.User.SiteUser;
import com.ll.gildong.User.UserService;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.photoBook.PhotoBook;
import com.ll.gildong.photoBook.PhotoBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;



@RequestMapping("/photobookanswer")
@Controller
@RequiredArgsConstructor
public class PhotoBookAnswerController {

    private final PhotoBookAnswerService photoBookAnswerService;

    private final PhotoBookService photoBookService;
    private final UserService userService;



    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid FreeNoticeAnswerForm freeNoticeAnswerForm, BindingResult bindingResult, Principal principal) {
        PhotoBook article = this.photoBookService.getPhotoBook(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "PhotoBook_detail";
        }
        this.photoBookAnswerService.create(article, freeNoticeAnswerForm.getContent(), siteUser);
        return String.format("redirect:/photobook/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        PhotoBookAnswer answer = this.photoBookAnswerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.photoBookAnswerService.delete(answer);
        return String.format("redirect:/photobook/detail/%s", answer.getPhotoBook().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid PhotoBookAnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        PhotoBookAnswer answer = this.photoBookAnswerService.getAnswer(id);
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/photobook/detail/%s", answer.getPhotoBook().getId());
        }
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.photoBookAnswerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/photobook/detail/%s", answer.getPhotoBook().getId());
    }
}
