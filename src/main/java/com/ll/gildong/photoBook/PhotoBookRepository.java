package com.ll.gildong.photoBook;

import com.ll.gildong.freeNotice.FreeNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoBookRepository extends JpaRepository<PhotoBook, Integer> {

    Page<PhotoBook> findAll(Specification<PhotoBook> spec, Pageable pageable);
}
