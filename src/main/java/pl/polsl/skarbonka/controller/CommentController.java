package pl.polsl.skarbonka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.error.exception.PermissionDeniedException;
import pl.polsl.skarbonka.model.Comment;
import pl.polsl.skarbonka.model.Donation;
import pl.polsl.skarbonka.model.Fundraising;
import pl.polsl.skarbonka.model.User;
import pl.polsl.skarbonka.request.CommentAddRequest;
import pl.polsl.skarbonka.response.EntityCreateResponse;
import pl.polsl.skarbonka.response.PageResponse;
import pl.polsl.skarbonka.security.AuthHelper;
import pl.polsl.skarbonka.service.CommentService;
import pl.polsl.skarbonka.service.FundraisingService;
import pl.polsl.skarbonka.service.UserService;
import pl.polsl.skarbonka.util.ResponseEntityUtil;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final FundraisingService fundraisingService;
    private final UserService userService;
    private final AuthHelper authHelper;
    private final ResponseEntityUtil responseEntityUtil;

    @Autowired
    public CommentController(CommentService commentService, FundraisingService fundraisingService, UserService userService, AuthHelper authHelper, ResponseEntityUtil responseEntityUtil) {
        this.commentService = commentService;
        this.fundraisingService = fundraisingService;
        this.userService = userService;
        this.authHelper = authHelper;
        this.responseEntityUtil = responseEntityUtil;
    }

    @PostMapping()
    public ResponseEntity<EntityCreateResponse> addComment(@RequestBody CommentAddRequest commentAddRequest, Authentication authentication) {
        User commentAuthor = userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new);
        Comment comment = new Comment();
        Fundraising fundraising = fundraisingService.findById(commentAddRequest.getFundraisingId()).orElseThrow(NotFoundException::new);
        comment.setFundraising(fundraising);
        comment.setText(commentAddRequest.getText());
        comment.setUser(commentAuthor);

        Comment savedComment = commentService.saveComment(comment);
        Date saveTimestamp = Date.from(Instant.now());
        return ResponseEntity.ok(EntityCreateResponse.of(savedComment, saveTimestamp));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        User commentAuthor = userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new);
        Comment comment = commentService.findById(commentId).orElseThrow(NotFoundException::new);
        if (comment.getUser().getId().equals(commentAuthor.getId()) || authHelper.isAdmin(authentication)) {
            commentService.removeCommentById(commentId);
            return responseEntityUtil.successfulMessageResponseEntity("Comment successfully removed");
        }
        throw new PermissionDeniedException(authHelper.getRoleOfAuthentication(authentication));
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsByFundraisingId(
            @RequestParam Long fundraisingId,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        Page<Comment> commentsPage = commentService.getCommentsByFundraisingId(fundraisingId, PageRequest.of(page, size));
        return ResponseEntity.ok(new PageResponse<>(commentsPage.getContent(), page, size).getContent());
    }

    @GetMapping("/{id}")
    public Comment findCommentById(@PathVariable Long id) {
        return commentService.findCommentById(id);
    }
}
