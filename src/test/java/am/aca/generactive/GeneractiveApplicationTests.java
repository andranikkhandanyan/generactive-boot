package am.aca.generactive;

import am.aca.generactive.controller.ItemController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GeneractiveApplicationTests {

    @Autowired
    ItemController itemController;

    @Autowired(required = false)
    RestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(itemController).isNotNull();
//        assertThat(restTemplate).isNotNull();
    }

}
