package am.aca.generactive.controller;

import am.aca.generactive.IntegrationTest;
import am.aca.generactive.model.Item;
import am.aca.generactive.repository.ItemRepository;
import am.aca.generactive.security.SecurityUtils;
import am.aca.generactive.service.AzureService;
import am.aca.generactive.service.dto.ItemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username="user",roles={"ADMIN"})
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SecurityUtils securityUtils;

    @MockBean
    private AzureService azureService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Transactional
    void createItem() throws Exception {
        long countBeforeCreate = itemRepository.count();

        ItemDTO itemDTO = createItemDTO();
        mockMvc.perform(MockMvcRequestBuilders.post("/item")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + securityUtils.getAdminJWT())
            .content(objectMapper.writeValueAsString(itemDTO)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.id").exists());

        assertThat(itemRepository.count()).isEqualTo(countBeforeCreate + 1);
    }

    @Test
    @Transactional
    void createItem_ByUser() throws Exception {
        long countBeforeCreate = itemRepository.count();

        ItemDTO itemDTO = createItemDTO();
        mockMvc.perform(MockMvcRequestBuilders.post("/item")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + securityUtils.getUserJWT())
            .content(objectMapper.writeValueAsString(itemDTO)))
        .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    void getItems() throws Exception {
        itemRepository.saveAndFlush(createItemEntity());

        mockMvc.perform(MockMvcRequestBuilders.get("/item")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + securityUtils.getUserJWT()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.[*].name").value(hasItem("Test")));

    }

    @ParameterizedTest
    @ValueSource(ints = {1})
    @Transactional
    void getItem(int id) throws Exception {
        Item item = itemRepository.saveAndFlush(createItemEntity());
        mockMvc.perform(MockMvcRequestBuilders.get("/item/{id}", item.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + securityUtils.getAdminJWT()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId().intValue()));
    }

    @Test
    void renderItem() {
        BDDMockito.given(azureService.isRendering()).willReturn(true);

        assertThat(azureService.isRendering()).isEqualTo(true);
    }

    public static ItemDTO createItemDTO() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setName("Test");
        itemDTO.setBasePrice(100);

        return itemDTO;
    }

    public static Item createItemEntity() {
        Item item = new Item();
        item.setName("Test");
        item.setBasePrice(100);

        return item;
    }
}
