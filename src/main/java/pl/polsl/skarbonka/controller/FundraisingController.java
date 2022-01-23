package pl.polsl.skarbonka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.polsl.skarbonka.error.exception.BadRequestException;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.error.exception.UnauthorizedException;
import pl.polsl.skarbonka.model.*;
import pl.polsl.skarbonka.request.FundraisingCreateRequest;
import pl.polsl.skarbonka.request.FundraisingEditRequest;
import pl.polsl.skarbonka.service.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/fundraisings")
public class FundraisingController {
    private final FundraisingService fundraisingService;
    private final FavoriteService favoriteService;
    private final UserService userService;
    private final ReportService reportService;
    private final CategoryService categoryService;

    @Autowired
    public FundraisingController(FundraisingService fundraisingService,
                                 FavoriteService favoriteService, UserService userService, ReportService reportService, CategoryService categoryService) {
        this.fundraisingService = fundraisingService;
        this.favoriteService = favoriteService;
        this.userService = userService;
        this.reportService = reportService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fundraising> findById(@PathVariable Long id) {
        Optional<Fundraising> fundraising = fundraisingService.findById(id);
        return fundraising.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user")
    public List<Fundraising> findByUserId(@RequestParam("userId") Long userId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") boolean sort) {
        return fundraisingService.findByUserId(userId, page, pageSize, sort);
    }

    @GetMapping("")
    public List<Fundraising> findByNameAndCategory(@RequestParam(required = false) String name, @RequestParam(required = false) String category, @RequestParam("page") int page,
                                                   @RequestParam("pageSize") int pageSize, @RequestParam("sort") boolean sort, @RequestParam("sortField") String sortField) {
        if ("undefined".equals(name)) {
            name = "";
        }
        if ("undefined".equals(category)) {
            category = "";
        }
        category = category.toUpperCase();
        if (SortField.NEWEST.name().equalsIgnoreCase(sortField)) {
            return fundraisingService.findByNameAndCategory(name, category, page, pageSize, sort, SortField.NEWEST);
        }
        return fundraisingService.findByNameAndCategory(name, category, page, pageSize, sort, SortField.POPULAR);
    }

    @GetMapping("/all")
    public List<Fundraising> findAll(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") boolean sort) {
        return fundraisingService.findAll(page, pageSize, sort);
    }

    @PostMapping("")
    public ResponseEntity<Fundraising> save(@RequestBody FundraisingCreateRequest fundraisingCreateRequest, Authentication authentication) {
        Fundraising fundraising = new Fundraising();
        fundraising.setUser(userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new));
        fundraising.setName(fundraisingCreateRequest.getName());
        fundraising.setCreatedDate(Date.from(Instant.now()));
        fundraising.setAccountBalance(BigDecimal.valueOf(0L));
        fundraising.setReportsAmount(0);
        fundraising.setEndDate(fundraisingCreateRequest.getEndDate());
        fundraising.setDescription(fundraisingCreateRequest.getDescription());
        fundraising.setCategory(categoryService.findById(fundraisingCreateRequest.getCategoryId()).orElseThrow(NotFoundException::new));
        fundraising.setMoneyGoal(BigDecimal.valueOf(fundraisingCreateRequest.getMoneyGoal().longValue()));
        Fundraising saved = fundraisingService.save(fundraising);
        if (saved != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/modify/{id}")
    public ResponseEntity<Fundraising> modify(@PathVariable Long id, @RequestBody FundraisingEditRequest fundraisingEditRequest, Authentication authentication) {
        User senderUser = userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new);
        Fundraising fundraising = fundraisingService.findById(id).orElseThrow(NotFoundException::new);
        if (!senderUser.equals(fundraising.getUser())) {
            if(!senderUser.getRole().equals(User.Role.ADMIN)) {
                throw new UnauthorizedException();
            }
        }
        BigDecimal newMoneyGoal = BigDecimal.valueOf(fundraisingEditRequest.getMoneyGoal().longValue());
        if (newMoneyGoal.compareTo(fundraising.getAccountBalance()) <= 0) {
            throw new BadRequestException("New Money goal less or equal");
        }
        fundraising.setMoneyGoal(newMoneyGoal);
        fundraising.setName(fundraisingEditRequest.getName());
        fundraising.setDescription(fundraisingEditRequest.getDescription());
        fundraising.setModificationDate(Date.from(Instant.now()));
        fundraising.setCategory(categoryService.findById(fundraisingEditRequest.getCategoryId()).orElseThrow(NotFoundException::new));
        Fundraising saved = fundraisingService.modify(fundraising);
        if (saved != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Fundraising> deleteById(@PathVariable Long id, Authentication authentication) throws IllegalAccessException {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new);
        Optional<Fundraising> byId = fundraisingService.findById(id);
        if (byId.isEmpty()) {
            throw new NotFoundException();
        }
        if (byId.get().getUser().getId().longValue() != user.getId().longValue()) {
            throw new IllegalAccessException();
        }
        boolean result = fundraisingService.deleteById(id);
        favoriteService.deleteAllByFundraisingId(id);
        reportService.deleteAllByFundraisingId(id);
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/report/{fundraisingId}")
    public ResponseEntity<Object> reportByFundraisingId(@PathVariable Long fundraisingId, Authentication authentication) throws IllegalAccessException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalAccessException();
        }
        User user = userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new);

        Optional<Report> report = reportService.findByFundraisingIdAndUserId(fundraisingId, user.getId());
        if (report.isPresent()) {
            return ResponseHandler.generateResponse("Zrzutka zgłoszona wcześniej", HttpStatus.BAD_REQUEST);
        }
        Fundraising fundraising = fundraisingService.findById(fundraisingId).orElseThrow(NotFoundException::new);
        userService.findById(user.getId()).orElseThrow(NotFoundException::new);
        reportService.addToReports(fundraising, user);
        return ResponseHandler.generateResponse("Zgłoszono", HttpStatus.CREATED);
    }
}
