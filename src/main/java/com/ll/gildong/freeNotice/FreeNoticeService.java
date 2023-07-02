package com.ll.gildong.freeNotice;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.sql.rowset.Predicate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.annotation.MergedAnnotations.search;

@Service
@RequiredArgsConstructor
public class FreeNoticeService {

    private  final FreeNoticeRepository freeNoticeRepository;

//    private Specification<FreeNotice> search(String kw){
//        return new Specification<>() {
//            private static final long seriaVersionUID=1L;
//            @Override
//            public Predicate toPredicate(Root<FreeNotice> q, CriteriaQuery<?> query, CriteriaBuilder cd){
//                query.distinct(true);
//                Join<FreeNotice>
//            }
//        }
//    }

    public Page<FreeNotice> getList(int page, String kw) {
        List<Sort.Order> sorts= new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,15,Sort.by(sorts));
//        Specification<FreeNotice> spec = search(kw);
        return this.freeNoticeRepository.findAll(pageable);
    }
}
