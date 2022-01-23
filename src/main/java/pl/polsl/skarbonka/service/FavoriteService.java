package pl.polsl.skarbonka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.polsl.skarbonka.model.Favorite;
import pl.polsl.skarbonka.model.Fundraising;
import pl.polsl.skarbonka.model.User;
import pl.polsl.skarbonka.repository.FavoriteRepository;
import pl.polsl.skarbonka.repository.FundraisingRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private FundraisingRepository fundraisingRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, FundraisingRepository fundraisingRepository) {
        this.favoriteRepository = favoriteRepository;
        this.fundraisingRepository = fundraisingRepository;
    }

    public void addToFavorites(Fundraising fundraising, User user) {
        Favorite favorite = new Favorite(user, fundraising);
        favoriteRepository.save(favorite);
    }

    public boolean deleteById(Long id) {
        favoriteRepository.deleteById(id);
        Optional<Favorite> favorite = favoriteRepository.findById(id);
        return favorite.isEmpty();
    }

    public void deleteAllByFundraisingId(Long id) {
        List<Favorite> allByFundraisingId = favoriteRepository.findAllByFundraisingId(id);
        allByFundraisingId.forEach(fav -> deleteById(fav.getId()));
    }

    public List<Fundraising> findAll(int page, int pageSize, boolean sort, Long userId) {
        if (sort) {
            return fundraisingRepository.findAllFavoriteFundraisingsByUserId(PageRequest.of(page, pageSize, Sort.by(Sort.Order.asc("fundraising.name"))), userId).toList();
        } else {
            return fundraisingRepository.findAllFavoriteFundraisingsByUserId(PageRequest.of(page, pageSize, Sort.unsorted()), userId).toList();
        }
    }

    public Optional<Favorite> findById(Long id) {
        return favoriteRepository.findById(id);
    }

    public Optional<Favorite> findByIdAndUserId(Long id, Long userId) {
        return favoriteRepository.findByIdAndUserId(id, userId);
    }
}
