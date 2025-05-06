package org.example.timetableassistant.service;

import org.example.timetableassistant.database.crud.ClassType;
import org.example.timetableassistant.model.Discipline;
import org.example.timetableassistant.model.DisciplineAllocation;
import org.example.timetableassistant.model.Teacher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class DisciplineAllocationService {
    private static final String BASE_URL = "http://localhost:4567/db/discipline-allocation";

    public static String createDisciplineAllocation(int disciplineId, int teacherId, int classTypeId, int hoursPerWeek) throws Exception {
        URL url = new URI(BASE_URL + "?discipline_id=" + disciplineId +
                                        "&teacher_id=" + teacherId +
                                        "&class_type_id=" + classTypeId +
                                        "&hours_per_week=" + hoursPerWeek)
                        .toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return "Discipline Allocation created successfully.";
        } else {
            throw new Exception("Failed to create discipline allocation. HTTP error code: " + responseCode);
        }
    }

    public static List<DisciplineAllocation> getAllDisciplineAllocations() throws Exception {
        URL url = new URI(BASE_URL + "/get-all").toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray daArray = jsonResponse.getJSONArray("message");
            List<DisciplineAllocation> das = new ArrayList<>();
            for (int i = 0; i < daArray.length(); i++) {
                JSONObject roomObj = daArray.getJSONObject(i);
                int id = roomObj.getInt("id");
                Discipline discipline = DisciplineService.getDisciplineById(roomObj.getInt("discipline_id"));
                Teacher teacher = TeacherService.getTeacherById(roomObj.getInt("teacher_id"));
                ClassType classType = ClassType.fromInt(roomObj.getInt("class_type_id"));
                das.add(new DisciplineAllocation(id, discipline, teacher, classType));
            }
            return das;
        } else {
            throw new Exception("Failed to get teachers. HTTP error code: " + responseCode);
        }
    }

    public static List<DisciplineAllocation> getByTeacherId(int id) throws Exception {
        return null;
    }
}
