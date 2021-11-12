package am.aca.generactive.controller;

import am.aca.generactive.service.ItemService;
import am.aca.generactive.service.dto.ItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<? extends ItemDTO> getAll() {
        return itemService.getAll();
    }

    @GetMapping("/search")
    @PostAuthorize("hasAnyAuthority('ADMIN')")
    public List<? extends ItemDTO> search(@RequestParam String name) {
        return itemService.find(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        return ResponseEntity.of(itemService.getItem(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public @ResponseBody ItemDTO create(@RequestBody ItemDTO itemDTO) {
        return itemService.create(itemDTO);
    }
}
