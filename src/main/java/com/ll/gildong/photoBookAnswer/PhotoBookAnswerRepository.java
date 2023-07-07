package com.ll.gildong.photoBookAnswer;

import com.ll.gildong.photoBook.PhotoBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoBookAnswerRepository extends JpaRepository<PhotoBookAnswer, Integer> {
}
