# Timetable Assistant

This is the documentation for the Timetable Assistant application, including an overview of the project, its structure, and how to run the application.



## Table of Contents

- [Program Design & Implementation](#program-design--implementation)
- [User Manual](#user-manual)
- [Unit Tests Coverage](#unit-tests-coverage)
- [Use of Assertions](#use-of-assertions)
- [Credits](#credits)



## Program Design & Implementation

This section describes the architecture, components, and functionalities implemented in the Timetable Assistant application.

The application is designed to assist users in creating and managing university timetables by handling the core entities involved: teachers, student groups, disciplines, rooms, class types, and time slots. It does not generate a timetable automatically but provides tools and validation to support manual construction.

### Architecture

The system follows a modular architecture, with each entity managed through its dedicated CRUD module. These modules handle all operations with the database using JDBC:

- **DisciplineCRUD**
- **DisciplineAllocationCRUD**
- **GroupCRUD**
- **SemiyearCRUD**
- **RoomCRUD**
- **RoomTypesCRUD**
- **TeacherCRUD**
- **TimeSlotCRUD**
- **ClassCRUD**
- **ClassTypeCRUD**


### Main Functionalities

- **CRUD Operations**: Add, retrieve, update, and delete records for each entity.
- **Constraint Validation**:
  - Classes are scheduled only on weekdays (Monday–Friday) between 08:00–20:00.
  - Courses must be placed in course rooms; laboratories in lab rooms; seminars have no room restrictions.
  - Course classes target the entire study year; seminars and laboratories target individual groups.
  - Room and teacher overlapping is prevented.
- **Conflict Detection**: Overlapping time slots and incorrect room or teacher assignments are reported.
- **HTML Export**: Generates static HTML timetables formatted similarly to the university’s official schedule.

### Design Considerations

- The implementation avoids the use of external libraries for business logic.
- All constraints and logic are enforced manually through validation code.
- The focus is on correct structure and extensibility, not on automated scheduling.
- GUI elements are considered for future phases to improve user interaction.

### Technology Stack

- **Java** for application logic
- **JDBC** for database operations
- **JUnit 5** and **Mockito** for unit testing and mocking
- **Assertions** for runtime verification of method contracts


## User Manual

text text text



## Unit Tests Coverage

This part covers the unit tests for the CRUD (Create, Read, Update, Delete) operations of the Timetable Assistant application.
The tests use JUnit 5 and Mockito to mock database interactions and verify the behavior of each CRUD class.

### Covered Entities and Operations

- **DisciplineCRUD**
  - Insert, get by ID, update, delete, get all disciplines
  - Handles success, not found, and SQL exception scenarios

- **GroupCRUD**
  - Insert, get by ID, get by number and semiyear, update, delete, get all groups
  - Tests for empty results, not found, and SQL exceptions

- **RoomCRUD**
  - Insert, get by ID, update, delete, get all rooms
  - Covers success, not found, and error cases

- **RoomTypesCRUD**
  - Insert, get by ID, update, delete, get all room types
  - Includes tests for not found and SQL errors

- **TeacherCRUD**
  - Insert, get by ID, update, delete, get all teachers
  - Tests for both successful and failing operations

- **TimeSlotCRUD**
  - Insert, get by ID, update, delete, get all time slots
  - Handles empty, not found, and exception cases

- **ClassCRUD**
  - Insert class (COURSE, LABORATORY, SEMINAR): success, validation failures, SQL exceptions
  - Get class by ID: success, not found, SQL exceptions
  - Update class: success, not found, SQL exceptions
  - Delete class: success, not found, SQL exceptions
  - Get classes by group ID, room ID, semiyear, teacher ID, discipline ID: success, no results, SQL exceptions
  - Get all classes: success (multiple results), no results, SQL exceptions

### Testing Approach

- All tests use Mockito to mock JDBC connections, statements, and result sets.
- Each CRUD method is tested for:
  - Successful execution
  - Handling of not found records
  - SQL exceptions and error messages
- Assertions verify both the operation result and the correctness of returned data or error messages.



## Use of Assertions

text text text



## Credits

* Aron Robert
* Chichirau Claudiu
* Dulhac Alexandru
* Onofrei Maria

_Quality of Software Systems Project
[Masters Year 1 - 2025]_