package com.example.courseenrollmentsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CourseenrollmentsystemApplicationTests {

    @Test
    public void contextLoads() {
        // This test will pass if the application context loads successfully
    }

    @Test
    public void simpleSanityTest() {
        int expected = 5;
        int actual = 2 + 3;
        assertThat(actual).isEqualTo(expected);
    }
}
