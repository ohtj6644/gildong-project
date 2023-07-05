package com.ll.gildong.notice;


import com.ll.gildong.User.SiteUser;
import com.ll.gildong.User.UserService;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.freeNotice.FreeNoticeForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final UserService userService;


    @GetMapping("/notice/list")
    public  String noticeList(Model model, @RequestParam(value = "page",defaultValue = "0")int page ){
        Page<Notice> paging = this.noticeService.getList(page);
        int FreeNoticeCount = paging.getNumberOfElements();

        model.addAttribute("paging",paging);
        model.addAttribute("FreeNoticeCount",FreeNoticeCount);
        return "Notice_list";

    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notice/create")
    public String createFreeNotice(NoticeForm noticeForm){

        return "Notice_form";
    }

    @PostMapping("/notice/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(@Valid NoticeForm noticeForm,
                                BindingResult bindingResult,
                                Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Notice_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.noticeService.create(noticeForm, siteUser);
        return "redirect:/notice/list";
    }


    @GetMapping(value = "notice/detail/{id}")
    public String articleDetail(Principal principal, Model model, @PathVariable("id") Integer id) {
        Notice article = this.noticeService.getNotice(id);
        boolean checkedLike = false;
        this.noticeService.viewCountUp(article);



        model.addAttribute("checkedLike", checkedLike);
        model.addAttribute("article", article);
        return "Notice_detail";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notice/delete/{id}")
    public String articleDelete(Principal principal, @PathVariable("id") Integer id) {
        Notice article = this.noticeService.getNotice(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.noticeService.delete(article);
        return "redirect:/notice/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notice/modify/{id}")
    public String articleModify(NoticeForm articleForm, @PathVariable("id") Integer id, Principal principal) {
        Notice article = this.noticeService.getNotice(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        articleForm.setSubject(article.getSubject());
        articleForm.setContent(article.getContent());
        return "Notice_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/notice/modify/{id}")
    public String articleModify(@Valid FreeNoticeForm articleForm, BindingResult bindingResult,
                                Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "Notice_form";
        }
        Notice article = this.noticeService.getNotice(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.noticeService.modify(article, articleForm.getSubject(), articleForm.getContent());
        return String.format("redirect:/notice/detail/%s", id);
    }



}
