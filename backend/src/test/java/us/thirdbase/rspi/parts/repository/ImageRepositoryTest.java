package us.thirdbase.rspi.parts.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import us.thirdbase.rspi.parts.model.Image;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    private ImageRepository subject;

    @Test
    void viewImages() {
        List<Image> actual = subject.findAll();

        assertThat(actual.size()).isEqualTo(93);
    }
}
