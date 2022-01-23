package pl.polsl.skarbonka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.model.Comment;
import pl.polsl.skarbonka.repository.CommentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<Comment> getCommentsByFundraisingId(Long fundraisingId, Pageable pageable) {
        return commentRepository.getCommentsByFundraisingId(fundraisingId, pageable);
    }

    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void removeCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
