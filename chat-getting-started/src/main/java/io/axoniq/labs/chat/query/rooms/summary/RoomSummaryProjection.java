package io.axoniq.labs.chat.query.rooms.summary;

import io.axoniq.labs.chat.coreapi.AllRoomsQuery;
import io.axoniq.labs.chat.coreapi.ParticipantJoinedRoomEvent;
import io.axoniq.labs.chat.coreapi.ParticipantLeftRoomEvent;
import io.axoniq.labs.chat.coreapi.RoomCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomSummaryProjection {

    private final RoomSummaryRepository roomSummaryRepository;

    public RoomSummaryProjection(RoomSummaryRepository roomSummaryRepository) {
        this.roomSummaryRepository = roomSummaryRepository;
    }

    // TODO: Create some event handlers that update this model when necessary.
    @EventHandler
    public void on(RoomCreatedEvent roomCreatedEvent) {
       RoomSummary roomSummary = new RoomSummary(roomCreatedEvent.getRoomId(), roomCreatedEvent.getName());
       roomSummaryRepository.save(roomSummary);
    }

    @EventHandler
    public void on(ParticipantJoinedRoomEvent participantJoinedRoomEvent) {
        Optional<RoomSummary> roomSummaryById = roomSummaryRepository.findById(participantJoinedRoomEvent.getRoomId());
        roomSummaryById.ifPresent(roomSummary -> roomSummary.addParticipant());
    }

    @EventHandler
    public void on(ParticipantLeftRoomEvent participantLeftRoomEvent) {
        Optional<RoomSummary> roomSummaryById = roomSummaryRepository.findById(participantLeftRoomEvent.getRoomId());
        roomSummaryById.ifPresent(roomSummary -> roomSummary.removeParticipant());
    }

    // TODO: Create the query handler to read data from this model.
    @QueryHandler
    public List<RoomSummary> handle(AllRoomsQuery allRoomsQuery) {
        return roomSummaryRepository.findAll();
    }
}
