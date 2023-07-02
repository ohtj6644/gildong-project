package com.ll.gildong.freeNotice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeNoticeRepository extends JpaRepository<FreeNotice, Integer> {
}
