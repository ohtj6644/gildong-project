package com.ll.gildong.freeNotice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreeNoticeRepository extends JpaRepository<FreeNotice, Integer> {


    Page<FreeNotice> findAll(Specification<FreeNotice> spec, Pageable pageable);
}
