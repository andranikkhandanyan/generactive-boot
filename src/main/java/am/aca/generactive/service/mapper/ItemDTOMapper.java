package am.aca.generactive.service.mapper;

import am.aca.generactive.model.Item;
import am.aca.generactive.service.dto.ItemDTO;
import am.aca.generactive.service.dto.ItemDetailsDTO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ItemDTOMapper {

    public static List<ItemDTO> mapToDTOs(Collection<? extends Item> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }

        List<ItemDTO> rv = new ArrayList<>();

        for (Item entity : entities) {
            rv.add(mapToDTO(entity).orElse(null));
        }

        return rv;
    }

    public static Optional<ItemDTO> mapToDTO(Item entity) {
        if (entity == null) {
            return Optional.empty();
        }

        ItemDTO dto = new ItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBasePrice(entity.getBasePrice());
        dto.setGroupName(entity.getGroup() == null ? null : entity.getGroup().getName());
        dto.setItemDetails(ItemDetailsDTO.mapToDTO(entity.getItemDetail()));

        return Optional.of(dto);
    }

    public static Item mapToEntity(ItemDTO dto) {
        if (dto == null) {
            return null;
        }

        Item entity = new Item();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setBasePrice(dto.getBasePrice());
        entity.setItemDetail(ItemDetailsDTO.mapToEntity(dto.getItemDetails()));

        return entity;
    }
}
