package com.ll.gildong.freeNotice;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreeNoticeService {

    private  final FreeNoticeRepository freeNoticeRepository;
}
