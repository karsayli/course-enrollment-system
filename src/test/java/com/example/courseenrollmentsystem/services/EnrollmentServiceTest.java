package com.example.courseenrollmentsystem.services;

import com.example.courseenrollmentsystem.dao.CourseDao;
import com.example.courseenrollmentsystem.dao.EnrollmentDao;
import com.example.courseenrollmentsystem.dao.StudentDao;
import com.example.courseenrollmentsystem.dataModel.Course;
import com.example.courseenrollmentsystem.dataModel.Enrollment;
import com.example.courseenrollmentsystem.dataModel.Student;
import com.example.courseenrollmentsystem.dataModel.enums.EnrollmentStatus;
import com.example.courseenrollmentsystem.dto.EnrollmentDTO;
import com.example.courseenrollmentsystem.service.serviceImplementation.EnrollmentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EnrollmentServiceTest {

    private EnrollmentDao enrollmentDao;
    private StudentDao studentDao;
    private CourseDao courseDao;
    private EnrollmentServiceImpl service;

    @BeforeEach
    void setup() {
        enrollmentDao = mock(EnrollmentDao.class);
        studentDao = mock(StudentDao.class);
        courseDao = mock(CourseDao.class);

        service = new EnrollmentServiceImpl(enrollmentDao, studentDao, courseDao);
    }

    /* -------------------------------------------------------------------------
       CREATE
    ------------------------------------------------------------------------- */

    @Test
    void testCreateEnrollmentSuccessfully() {
        Student student = new Student(); student.setId(1L);
        Course course = new Course(); course.setId(10L);
        course.setCapacity(5);
        course.setEnrollments(new ArrayList<>());

        Enrollment saved = new Enrollment();
        saved.setId(100L);
        saved.setStudent(student);
        saved.setCourse(course);
        saved.setStatus(EnrollmentStatus.ENROLLED);

        EnrollmentDTO dto = EnrollmentDTO.builder()
                .studentId(1L)
                .courseId(10L)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        when(studentDao.findById(1L)).thenReturn(Optional.of(student));
        when(courseDao.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentDao.save(any(Enrollment.class))).thenReturn(saved);

        EnrollmentDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(1L, result.getStudentId());
        assertEquals(10L, result.getCourseId());
        assertEquals(EnrollmentStatus.ENROLLED, result.getStatus());

        verify(enrollmentDao).save(any(Enrollment.class));
    }

    @Test
    void testCreateEnrollmentFailsWhenStudentNotFound() {
        EnrollmentDTO dto = EnrollmentDTO.builder()
                .studentId(1L)
                .courseId(10L)
                .build();

        when(studentDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.create(dto));
    }

    @Test
    void testCreateEnrollmentFailsWhenCourseNotFound() {
        Student student = new Student(); student.setId(1L);

        EnrollmentDTO dto = EnrollmentDTO.builder()
                .studentId(1L)
                .courseId(10L)
                .build();

        when(studentDao.findById(1L)).thenReturn(Optional.of(student));
        when(courseDao.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.create(dto));
    }

    @Test
    void testCreateFailsWhenCapacityReached() {
        Student student = new Student(); student.setId(1L);

        Course course = new Course();
        course.setId(10L);
        course.setCapacity(1);

        Enrollment existing = new Enrollment();
        course.setEnrollments(List.of(existing)); // already full

        EnrollmentDTO dto = EnrollmentDTO.builder()
                .studentId(1L)
                .courseId(10L)
                .status(EnrollmentStatus.ENROLLED)
                .build();

        when(studentDao.findById(1L)).thenReturn(Optional.of(student));
        when(courseDao.findById(10L)).thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () -> service.create(dto));
    }

    /* -------------------------------------------------------------------------
       UPDATE
    ------------------------------------------------------------------------- */

    @Test
    void testUpdateEnrollment() {
        Enrollment existing = new Enrollment();
        existing.setId(50L);

        Student s = new Student();
        s.setId(3L);
        existing.setStudent(s);

        Course c = new Course();
        c.setId(5L);
        existing.setCourse(c);

        EnrollmentDTO dto = EnrollmentDTO.builder()
                .id(50L)
                .status(EnrollmentStatus.DROPPED)
                .finalGrade("A")
                .enrolledAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .droppedAt(LocalDateTime.now())
                .build();

        when(enrollmentDao.findById(50L)).thenReturn(Optional.of(existing));
        when(enrollmentDao.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EnrollmentDTO updated = service.update(dto);

        assertEquals("A", updated.getFinalGrade());
        assertEquals(EnrollmentStatus.DROPPED, updated.getStatus());
    }

    @Test
    void testUpdateFailsWhenEnrollmentNotFound() {
        EnrollmentDTO dto = EnrollmentDTO.builder().id(999L).build();
        when(enrollmentDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(dto));
    }

    /* -------------------------------------------------------------------------
       FINDERS
    ------------------------------------------------------------------------- */

    @Test
    void testFindByStudent() {
        Enrollment e = new Enrollment();
        e.setId(1L);
        Student s = new Student(); s.setId(3L);
        Course c = new Course(); c.setId(5L);
        e.setStudent(s);
        e.setCourse(c);

        when(enrollmentDao.findByStudent(3L)).thenReturn(List.of(e));

        List<EnrollmentDTO> result = service.findByStudent(3L);

        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getStudentId());
        assertEquals(5L, result.get(0).getCourseId());
    }

    @Test
    void testFindByCourse() {
        Enrollment e = new Enrollment();
        e.setId(1L);
        Student s = new Student(); s.setId(3L);
        Course c = new Course(); c.setId(5L);
        e.setStudent(s);
        e.setCourse(c);

        when(enrollmentDao.findByCourse(5L)).thenReturn(List.of(e));

        List<EnrollmentDTO> result = service.findByCourse(5L);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getCourseId());
    }

    /* -------------------------------------------------------------------------
       EXISTS
    ------------------------------------------------------------------------- */

    @Test
    void testExistsByStudentAndCourse() {
        when(enrollmentDao.existsByStudentAndCourse(1L, 10L)).thenReturn(true);
        assertTrue(service.existsByStudentAndCourse(1L, 10L));
    }

    /* -------------------------------------------------------------------------
       DELETE
    ------------------------------------------------------------------------- */

    @Test
    void testDelete() {
        service.delete(100L);
        verify(enrollmentDao).deleteById(100L);
    }
}
