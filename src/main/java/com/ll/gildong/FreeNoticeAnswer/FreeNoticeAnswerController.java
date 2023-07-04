package com.ll.gildong.FreeNoticeAnswer;

import com.ll.gildong.User.SiteUser;
import com.ll.gildong.User.UserService;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.freeNotice.FreeNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/freenoticeanswer")
@RequiredArgsConstructor
@Controller
public class FreeNoticeAnswerController {

    private final FreeNoticeService freeNoticeService;
    private final FreeNoticeAnswerService freeNoticeAnswerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid FreeNoticeAnswerForm freeNoticeAnswerForm, BindingResult bindingResult, Principal principal) {
        FreeNotice article = this.freeNoticeService.getFreeNotice(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "FreeNotice_detail";
        }
        this.freeNoticeAnswerService.create(article, freeNoticeAnswerForm.getContent(), siteUser);
        return String.format("redirect:/freenotice/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        FreeNoticeAnswer answer = this.freeNoticeAnswerService.getAnswer(id);
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.freeNoticeAnswerService.delete(answer);
        return String.format("redirect:/freenotice/detail/%s", answer.getArticle().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid FreeNoticeAnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        FreeNoticeAnswer answer = this.freeNoticeAnswerService.getAnswer(id);
        if (bindingResult.hasErrors()) {
            return String.format("redirect:/freenotice/detail/%s", answer.getArticle().getId());
        }
        if (!answer.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.freeNoticeAnswerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/freenotice/detail/%s", answer.getArticle().getId());
    }

}