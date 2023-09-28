package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import ru.practicum.shareit.user.UserDao;

@Service
public class ItemService implements ItemServiceInter{

        private UserDao userDao;
        private ItemDao itemDao;
        private ItemMapper itemMapper = new ItemMapper();
        Gson gson = new Gson();
        private int id = 0;

        @Autowired
        private ItemService(UserDao userDao, ItemDao itemDao) {
            this.userDao = userDao;
            this.itemDao = itemDao;
        }

        private int getId() {
            return ++id;
        }


        public ItemDto save(ItemDto itemDto, Integer owner) throws ValidationException, NotFoundException {
            validateCreateItem(itemDto, owner);
            Item item = ItemMapper.toItem(itemDto);
            item.setId(getId());
            item.setOwner(owner);
            itemDao.save(item);
            return ItemMapper.toItemDto(item);
        }

        public String changeItem(ItemDto itemDto, Integer userId, Integer itemId) throws NotFoundException {
            validateUpdateItem(userId, itemId);
            Item changedItem = ItemMapper.toItem(itemDto);
            Item itemBefore = itemDao.getById(itemId);
            if (changedItem.getAvailable() != null) {
                itemBefore.setAvailable(changedItem.getAvailable());
            }
            if (changedItem.getDescription() != null) {
                itemBefore.setDescription(changedItem.getDescription());
            }
            if (changedItem.getName() != null) {
                itemBefore.setName(changedItem.getName());
            }
            itemDao.save(itemBefore);
            return gson.toJson(itemBefore);
        }

        public ItemDto getById(Integer id) throws NotFoundException {
            validateNotFoundItem(id);
            return ItemMapper.toItemDto(itemDao.getById(id));
        }

        public List<ItemDto> getAll() {
            return ItemMapper.toListDto(itemDao.getValues());
        }

        public List<ItemDto> getAllItemByUser(Integer id) {
            List<Item> userItems = new ArrayList<>();
            for (Item item : itemDao.getValues()) {
                if (id == item.getOwner()) {
                    userItems.add(item);
                }
            }
           return ItemMapper.toListDto(userItems);
        }

        public List<ItemDto> getBySearch(String text) {
           if (text == null || text.isBlank()) {
               return new ArrayList<>();
           }
            List<Item> items = new ArrayList<>();
            List<String> words = new ArrayList<>(List.of(text.toLowerCase().split(" ")));
            for (Item item : itemDao.getValues()) {
                if (item.getAvailable()) {
                    int itemsNumber = items.size();
                    List<String> itemWords = new ArrayList<>(List.of(item.getName().toLowerCase().split(" ")));
                    itemWords.addAll(List.of(item.getDescription().toLowerCase().split(" ")));
                    search: {
                        for (String wordText : words) {
                            for (String wordItem : itemWords) {
                                if (wordItem.contains(wordText)) {
                                    if (items.size() != itemsNumber) {
                                        break search;
                                    }

                                    items.add(item);
                                }
                            }
                        }
                    }
                }
            }
            return ItemMapper.toListDto(items);
        }

        private void validateCreateItem(ItemDto itemDto, Integer ownerId)
                throws ValidationException, NotFoundException {
            validateNotFoundUser(ownerId);
            if (itemDto.getName() == null || itemDto.getName().isBlank() ||
               itemDto.getDescription() == null || itemDto.getDescription().isBlank() ||
               itemDto.getAvailable() == null) {
                throw new ValidationException("Указаны не все данные");
            }
        }

        private void validateNotFoundItem(int itemId) throws NotFoundException {
          if (itemDao.getById(itemId) == null) {
               throw new NotFoundException("Указанного предмета нет");
          }
        }

         private void validateCheckUser(int itemId, int userId) throws NotFoundException {
            validateNotFoundItem(itemId);
            if (itemDao.getById(itemId).getOwner() != userId) {
                throw new NotFoundException("Указан не верный пользователь");
            }
         }

         private void validateUpdateItem(int userId, int itemId)
                throws NotFoundException {
            validateCheckUser(itemId,userId);
         }

          private void validateNotFoundUser(int userId) throws NotFoundException {
                if (userDao.getById(userId) == null) {
                    throw  new NotFoundException("Пользователь не найден");
                }
          }

}
