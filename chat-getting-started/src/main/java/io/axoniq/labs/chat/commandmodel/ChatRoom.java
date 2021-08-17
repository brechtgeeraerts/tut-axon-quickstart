package io.axoniq.labs.chat.commandmodel;

import io.axoniq.labs.chat.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class ChatRoom {

    // TODO: This class has just been created to make the test compile. It's missing, well, everything...

    @AggregateIdentifier
    private String roomId;
    private List<String> participants = new ArrayList<>();


    public ChatRoom() {
    }

    @CommandHandler
    public ChatRoom(CreateRoomCommand createRoomCmd) {
        apply(new RoomCreatedEvent(createRoomCmd.getRoomId(), createRoomCmd.getName()));
    }


    @CommandHandler
    public void handle(JoinRoomCommand joinRoomCmd) {
        if (!participants.contains(joinRoomCmd.getParticipant())) {
            apply(new ParticipantJoinedRoomEvent(joinRoomCmd.getRoomId(), joinRoomCmd.getParticipant()));
        }
    }

    @CommandHandler
    public void handle(LeaveRoomCommand leaveRoomCmd) {
        if (participants.contains(leaveRoomCmd.getParticipant())) {
            apply(new ParticipantLeftRoomEvent(leaveRoomCmd.getRoomId(), leaveRoomCmd.getParticipant()));
        }
    }

    @CommandHandler
    public void handle(PostMessageCommand postMessageCmd) {
        if (!participants.contains(postMessageCmd.getParticipant())) {
            throw new IllegalStateException("Participant may only post messages to rooms he/she has joined");
        } else {
            apply(new MessagePostedEvent(postMessageCmd.getRoomId(), postMessageCmd.getParticipant(), postMessageCmd.getMessage()));
        }
    }


    @EventSourcingHandler
    public void on(RoomCreatedEvent roomCreatedEvent) {
        this.roomId = roomCreatedEvent.getRoomId();
    }

    @EventSourcingHandler
    public void on(ParticipantJoinedRoomEvent participantJoinedRoomEvent) {
        participants.add(participantJoinedRoomEvent.getParticipant());
    }

    @EventSourcingHandler
    public void on(ParticipantLeftRoomEvent participantLeftRoomEvent) {
        participants.remove(participantLeftRoomEvent.getParticipant());
    }

}
