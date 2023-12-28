package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.Gson;
import ru.practicum.shareit.user.UserRepository;

@Service
public class ItemService implements ItemServiceInter {
        private ItemRepository itemRepository;
        private UserRepository userRepository;
        private BookingRepository bookingRepository;
        private CommentRepository commentRepository;
        private BookingMapper bookingMapper;
        Gson gson = new Gson();

        @Autowired
        private ItemService(UserRepository userRepository,
                            ItemRepository itemRepository,
                            BookingRepository bookingRepository,
                            CommentRepository commentRepository,
                            BookingMapper bookingMapper) {
            this.userRepository = userRepository;
            this.itemRepository = itemRepository;
            this.bookingRepository = bookingRepository;
            this.commentRepository = commentRepository;
            this.bookingMapper = bookingMapper;
        }

        public ItemDto save(ItemDto itemDto, Long owner) throws ValidationException, NotFoundException {
            validateCreateItem(itemDto, owner);
            Item item = ItemMapper.toItem(itemDto);
            item.setUserId(owner);

            itemRepository.save(item);
            return ItemMapper.toItemDto(item);
        }

        public ItemDto changeItem(ItemDto itemDto, Long userId, Long itemId) throws NotFoundException {
            validateUpdateItem(userId, itemId);
            Item changedItem = ItemMapper.toItem(itemDto);
            Item itemBefore = itemRepository.getById(itemId);
                if (changedItem.getAvailable() != null) {
                    itemBefore.setAvailable(changedItem.getAvailable());
                }
                if (changedItem.getDescription() != null) {
                    itemBefore.setDescription(changedItem.getDescription());
                }
                 if (changedItem.getName() != null) {
                    itemBefore.setName(changedItem.getName());
                 }

             itemRepository.save(itemBefore);
        return ItemMapper.toItemDto(itemBefore);
    }

        public ItemDto getById(Long id, Long userId) throws NotFoundException {
            validateNotFoundItem(id);
            ItemDto itemDto = ItemMapper.toItemDto(itemRepository.getById(id));

            if (commentRepository.existsByItemId(itemDto.getId())) {
                itemDto.setComments(CommentMapper.toListCommentDto(commentRepository.findByItemId(itemDto.getId())));
            } else  {
                itemDto.setComments(new ArrayList<>());
            }

            if (bookingRepository.existsByItemId(itemDto.getId())) {
                if (Objects.equals(itemRepository.getById(id).getUserId(), userId)) {
                    itemDto = findIndexes(bookingRepository.findByItemIdOrderByStartDesc(itemDto.getId()), itemDto);
                }
            }
            return itemDto;
        }

        public List<ItemDto> getAll() {
            return ItemMapper.toListDto(itemRepository.findAll());
        }

        public List<ItemDto> getAllItemByUser(Long id) {
            List<Item> userItems = new ArrayList<>();
            for (Item item : itemRepository.findAllByOrderByIdAsc()) {
                if (Objects.equals(id, item.getUserId())) {
                    userItems.add(item);
                }
            }
            List<ItemDto> itemDtos = ItemMapper.toListDto(userItems);
            for (ItemDto itemDto : itemDtos) {
                if (bookingRepository.existsByItemId(itemDto.getId())) {
                    itemDto = findIndexes(bookingRepository.findByItemIdOrderByStartDesc(itemDto.getId()), itemDto);
                }
                if (commentRepository.existsByItemId(itemDto.getId())) {
                    itemDto.setComments(CommentMapper.toListCommentDto(commentRepository.findByItemId(itemDto.getId())));
                } else {
                    itemDto.setComments(new ArrayList<>());
                }
            }
           return itemDtos;
        }

