package com.ll.gildong.photoBook;


import com.ll.gildong.User.SiteUser;
import com.ll.gildong.User.UserService;
import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.freeNotice.FreeNoticeForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

}
