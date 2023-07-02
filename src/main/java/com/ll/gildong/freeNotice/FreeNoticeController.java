package com.ll.gildong.freeNotice;


import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FreeNoticeController {

    private  final  FreeNoticeService freeNoticeService;


    @GetMapping("/freeNotice/create")
    public String createFreeNotice(){
        return "FreeNotice_form";
    }

    @GetMapping("freeNotice/list")
    public String freeNoticeList(Model model , @RequestParam(value = "page",defaultValue = "0")int page ,
                                 @RequestParam(value = "kw",defaultValue = "")String kw){
        Page<FreeNotice> paging = this.freeNoticeService.getList(page,kw);
        int FreeNoticeCount = paging.getNumberOfElements();
        return "FreeNotice_list";
    }


}
