package io.axoniq.labs.chat.query.rooms.participants;

import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomParticipantsQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomParticipantsProjection {

    private final RoomParticipantsRepository repository;

    public RoomParticipantsProjection(RoomParticipantsRepository repository) {
        this.repository = repository;
    }

    // TODO: Create some event handlers that update this model when necessary.
    @EventHandler
    public void on(ParticipantJoinedRoomEvent participantJoinedRoomEvent) {
        RoomParticipant participant = new RoomParticipant(participantJoinedRoomEvent.getRoomId(), participantJoinedRoomEvent.getParticipant());
        repository.save(participant);
    }

    @EventHandler
    public void on(ParticipantLeftRoomEvent participantLeftRoomEvent) {
        RoomParticipant participant = new RoomParticipant(participantLeftRoomEvent.getRoomId(), participantLeftRoomEvent.getParticipant());
        repository.deleteByParticipantAndRoomId(participant.getParticipant(), participant.getRoomId());
    }

    // TODO: Create the query handler to read data from this model.
    @QueryHandler
    public List<RoomParticipant> handle(RoomParticipantsQuery roomParticipantsQuery) {
        return repository.findAll();
    }
}
