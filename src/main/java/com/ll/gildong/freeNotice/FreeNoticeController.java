package com.ll.gildong.freeNotice;


import com.ll.gildong.User.SiteUser;
import com.ll.gildong.User.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class FreeNoticeController {

    private  final  FreeNoticeService freeNoticeService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/freenotice/create")
    public String createFreeNotice(FreeNoticeForm freeNoticeForm){

        return "FreeNotice_form";
    }

    @PostMapping("/freenotice/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(@Valid FreeNoticeForm freeNoticeForm,
                                BindingResult bindingResult,
                                Principal principal,
                                @RequestParam("files") MultipartFile[] files) throws Exception {
        if (bindingResult.hasErrors()) {
            return "FreeNotice_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.freeNoticeService.create(freeNoticeForm, siteUser, files);
        return "redirect:/freenotice/list";
    }

    @GetMapping("/freenotice/list")
    public String freeNoticeList(Model model , @RequestParam(value = "page",defaultValue = "0")int page ,
                                 @RequestParam(value = "kw",defaultValue = "")String kw){
        Page<FreeNotice> paging = this.freeNoticeService.getList(page,kw);
        int FreeNoticeCount = paging.getNumberOfElements();

        model.addAttribute("paging",paging);
        model.addAttribute("FreeNoticeCount",FreeNoticeCount);
        return "FreeNotice_list";
    }


    @GetMapping(value = "freenotice/detail/{id}")
    public String articleDetail(Principal principal, Model model, @PathVariable("id") Integer id) {
        FreeNotice article = this.freeNoticeService.getFreeNotice(id);
        boolean checkedLike = false;
        this.freeNoticeService.viewCountUp(article);



        model.addAttribute("checkedLike", checkedLike);
        model.addAttribute("article", article);
        return "FreeNotice_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/freenotice/modify/{id}")
    public String articleModify(FreeNoticeForm articleForm, @PathVariable("id") Integer id, Principal principal) {
        FreeNotice article = this.freeNoticeService.getFreeNotice(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        articleForm.setSubject(article.getSubject());
        articleForm.setContent(article.getContent());
        articleForm.setCategory(article.getCategory());
        return "FreeNotice_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/freenotice/modify/{id}")
    public String articleModify(@Valid FreeNoticeForm articleForm, BindingResult bindingResult,
                                Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "FreeNotice_form";
        }
        FreeNotice article = this.freeNoticeService.getFreeNotice(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.freeNoticeService.modify(article, articleForm.getSubject(), articleForm.getContent(), articleForm.getCategory());
        return String.format("redirect:/freenotice/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/freenotice/delete/{id}")
    public String articleDelete(Principal principal, @PathVariable("id") Integer id) {
        FreeNotice article = this.freeNoticeService.getFreeNotice(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.freeNoticeService.delete(article);
        return "redirect:/article/list";
    }



}
