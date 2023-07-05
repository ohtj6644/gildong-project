package com.ll.gildong.notice;

import com.ll.gildong.freeNotice.FreeNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
