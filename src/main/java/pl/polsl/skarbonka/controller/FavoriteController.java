package pl.polsl.skarbonka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.model.Favorite;
import pl.polsl.skarbonka.model.Fundraising;
import pl.polsl.skarbonka.model.ResponseHandler;
import pl.polsl.skarbonka.model.User;
import pl.polsl.skarbonka.service.FavoriteService;
import pl.polsl.skarbonka.service.FundraisingService;
import pl.polsl.skarbonka.service.UserService;

import java.time.Instant;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/favorites")
public class FavoriteController {

    private FavoriteService favoriteService;
    private FundraisingService fundraisingService;
    private UserService userService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService, FundraisingService fundraisingService, UserService userService) {
        this.favoriteService = favoriteService;
        this.fundraisingService = fundraisingService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Fundraising> findAllByUserId(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") boolean sort, @RequestParam Long userId) {
        User user = userService.findById(userId).orElseThrow(NotFoundException::new);
        return new ArrayList<>(favoriteService.findAll(page, pageSize, sort, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Favorite> deleteById(@PathVariable Long id, @RequestParam Long userId) {
        Optional<Favorite> fav = favoriteService.findById(id);
        if (fav.isPresent()) {
            Long id1 = fav.get().getUser().getId();
            if (!Objects.equals(id1, userId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            boolean result = favoriteService.deleteById(id);
            if (result) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("")
    public ResponseEntity<Object> addToFavorites(@RequestParam Long fundraisingId, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new);
        Optional<Favorite> favorite = favoriteService.findByIdAndUserId(fundraisingId, user.getId());
        if (favorite.isPresent()) {
            return ResponseHandler.generateResponse("Zrzutka dodana do ulubionych wcześniej", HttpStatus.BAD_REQUEST);
        }
        Optional<Fundraising> fundraising = fundraisingService.findById(fundraisingId);
        if (fundraising.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        fundraising.get().setCreatedDate(Date.from(Instant.now()));
        favoriteService.addToFavorites(fundraising.get(), user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
