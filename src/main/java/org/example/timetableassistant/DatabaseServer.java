package org.example.timetableassistant;
import org.example.timetableassistant.database.handler.*;

import static spark.Spark.*;

public class DatabaseServer {
    public static void startServer() {
        port(4567); // setează portul serverului

        path("/db", () -> {
            // TimeSlots Handlers
            post("/time-slot", (req, res) -> TimeSlotHandler.createTimeSlot(req, res));
            get("/time-slot/get-all", (req, res) -> TimeSlotHandler.getAllTimeSlots(req, res));
            get("/time-slot/:id", (req, res) -> TimeSlotHandler.getTimeSlotById(req, res));
            put("/time-slot/:id", (req, res) -> TimeSlotHandler.updateTimeSlot(req, res));
            delete("/time-slot/:id", (req, res) -> TimeSlotHandler.deleteTimeSlot(req, res));

            // Class Handlers
            post("/class", (req, res) -> ClassHandler.createClass(req, res));
            put("/class/:id", (req, res) -> ClassHandler.updateClass(req, res));
            delete("/class/:id", (req, res) -> ClassHandler.deleteClass(req, res));
            get("/class/get-all", (req, res) -> ClassHandler.getAllClasses(req, res));
            get("/class/:id", (req, res) -> ClassHandler.getClassById(req, res));
            get("/class/get-by-group-id/:groupId", (req, res) -> ClassHandler.getClassesByGroupId(req, res));
            get("/classes/get-by-room-id/:roomId", (req, res) -> ClassHandler.getClassesByRoomId(req, res));
            get("/classes/get-by-semiyear/:semiyear", (req, res) -> ClassHandler.getClassesBySemiyear(req, res));
            get("/classes/get-by-teacher-id/:teacherId", (req, res) -> ClassHandler.getClassesByTeacherId(req, res));
            get("/classes/get-by-discipline-id/:disciplineId", (req, res) -> ClassHandler.getClassesByDisciplineId(req, res));
            get("/classes/get-by-time-slot-id/:time_slot_id", (req, res) -> ClassHandler.getClassesByTimeSlotId(req, res));

            // Discipline Handler
            post("/discipline", (req, res) -> DisciplineHandler.createDiscipline(req, res));
            get("/discipline/get-all", (req, res) -> DisciplineHandler.getAllDisciplines(req, res));
            get("/discipline/:id", (req, res) -> DisciplineHandler.getDisciplineById(req, res));
            put("/discipline/:id", (req, res) -> DisciplineHandler.updateDiscipline(req, res));
            delete("/discipline/:id", (req, res) -> DisciplineHandler.deleteDiscipline(req, res));

            // Discipline Allocation Handler
            post("/discipline-allocation", (req, res) -> DisciplineAllocationHandler.createDisciplineAllocation(req, res));
            get("/discipline-allocation/get-all", (req, res) -> DisciplineAllocationHandler.getAllDisciplineAllocations(req, res));
            get("/discipline-allocation/get-by-discipline-id/:id", DisciplineAllocationHandler::getAllDisciplineAllocationsByDisciplineId);
            get("/discipline-allocation/get-by-teacher-id/:id", DisciplineAllocationHandler::getAllDisciplineAllocationsByTeacherId);
            get("/discipline-allocation/:id", (req, res) -> DisciplineAllocationHandler.getDisciplineAllocationById(req, res));
            put("/discipline-allocation/:id", (req, res) -> DisciplineAllocationHandler.updateDisciplineAllocation(req, res));
            delete("/discipline-allocation/:id", (req, res) -> DisciplineAllocationHandler.deleteDisciplineAllocation(req, res));

            // RoomType Handler
            post("/roomType", (req, res) -> RoomTypesHandler.createRoomType(req, res));
            get("/roomType/get-all", (req, res) -> RoomTypesHandler.getAllRoomTypes(req, res));
            get("/roomType/:id", (req, res) -> RoomTypesHandler.getRoomTypeById(req, res));
            put("/roomType/:id", (req, res) -> RoomTypesHandler.updateRoomType(req, res));
            delete("/roomType/:id", (req, res) -> RoomTypesHandler.deleteRoomType(req, res));

            // Room Handler
            post("/room", (req, res) -> RoomHandler.createRoom(req, res));
            get("/room/get-all", (req, res) -> RoomHandler.getAllRooms(req, res));
            get("/room/:id", (req, res) -> RoomHandler.getRoomById(req, res));
            put("/room/:id", (req, res) -> RoomHandler.updateRoom(req, res));
            delete("/room/:id", (req, res) -> RoomHandler.deleteRoom(req, res));

            // Teacher Handler
            post("/teacher", (req, res) -> TeacherHandler.createTeacher(req, res));
            get("/teacher/get-all", (req, res) -> TeacherHandler.getAllTeachers(req, res));
            get("/teacher/get-by-id/:id", (req, res) -> TeacherHandler.getTeacherById(req, res));
            get("/teacher/get-by-name/:name", (req, res) -> TeacherHandler.getTeacherByName(req, res));
            put("/teacher/:id", (req, res) -> TeacherHandler.updateTeacher(req, res));
            delete("/teacher/:id", (req, res) -> TeacherHandler.deleteTeacher(req, res));

            // Semiyear routes
            post("/semiyear", SemiyearHandler::createSemiyear);
            get("/semiyear/get-all", SemiyearHandler::getAllSemiyears);
            get("/semiyear/:id", SemiyearHandler::getSemiyearById);
            get("/semiyear/get-by-name", SemiyearHandler::getSemiyearByNameAndYear); // e.g. /semiyears/by?name=Sem1&study_year=1
            put("/semiyear/:id", SemiyearHandler::updateSemiyear);
            delete("/semiyear/:id", SemiyearHandler::deleteSemiyear);

            // Group routes
            post("/group", GroupHandler::createGroup);
            get("/group/get-all", GroupHandler::getAllGroups);
            get("/group/:id", GroupHandler::getGroupById);
            put("/group/:id", GroupHandler::updateGroup);
            delete("/group/:id", GroupHandler::deleteGroup);
            get("/group/get-by-number-semiyear/:name", GroupHandler::getGroupByNumberAndSemiyear);

        });

        System.out.println("Serverul local rulează pe portul 4567...");
    }
}
