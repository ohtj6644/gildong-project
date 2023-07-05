package com.ll.gildong;


import com.ll.gildong.freeNotice.FreeNotice;
import com.ll.gildong.freeNotice.FreeNoticeService;
import com.ll.gildong.notice.Notice;
import com.ll.gildong.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class MainController {

    private  final FreeNoticeService freeNoticeService;
    private final NoticeService noticeService;
    @GetMapping("/")
    public String mainPage(Model model){
        Page<FreeNotice> freePaging = this.freeNoticeService.getList5(0,"kw");
        Page<Notice> noticePaging = this.noticeService.getList5(0);

        model.addAttribute("freePaging",freePaging);
        model.addAttribute("noticePaging",noticePaging);


        return "MainPage";
    }
}
