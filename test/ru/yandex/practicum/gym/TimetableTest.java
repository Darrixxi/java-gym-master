package ru.yandex.practicum.gym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
        assertEquals(1, mondaySessions.size(), "За понедельник должно вернуться одно занятие");
        assertEquals(singleTrainingSession, mondaySessions.get(0));
        assertTrue(tuesdaySessions.isEmpty(), "За вторник не должно быть занятий");
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());

        List<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursday.size());
        assertEquals(new TimeOfDay(13, 0), thursday.get(0).getTimeOfDay());
        assertEquals(new TimeOfDay(20, 0), thursday.get(1).getTimeOfDay());

        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
        // Проверить, что за понедельник вернулось одно занятие
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        // Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> at13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, at13.size());
        assertEquals(singleTrainingSession, at13.get(0));

        List<TrainingSession> at14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(at14.isEmpty());
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        //Проверить, что за понедельник в 14:00 не вернулось занятий
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Пётр", "Петрович");
        Group group1 = new Group("Группа 1", Age.CHILD, 60);
        Group group2 = new Group("Группа 2", Age.ADULT, 90);

        TrainingSession session1 = new TrainingSession(group1, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2, DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        assertEquals(2, sessions.size(), "В одно время могут быть две тренировки");
        assertTrue(sessions.contains(session1));
        assertTrue(sessions.contains(session2));
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty());
        assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(9, 0)).isEmpty());
        assertTrue(timetable.getCountByCoaches().isEmpty());
    }

    @Test
    void testOrderingWithinDay() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Йога", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(18, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(15, 30)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));

        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(4, tuesday.size());

        assertEquals(10, tuesday.get(0).getTimeOfDay().getHours());
        assertEquals(10, tuesday.get(1).getTimeOfDay().getHours());
        assertEquals(15, tuesday.get(2).getTimeOfDay().getHours());
        assertEquals(18, tuesday.get(3).getTimeOfDay().getHours());
    }

    @Test
    void testGetCountByCoaches() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        Group group = new Group("Пилатес", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.FRIDAY, new TimeOfDay(11, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(15, 0)));

        Map<Coach, Integer> counts = timetable.getCountByCoaches();

        assertEquals(2, counts.size());

        Coach[] coaches = counts.keySet().toArray(new Coach[0]);
        assertEquals(coach1, coaches[0], "Первым должен идти тренер с наибольшим количеством занятий");
        assertEquals(3, counts.get(coach1));
        assertEquals(1, counts.get(coach2));
    }
}
