package ru.practicum.shareit.item;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemDao {
    private Map<Long, Item> itemMap = new HashMap<>();

    public Item getById(int id) {
        return itemMap.get(id);
    }

    public void save(Item item) {
        itemMap.put(item.getId(), item);
    }

    public List<Item> getValues() {
        return new ArrayList<>(itemMap.values());
    }

}
