package org.example.timetableassistant.service;

import org.example.timetableassistant.model.Semiyear;

public class AssistantService {
    public static void verifyClassCreation(int roomId, int timeSlotId, Semiyear semiyear, Integer groupId, int teacherId) throws Exception {

        ClassService.getByTimeSlotId(timeSlotId)
                .forEach(classEntry -> {
                    if (classEntry.getRoomId() == roomId) {
                        throw new RuntimeException("Conflict with existing class in the same room at the same time.");
                    }
                    if (classEntry.getTeacherId() == teacherId) {
                        throw new RuntimeException("Conflict with existing class for the same teacher at the same time.");
                    }
                    if (groupId != null && classEntry.getGroupId() != null && classEntry.getGroupId() == groupId) {
                        throw new RuntimeException("Conflict with existing class for the same group at the same time.");
                    }
                });

    }
}
