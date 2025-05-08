package org.example.timetableassistant.service;

import org.example.timetableassistant.database.OperationResult;
import org.example.timetableassistant.database.crud.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Exporter {
    TeacherCRUD teacherCRUD = new TeacherCRUD();
    ClassCRUD classCRUD = new ClassCRUD();
    public Exporter(){}
    public String generateMainPage(){
        return "<html lang=\"ro\">\n" +
                "    <head>\n" +
                "        <title>\n" +
                "            Orar main\n" +
                "        </title>\n" +
                "    </head>\n" +
                "    \n" +
                "    <body><h1>Orar main</h1>\n" +
                "    \n" +
                "    <h2>Orar main</h2>\n" +
                "    \n" +
                "    <ul>\n" +
                "    <li><a href=\"orar_studenti.html\">Studenti</a>\n" +
                "    </li><li><a href=\"orar_profesori.html\">Profesori</a>\n" +
                "    </li><li><a href=\"orar_resurse.html\">Sali</a>\n" +
                "    </li><li><a href=\"orar_discipline.html\">Discipline de studiu</a>\n" +
                "    </li></ul>\n" +
                "    </body>\n" +
                "</html>";
    }
    public String generateStudentsPage(List<String> groups){
        String result = "<html lang=\"ro\">\n" +
                "    <head>\n" +
                "        <title>\n" +
                "            Orar studenti\n" +
                "        </title>\n" +
                "    </head>\n" +
                "    \n" +
                "    <body><h1>Orar studenti</h1>\n" +
                "     <ul>";
        for(String group : groups){
            group.replace(" ", "_");
            result += "<li><a href=orar_" + group + ".html\">" + group + "</a></li>";
        }
        result += "</ul></body></hmtl>";
        return result;
    }
    public String generateProfessorsPage(List<String> profs){
        String result = "<html lang=\"ro\">\n" +
                "    <head>\n" +
                "        <title>\n" +
                "            Orar profesori\n" +
                "        </title>\n" +
                "    </head>\n" +
                "    \n" +
                "    <body><h1>Orar profesori</h1>\n" +
                "     <ul>";
        for(String prof : profs){
            prof.replace(" ", "_");
            result += "<li><a href=orar_" + prof + ".html\">" + prof + "</a></li>";
        }
        result += "</ul></body></hmtl>";
        return result;
    }
    public String generateDisciplinesPage(List<String> disciplines){
        String result = "<html lang=\"ro\">\n" +
                "    <head>\n" +
                "        <title>\n" +
                "            Orar discipline\n" +
                "        </title>\n" +
                "    </head>\n" +
                "    \n" +
                "    <body><h1>Orar discipline</h1>\n" +
                "     <ul>";
        for(String d : disciplines){
            d.replace(" ", "_");
            result += "<li><a href=orar_" + d + ".html\">" + d + "</a></li>";
        }
        result += "</ul></body></hmtl>";
        return result;
    }
    public String generateRoomsPage(List<String> rooms){
        String result = "<html lang=\"ro\">\n" +
                "    <head>\n" +
                "        <title>\n" +
                "            Orar sali\n" +
                "        </title>\n" +
                "    </head>\n" +
                "    \n" +
                "    <body><h1>Orar sali</h1>\n" +
                "     <ul>";
        for(String r : rooms){
            r.replace(" ", "_");
            result += "<li><a href=orar_" + r + ".html\">" + r + "</a></li>";
        }
        result += "</ul></body></hmtl>";
        return result;
    }
    public void generateTeacherTimetable() {
        DisciplineCRUD disciplineCRUD = new DisciplineCRUD();
        RoomCRUD roomCRUD = new RoomCRUD();
        TimeSlotCRUD timeSlotCRUD = new TimeSlotCRUD();
        GroupCRUD groupCRUD = new GroupCRUD();

        OperationResult allTeachersResult = teacherCRUD.getAllTeachers();
        if (!allTeachersResult.success) {
            System.out.println("Eroare la preluarea profesorilor: " + allTeachersResult.message);
            return;
        }

        List<HashMap> teachers = (List<HashMap>) allTeachersResult.message;

        for (HashMap teacher : teachers) {
            String teacherName = (String) teacher.get("name");
            Integer teacherId = (Integer) teacher.get("id");

            StringBuilder html = new StringBuilder();
            html.append("<html lang=\"ro\">\n<head><meta charset=\"UTF-8\">\n<title>Orar Profesor ")
                    .append(teacherName)
                    .append("</title></head>\n<body>\n<h1>Orar pentru profesor: ")
                    .append(teacherName)
                    .append("</h1>\n<table border='1' cellspacing='0' cellpadding='5'>\n<thead>\n<tr>")
                    .append("<th>Disciplina</th><th>Tip</th><th>Sala</th><th>Zi</th><th>Interval orar</th><th>Grupa</th>")
                    .append("</tr>\n</thead>\n<tbody>\n");

            OperationResult classesResult = classCRUD.getClassesByTeacherId(teacherId);
            if (!classesResult.success || classesResult.message == null) {
                html.append("<tr><td colspan='6' align='center'>-</td></tr>\n");
            } else {
                List<HashMap> classes = (List<HashMap>) classesResult.message;
                if (classes.isEmpty()) {
                    html.append("<tr><td colspan='6' align='center'>-</td></tr>\n");
                } else {
                    for (HashMap cls : classes) {
                        String disciplina = getEntityName(cls.get("discipline_id"), disciplineCRUD);
                        String sala = getEntityName(cls.get("room_id"), roomCRUD);
                        String[] time = getTimeSlot(cls.get("time_slot_id"), timeSlotCRUD);
                        String grupa = getGroupName(cls.get("group_id"), groupCRUD);
                        String classType = (String) cls.getOrDefault("class_type", "-");

                        html.append("<tr>")
                                .append("<td>").append(emptyOrDash(disciplina)).append("</td>")
                                .append("<td>").append(emptyOrDash(classType)).append("</td>")
                                .append("<td>").append(emptyOrDash(sala)).append("</td>")
                                .append("<td>").append(emptyOrDash(time[0])).append("</td>")
                                .append("<td>").append(emptyOrDash(time[1])).append("</td>")
                                .append("<td>").append(emptyOrDash(grupa)).append("</td>")
                                .append("</tr>\n");
                    }
                }
            }

            html.append("</tbody>\n</table>\n</body>\n</html>");

            // Salvăm fișierul HTML
            String fileName = "orar_" + teacherName.replace(" ", "_") + ".html";
            saveHtmlToFile(fileName, html.toString());
            System.out.println("Fișier generat: " + fileName);
        }
    }


    private String getEntityName(Object id, Object crud) {
        if (id == null) return "";
        OperationResult result = null;

        if (crud instanceof DisciplineCRUD) {
            result = ((DisciplineCRUD) crud).getDisciplineById((int) id);
        } else if (crud instanceof RoomCRUD) {
            result = ((RoomCRUD) crud).getRoomById((int) id);
        }

        if (result != null && result.success) {
            HashMap data = (HashMap) result.message;
            return (String) data.getOrDefault("name", "");
        }
        return "";
    }

    private String[] getTimeSlot(Object id, TimeSlotCRUD crud) {
        if (id == null) return new String[]{"", ""};
        OperationResult result = crud.getTimeSlotById((int) id);
        if (result.success) {
            HashMap data = (HashMap) result.message;
            String day = (String) data.getOrDefault("day_of_week", "");
            String time = data.getOrDefault("start_time", "") + " - " + data.getOrDefault("end_time", "");
            return new String[]{day, time};
        }
        return new String[]{"", ""};
    }

    private String getGroupName(Object id, GroupCRUD crud) {
        if (id == null) return "";
        OperationResult result = crud.getGroupById((int) id);
        if (result.success) {
            HashMap data = (HashMap) result.message;
            return (String) data.getOrDefault("name", "");
        }
        return "";
    }

    private void saveHtmlToFile(String filename, String content) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filename), content.getBytes());
        } catch (IOException e) {
            System.out.println("Eroare la salvarea fișierului: " + filename);
            e.printStackTrace();
        }
    }

    private String emptyOrDash(String value) {
        return (value == null || value.trim().isEmpty()) ? "-" : value;
    }


