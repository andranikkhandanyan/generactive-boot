package am.aca.generactive.repository;

import am.aca.generactive.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void saveItem() {
        Item item = new Item();
        item.setName("test");

        long countBeforeSave = itemRepository.count();

        itemRepository.save(item);

        assertThat(itemRepository.count()).isEqualTo(countBeforeSave + 1);

    }

    @Test
    @Transactional
    void getItem() {
        Item item = new Item();
        item.setName("test");

        itemRepository.save(item);

        assertThat(itemRepository.count()).isGreaterThan(0);

    }
}
