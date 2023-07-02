package com.ll.gildong.freeNotice;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FreeNoticeController {

    private  final  FreeNoticeService freeNoticeService;
}
