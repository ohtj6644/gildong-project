package com.ll.gildong.freeNotice;


import com.ll.gildong.User.SiteUser;
import com.ll.gildong.User.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class FreeNoticeController {

    private  final  FreeNoticeService freeNoticeService;
    private final UserService userService;


    @GetMapping("/freenotice/create")
    public String createFreeNotice(){
        return "FreeNotice_form";
    }

    @PostMapping("/freenotice/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(@Valid FreeNoticeForm freeNoticeForm,
                                BindingResult bindingResult,
                                Principal principal,
                                @RequestParam("files") MultipartFile[] files) throws Exception {
        if (bindingResult.hasErrors()) {
            return "article_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.freeNoticeService.create(freeNoticeForm, siteUser, files);
        return "redirect:/article/list";
    }

    @GetMapping("freenotice/list")
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
        if (principal != null) {
            SiteUser siteUser = this.userService.getUser(principal.getName());
            for (SiteUser voter : article.getVoter()) {
                if (voter.getId() == siteUser.getId()) {
                    checkedLike = true;
                }
            }
        }


        model.addAttribute("checkedLike", checkedLike);
        model.addAttribute("article", article);
        return "article_detail";
    }


}
