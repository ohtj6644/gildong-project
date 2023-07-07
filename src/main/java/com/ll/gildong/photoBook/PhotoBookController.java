package com.ll.gildong.photoBook;


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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class PhotoBookController {
    private final PhotoBookService photoBookService;
    private final UserService userService;

    @GetMapping("/photobook/list")
    public String photobookList(Model model , @RequestParam(value = "page",defaultValue = "0")int page ,
                                 @RequestParam(value = "kw",defaultValue = "")String kw){
        Page<PhotoBook> paging = this.photoBookService.getList(page,kw);
        int PhotoBookCount = paging.getNumberOfElements();

        model.addAttribute("paging",paging);
        model.addAttribute("PhotoBookCount",PhotoBookCount);
        return "PhotoBook_list";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/photobook/create")
    public String createPhotoBook(PhotoBookForm photoBookForm){

        return "PhotoBook_form";
    }

    @PostMapping("/photobook/create")
    @PreAuthorize("isAuthenticated()")
    public String articleCreate(@Valid PhotoBookForm photoBookForm,
                                BindingResult bindingResult,
                                Principal principal,
                                @RequestParam("files") MultipartFile[] files) throws Exception {
        if (bindingResult.hasErrors()) {
            return "PhotoBook_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.photoBookService.create(photoBookForm, siteUser, files);
        return "redirect:/photobook/list";
    }


    @GetMapping(value = "photobook/detail/{id}")
    public String articleDetail(Principal principal, Model model, @PathVariable("id") Integer id) {
        PhotoBook article = this.photoBookService.getPhotoBook(id);
        boolean checkedLike = false;
        this.photoBookService.viewCountUp(article);



        model.addAttribute("checkedLike", checkedLike);
        model.addAttribute("article", article);
        return "PhotoBook_detail";
    }

    ////////////////////////////////////////////
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/photobook/modify/{id}")
    public String articleModify(PhotoBookForm photoBookForm, @PathVariable("id") Integer id, Principal principal) {
        PhotoBook article = this.photoBookService.getPhotoBook(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        photoBookForm.setSubject(article.getSubject());
        photoBookForm.setContent(article.getContent());
        return "PhotoBook_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/photobook/modify/{id}")
    public String articleModify(@Valid PhotoBookForm photoBookForm, BindingResult bindingResult,
                                Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "PhotoBooke_form";
        }
        PhotoBook article = this.photoBookService.getPhotoBook(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.photoBookService.modify(article, photoBookForm.getSubject(), photoBookForm.getContent());
        return String.format("redirect:/freenotice/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/photobook/delete/{id}")
    public String articleDelete(Principal principal, @PathVariable("id") Integer id) {
        PhotoBook article = this.photoBookService.getPhotoBook(id);
        if (!article.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.photoBookService.delete(article);
        return "redirect:/freenotice/list";
    }

}
