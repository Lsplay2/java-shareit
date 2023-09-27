package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemRequestService {
    Map<Integer, ItemRequest> itemRequestMap = new HashMap<>();

    public void save(ItemRequest itemRequest) {
        itemRequestMap.put(itemRequest.getId(), itemRequest);
    }

    public ItemRequest getById(Integer id) {
        return itemRequestMap.get(id);
    }

    public List<ItemRequest> getAll() {
        return new ArrayList<>(itemRequestMap.values());
    }

}
