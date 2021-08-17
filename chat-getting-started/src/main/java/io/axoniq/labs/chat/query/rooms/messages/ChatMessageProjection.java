package io.axoniq.labs.chat.query.rooms.messages;

import io.axoniq.labs.chat.coreapi.MessagePostedEvent;
import io.axoniq.labs.chat.coreapi.RoomMessagesQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ChatMessageProjection {

    private final ChatMessageRepository repository;
    private final QueryUpdateEmitter updateEmitter;

    public ChatMessageProjection(ChatMessageRepository repository, QueryUpdateEmitter updateEmitter) {
        this.repository = repository;
        this.updateEmitter = updateEmitter;
    }

    // TODO: Create some event handlers that update this model when necessary.

    // TODO: Create the query handler to read data from this model.
    @QueryHandler
    public List<ChatMessage> handle(RoomMessagesQuery roomMessagesQuery) {
        String roomId = roomMessagesQuery.getRoomId();
        return repository.findAllByRoomIdOrderByTimestamp(roomId);
    }

    // TODO: Emit updates when new message arrive to notify subscription query by modifying the event handler.
    @EventHandler
    public void on(MessagePostedEvent messagePostedEvent, @Timestamp Instant timestamp) {
        ChatMessage chatMessage = new ChatMessage(
                messagePostedEvent.getParticipant(),
                messagePostedEvent.getRoomId(),
                messagePostedEvent.getMessage(),
                timestamp.toEpochMilli()
        );
        repository.save(chatMessage);

        updateEmitter.emit(
                RoomMessagesQuery.class,
                roomMessagesQuery -> true,
                chatMessage
        );
    }
}
