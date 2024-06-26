package music.application;

import music.domain.CartItem;
import music.domain.CartRepository;
import music.domain.MyAlbum;
import music.domain.dto.AlbumDTO;
import music.service.Database;

import java.util.List;

public class CartService {
    private final CartRepository cartRepository;
    private final Database database;

    public CartService(CartRepository cartRepository, Database database) {
        this.cartRepository = cartRepository;
        this.database = database;
    }

    public void put(List<String> albumIds) {
        if (albumIds.isEmpty()) {
            throw new IllegalArgumentException("하나 이상의 앨범을 담을 수 있습니다.");
        }

        List<AlbumDTO> albums = database.find(albumIds);
        List<CartItem> items = albums.stream()
                .map(CartItem::new)
                .toList();
        cartRepository.save(items);
    }

    public void cancel(List<Integer> cartItemIds) {
        cartRepository.delete(cartItemIds);
    }

    public List<CartItem> findAll() {
        return cartRepository.findAll();
    }

    public void update(int inputCartItemIds, int quantity) {
        cartRepository.update(inputCartItemIds, quantity);
    }

    public void buy(MyAlbum myAlbum, Database db) {
        cartRepository.buy(myAlbum, db);
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        List<CartItem> cartItems = cartRepository.findAll();
        for (CartItem item : cartItems) {
            totalPrice += item.getAlbum().getCollectionPriceKRW() * item.getQuantity();
        }
        return totalPrice;
    }
}
