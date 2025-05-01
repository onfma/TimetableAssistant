package org.example.timetableassistant;
import org.example.timetableassistant.database.handler.*;

import static spark.Spark.*;

public class DatabaseServer {
    public static void startServer() {
        port(4567); // setează portul serverului

        path("/db", () -> {
            // TimeSlots Handlers
            post("/time-slot", (req, res) -> TimeSlotHandler.createTimeSlot(req, res));
            get("/time-slot/:id", (req, res) -> TimeSlotHandler.getTimeSlotById(req, res));
            put("/time-slot/:id", (req, res) -> TimeSlotHandler.updateTimeSlot(req, res));
            delete("/time-slot/:id", (req, res) -> TimeSlotHandler.deleteTimeSlot(req, res));

            // Class Handlers
            post("/class", (req, res) -> ClassHandler.createClass(req, res));
            get("/class/:id", (req, res) -> ClassHandler.getClassById(req, res));
            put("/class/:id", (req, res) -> ClassHandler.updateClass(req, res));
            delete("/class/:id", (req, res) -> ClassHandler.deleteClass(req, res));

            // ClassTypes Handlers
            post("/class-type", (req, res) -> ClassTypeHandler.createClassType(req, res));
            get("/class-type/:id", (req, res) -> ClassTypeHandler.getClassTypeById(req, res));
            put("/class-type/:id", (req, res) -> ClassTypeHandler.updateClassType(req, res));
            delete("/class-type/:id", (req, res) -> ClassTypeHandler.deleteClassType(req, res));

            // Discipline Handler
            post("/discipline", (req, res) -> DisciplineHandler.createDiscipline(req, res));
            get("/discipline/:id", (req, res) -> DisciplineHandler.getDisciplineById(req, res));
            put("/discipline/:id", (req, res) -> DisciplineHandler.updateDiscipline(req, res));
            delete("/discipline/:id", (req, res) -> DisciplineHandler.deleteDiscipline(req, res));

            // Room Handler
            post("/room", (req, res) -> RoomHandler.createRoom(req, res));
            get("/room/:id", (req, res) -> RoomHandler.getRoomById(req, res));
            put("/room/:id", (req, res) -> RoomHandler.updateRoom(req, res));
            delete("/room/:id", (req, res) -> RoomHandler.deleteRoom(req, res));

            // Student Handler
            post("/student", (req, res) -> StudentHandler.createStudent(req, res));
            get("/student/get-by-id/:id", (req, res) -> StudentHandler.getStudentById(req, res));
            get("/student/get-by-group-name/:group_name", (req, res) -> StudentHandler.getStudentsByGroup(req, res));
            put("/student/:id", (req, res) -> StudentHandler.updateStudent(req, res));
            delete("/student/:id", (req, res) -> StudentHandler.deleteStudent(req, res));

            // Teacher Handler
            post("/teacher", (req, res) -> TeacherHandler.createTeacher(req, res));
            get("/teacher/get-by-id/:id", (req, res) -> TeacherHandler.getTeacherById(req, res));
            get("/teacher/get-by-name/:name", (req, res) -> TeacherHandler.getTeacherByName(req, res));
            put("/teacher/:id", (req, res) -> TeacherHandler.updateTeacher(req, res));
            delete("/teacher/:id", (req, res) -> TeacherHandler.deleteTeacher(req, res));
        });

        System.out.println("Serverul local rulează pe portul 4567...");
    }
}