        public List<ItemDto> getBySearch(String text) {
           if (text == null || text.isBlank()) {
               return new ArrayList<>();
           }
            List<Item> items = new ArrayList<>();
            List<String> words = new ArrayList<>(List.of(text.toLowerCase().split(" ")));
            for (Item item : itemRepository.findAll()) {
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

        //Comments
        public CommentDto addComment(Comment comment, Long itemId, Long userId) throws NotFoundException, ValidationException {
            validateNotBookingForUser(itemId, userId);
            validateNullComment(comment.getText());
            validateFutureComment(itemId, userId);
            comment.setCreated(LocalDateTime.now());
            comment.setUser(userRepository.getUserById(userId));
            comment.setItem(itemRepository.getById(itemId));
            commentRepository.save(comment);
            return CommentMapper.toCommentDto(comment);
        }

        public List<CommentDto> getAllComments(Long itemId, Long userId) {
            return CommentMapper.toListCommentDto(commentRepository.findByItemId(itemId));
        }

    private ItemDto findIndexes(List<Booking> bookings, ItemDto itemDto) {
        List<BookingDto> bookingDtos = bookingMapper.toDtoList(bookings);
        LocalDateTime testTime = LocalDateTime.now();
        for (BookingDto booking : bookingDtos) {
            if (itemDto.getNextBooking() == null) {
                if (booking.getStart().isAfter(testTime)) {
                    if (booking.getEnd().isAfter(LocalDateTime.now())) {
                        itemDto.setNextBooking(booking);
                    }
                }
            } else {
                if (booking.getStart().isAfter(LocalDateTime.now())
                        && booking.getStart().isBefore(itemDto.getNextBooking().getStart())) {
                    if (booking.getEnd().isAfter(LocalDateTime.now())) {
                        itemDto.setNextBooking(booking);
                    }
                }
            }

            if (itemDto.getLastBooking() == null) {
                if (booking.getEnd().isBefore(testTime)) {
                    if (booking.getStart().isBefore(LocalDateTime.now())) {
                        itemDto.setLastBooking(booking);
                    }
                }
            } else {
                if (booking.getEnd().isBefore(testTime)
                        && booking.getEnd().isAfter(itemDto.getLastBooking().getEnd())) {
                    if (booking.getStart().isBefore(LocalDateTime.now())) {
                        itemDto.setLastBooking(booking);
                    }
                }
            }
        }
        return itemDto;
    }

        private void validateCreateItem(ItemDto itemDto, Long ownerId)
                throws ValidationException, NotFoundException {
            if (itemDto.getName() == null || itemDto.getName().isBlank() ||
               itemDto.getDescription() == null || itemDto.getDescription().isBlank() ||
               itemDto.getAvailable() == null) {
                throw new ValidationException("Указаны не все данные");
            }
            validateNotFoundUser(ownerId);
        }

        private void validateNotBookingForUser(Long itemId, Long userId) throws NotFoundException {
            if (!bookingRepository.existsByItemIdAndUserId(itemId, userId)) {
                throw new NotFoundException("Пользователь не арендовал вешь");
            }
        }

        private void validateNotFoundItem(Long itemId) throws NotFoundException {
          if (!itemRepository.existsById(itemId)) {
               throw new NotFoundException("Указанного предмета нет");
          }
        }

         private void validateCheckUser(Long itemId, Long userId) throws NotFoundException {
            validateNotFoundItem(itemId);
            if (!Objects.equals(itemRepository.getById(itemId).getUserId(), userId)) {
                throw new NotFoundException("Указан не верный пользователь");
            }
         }

         private void validateUpdateItem(Long userId, Long itemId)
                throws NotFoundException {
            validateCheckUser(itemId,userId);
         }

          private void validateNotFoundUser(Long userId) throws NotFoundException {
                if (!userRepository.existsById(userId)) {
                    throw  new NotFoundException("Пользователь не найден");
                }
          }

          private void validateNullComment(String comment) throws ValidationException {
            if (comment == null || comment.isBlank()) {
                throw new ValidationException("Комментарий пуст");
            }
          }

          private void validateFutureComment(Long itemId, Long userId) throws ValidationException {

              if (bookingRepository.findByItemIdAndUserIdOrderByStartAsc(itemId, userId).get(0).getStart().isAfter(LocalDateTime.now())) {
                  throw new ValidationException("Вы еще не опробовали предмет");
              }

          }

}
