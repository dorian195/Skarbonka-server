package pl.polsl.skarbonka.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.polsl.skarbonka.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
        Page<Comment> getCommentsByFundraisingId(Long userId, Pageable pageable);
}
