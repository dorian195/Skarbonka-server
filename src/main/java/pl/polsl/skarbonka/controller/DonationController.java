package pl.polsl.skarbonka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.polsl.skarbonka.error.exception.BadRequestException;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.model.Donation;
import pl.polsl.skarbonka.model.Fundraising;
import pl.polsl.skarbonka.model.User;
import pl.polsl.skarbonka.query.DonatorsQueryResult;
import pl.polsl.skarbonka.response.EntityCreateResponse;
import pl.polsl.skarbonka.service.DonationService;
import pl.polsl.skarbonka.service.FundraisingService;
import pl.polsl.skarbonka.service.UserService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/donations")
public class DonationController {
    private final DonationService donationService;
    private final FundraisingService fundraisingService;
    private final UserService userService;
    private final Long ANON_ID = 37L;

    @Autowired
    public DonationController(DonationService donationService, FundraisingService fundraisingService, UserService userService) {
        this.donationService = donationService;
        this.fundraisingService = fundraisingService;
        this.userService = userService;
    }

    @GetMapping("")
    public List<Donation> findAllByFundraisingId(@RequestParam("fundraisingId") Long fundraisingId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") boolean sort) {
        return donationService.findAllByFundraisingId(fundraisingId, page, pageSize, sort);
    }

    @GetMapping("donators")
    public List<DonatorsQueryResult> findAllDonatorsByFundraisingId(@RequestParam("fundraisingId") Long fundraisingId, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") boolean sort) {
        return donationService.findDistinctUsersByFundraisingId(fundraisingId, page, pageSize, sort);
    }

    @GetMapping("/{id}")
    public Donation findDonationById(@PathVariable Long id) {
        return donationService.findDonationById(id);
    }

    @PostMapping("")
    public ResponseEntity<EntityCreateResponse> save(@RequestBody Donation donation, @RequestParam("fundraisingId") Long fundraisingId, Authentication authentication) {
        if (!donation.isAnonymous()) {
            User user = userService.findByEmail(authentication.getName()).orElseThrow(NotFoundException::new);
            donation.setUser(user);
        } else {
            User anon = userService.findById(ANON_ID).get();
            donation.setUser(anon);
        }
        Optional<Fundraising> fundraising = fundraisingService.findById(fundraisingId);
        if (fundraising.isEmpty()) {
            throw new NotFoundException("Nie znaleziono takiej zrzutki");
        }
        Date now = Date.from(Instant.now());
        Fundraising fund = fundraising.get();
        if (now.after(fund.getEndDate())) {
            throw new BadRequestException("Zrzutka już się zakończyła");
        }
        donation.setFundraising(fund);
        BigDecimal balance = fund.getAccountBalance();
        fund.setAccountBalance(new BigDecimal(balance.longValue() + donation.getAmmount().longValue()));
        donation.setCreatedDate(now);
        Donation saved = donationService.save(donation);
        if (saved != null) {
            Date timestamp = Date.from(Instant.now());
            return ResponseEntity.ok(EntityCreateResponse.of(saved, timestamp));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
