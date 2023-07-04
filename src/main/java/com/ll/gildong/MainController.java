package com.ll.gildong;


import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.freeNotice.FreeNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class MainController {

    private  final FreeNoticeService freeNoticeService;
    @GetMapping("/")
    public String mainPage(Model model){
        Page<FreeNotice> freePaging = this.freeNoticeService.getList5(0,"kw");

        model.addAttribute("freePaging",freePaging);


        return "MainPage";
    }
}
