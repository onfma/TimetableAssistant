package org.example.timetableassistant.service;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.TimeSlotCRUD;
import org.example.timetableassistant.model.TimeSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeSlotService {
    private static final TimeSlotCRUD crud = new TimeSlotCRUD();

    public static List<TimeSlot> getAllTimeSlots() {
        OperationResult result = crud.getAllTimeSlots();
        List<TimeSlot> slots = new ArrayList<>();

        if (result.success && result.message instanceof List<?>) {
            List<?> rawList = (List<?>) result.message;
            for (Object obj : rawList) {
                if (obj instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) obj;
                    int id = (int) map.get("id");
                    String day = (String) map.get("day_of_week");
                    String start = (String) map.get("start_time");
                    String end = (String) map.get("end_time");

                    slots.add(new TimeSlot(id, day, start, end));
                }
            }
        }

        return slots;
    }
}