//    public void generateDisciplineTimetable() {
//        DisciplineCRUD disciplineCRUD = new DisciplineCRUD();
//        RoomCRUD roomCRUD = new RoomCRUD();
//        TimeSlotCRUD timeSlotCRUD = new TimeSlotCRUD();
//        GroupCRUD groupCRUD = new GroupCRUD();
//        TeacherCRUD teacherCRUD = new TeacherCRUD();
//
//        OperationResult allDisciplinesResult = disciplineCRUD.getAllDisciplines();
//        if (!allDisciplinesResult.success) {
//            System.out.println("Eroare la preluarea disciplinelor: " + allDisciplinesResult.message);
//            return;
//        }
//
//        List<HashMap> disciplines = (List<HashMap>) allDisciplinesResult.message;
//
//        for (HashMap discipline : disciplines) {
//            String disciplineName = (String) discipline.get("name");
//            Integer disciplineId = (Integer) discipline.get("id");
//
//            StringBuilder html = new StringBuilder();
//            html.append("<html lang=\"ro\">\n<head><meta charset=\"UTF-8\">\n<title>Orar Disciplina ")
//                    .append(disciplineName)
//                    .append("</title></head>\n<body>\n<h1>Orar pentru disciplina: ")
//                    .append(disciplineName)
//                    .append("</h1>\n<table border='1' cellspacing='0' cellpadding='5'>\n<thead>\n<tr>")
//                    .append("<th>Profesor</th><th>Tip</th><th>Sala</th><th>Zi</th><th>Interval orar</th><th>Grupa</th>")
//                    .append("</tr>\n</thead>\n<tbody>\n");
//
//            OperationResult classesResult = classCRUD.getClassesByDisciplineId(disciplineId);
//            if (!classesResult.success || classesResult.message == null) {
//                html.append("<tr><td colspan='6' align='center'></td></tr>\n");
//            } else {
//                List<HashMap> classes = (List<HashMap>) classesResult.message;
//                if (classes.isEmpty()) {
//                    html.append("<tr><td colspan='6' align='center'></td></tr>\n");
//                } else {
//                    for (HashMap cls : classes) {
//                        String profesor = getEntityName(cls.get("teacher_id"), teacherCRUD);
//                        String sala = getEntityName(cls.get("room_id"), roomCRUD);
//                        String[] time = getTimeSlot(cls.get("time_slot_id"), timeSlotCRUD);
//                        String grupa = getGroupName(cls.get("group_id"), groupCRUD);
//                        String classType = (String) cls.getOrDefault("class_type", "");
//
//                        html.append("<tr>")
//                                .append("<td>").append(emptyOrDash(profesor)).append("</td>")
//                                .append("<td>").append(emptyOrDash(classType)).append("</td>")
//                                .append("<td>").append(emptyOrDash(sala)).append("</td>")
//                                .append("<td>").append(emptyOrDash(time[0])).append("</td>")
//                                .append("<td>").append(emptyOrDash(time[1])).append("</td>")
//                                .append("<td>").append(emptyOrDash(grupa)).append("</td>")
//                                .append("</tr>\n");
//                    }
//                }
//            }
//
//            html.append("</tbody>\n</table>\n</body>\n</html>");
//
//            // Salvăm fișierul HTML
//            String fileName = "orar_" + disciplineName.replace(" ", "_") + ".html";
//            saveHtmlToFile(fileName, html.toString());
//            System.out.println("Fișier generat: " + fileName);
//        }
//    }

    public void generateRoomTimetable() {
        RoomCRUD roomCRUD = new RoomCRUD();
        ClassCRUD classCRUD = new ClassCRUD();
        DisciplineCRUD disciplineCRUD = new DisciplineCRUD();
        TimeSlotCRUD timeSlotCRUD = new TimeSlotCRUD();
        GroupCRUD groupCRUD = new GroupCRUD();
        TeacherCRUD teacherCRUD = new TeacherCRUD();

        OperationResult allRoomsResult = roomCRUD.getAllRooms();
        if (!allRoomsResult.success) {
            System.out.println("Eroare la preluarea sălilor: " + allRoomsResult.message);
            return;
        }

        List<HashMap> rooms = (List<HashMap>) allRoomsResult.message;

        for (HashMap room : rooms) {
            String roomName = (String) room.get("name");
            Integer roomId = (Integer) room.get("id");

            StringBuilder html = new StringBuilder();
            html.append("<html lang=\"ro\">\n<head><meta charset=\"UTF-8\">\n<title>Orar Sala ")
                    .append(roomName)
                    .append("</title></head>\n<body>\n<h1>Orar pentru sala: ")
                    .append(roomName)
                    .append("</h1>\n<table border='1' cellspacing='0' cellpadding='5'>\n<thead>\n<tr>")
                    .append("<th>Profesor</th><th>Disciplina</th><th>Tip</th><th>Zi</th><th>Interval orar</th><th>Grupa</th>")
                    .append("</tr>\n</thead>\n<tbody>\n");

            OperationResult classesResult = classCRUD.getClassesByRoomId(roomId);
            if (!classesResult.success || classesResult.message == null) {
                html.append("<tr><td colspan='6' align='center'></td></tr>\n");
            } else {
                List<HashMap> classes = (List<HashMap>) classesResult.message;
                if (classes.isEmpty()) {
                    html.append("<tr><td colspan='6' align='center'></td></tr>\n");
                } else {
                    for (HashMap cls : classes) {
                        String profesor = getTeacherName(cls.get("teacher_id"), teacherCRUD);  // Preluăm numele profesorului
                        String disciplina = getEntityName(cls.get("discipline_id"), disciplineCRUD);
                        String grupa = getGroupName(cls.get("group_id"), groupCRUD);
                        String classType = (String) cls.getOrDefault("class_type", "");
                        String[] time = getTimeSlot(cls.get("time_slot_id"), timeSlotCRUD);

                        html.append("<tr>")
                                .append("<td>").append(emptyOrDash(profesor)).append("</td>")
                                .append("<td>").append(emptyOrDash(disciplina)).append("</td>")
                                .append("<td>").append(emptyOrDash(classType)).append("</td>")
                                .append("<td>").append(emptyOrDash(time[0])).append("</td>")
                                .append("<td>").append(emptyOrDash(time[1])).append("</td>")
                                .append("<td>").append(emptyOrDash(grupa)).append("</td>")
                                .append("</tr>\n");
                    }
                }
            }

            html.append("</tbody>\n</table>\n</body>\n</html>");

            // Salvăm fișierul HTML
            String fileName = "orar_" + roomName.replace(" ", "_") + ".html";
            saveHtmlToFile(fileName, html.toString());
            System.out.println("Fișier generat: " + fileName);
        }
    }

    private String getTeacherName(Object id, TeacherCRUD teacherCRUD) {
        if (id == null) return "";
        OperationResult result = teacherCRUD.getTeacherById((int) id);
        if (result.success) {
            HashMap data = (HashMap) result.message;
            return (String) data.getOrDefault("name", "");
        }
        return "";
    }


    public void generateGroupTimetable() {
        GroupCRUD groupCRUD = new GroupCRUD();
        ClassCRUD classCRUD = new ClassCRUD();
        DisciplineCRUD disciplineCRUD = new DisciplineCRUD();
        TimeSlotCRUD timeSlotCRUD = new TimeSlotCRUD();
        RoomCRUD roomCRUD = new RoomCRUD();
        TeacherCRUD teacherCRUD = new TeacherCRUD();

        OperationResult allGroupsResult = groupCRUD.getAllGroups();
        if (!allGroupsResult.success) {
            System.out.println("Eroare la preluarea grupelor: " + allGroupsResult.message);
            return;
        }

        List<HashMap> groups = (List<HashMap>) allGroupsResult.message;

        for (HashMap group : groups) {
            String groupName = (String) group.get("semiyear") + group.get("number");
            Integer groupId = (Integer) group.get("id");

            StringBuilder html = new StringBuilder();
            html.append("<html lang=\"ro\">\n<head><meta charset=\"UTF-8\">\n<title>Orar Grupa ")
                    .append(groupName)
                    .append("</title></head>\n<body>\n<h1>Orar pentru grupa: ")
                    .append(groupName)
                    .append("</h1>\n<table border='1' cellspacing='0' cellpadding='5'>\n<thead>\n<tr>")
                    .append("<th>Profesor</th><th>Disciplina</th><th>Tip</th><th>Zi</th><th>Interval orar</th><th>Sala</th>")
                    .append("</tr>\n</thead>\n<tbody>\n");

            OperationResult classesResult = classCRUD.getClassesByGroupId(groupId);
            if (!classesResult.success || classesResult.message == null) {
                html.append("<tr><td colspan='6' align='center'></td></tr>\n");
            } else {
                List<HashMap> classes = (List<HashMap>) classesResult.message;
                if (classes.isEmpty()) {
                    html.append("<tr><td colspan='6' align='center'></td></tr>\n");
                } else {
                    for (HashMap cls : classes) {
                        String profesor = getTeacherName(cls.get("teacher_id"), teacherCRUD);  // Preluăm numele profesorului
                        String disciplina = getEntityName(cls.get("discipline_id"), disciplineCRUD);
                        String sala = getEntityName(cls.get("room_id"), roomCRUD);
                        String classType = (String) cls.getOrDefault("class_type", "");
                        String[] time = getTimeSlot(cls.get("time_slot_id"), timeSlotCRUD);

                        html.append("<tr>")
                                .append("<td>").append(emptyOrDash(profesor)).append("</td>")
                                .append("<td>").append(emptyOrDash(disciplina)).append("</td>")
                                .append("<td>").append(emptyOrDash(classType)).append("</td>")
                                .append("<td>").append(emptyOrDash(time[0])).append("</td>")
                                .append("<td>").append(emptyOrDash(time[1])).append("</td>")
                                .append("<td>").append(emptyOrDash(sala)).append("</td>")
                                .append("</tr>\n");
                    }
                }
            }

            html.append("</tbody>\n</table>\n</body>\n</html>");

            // Salvăm fișierul HTML
            String fileName = "orar_grupa_" + groupName.replace(" ", "_") + ".html";
            saveHtmlToFile(fileName, html.toString());
            System.out.println("Fișier generat: " + fileName);
        }
    }


}
