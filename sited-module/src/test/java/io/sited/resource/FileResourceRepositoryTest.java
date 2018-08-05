package io.sited.resource;

import com.google.common.io.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class FileResourceRepositoryTest {
    FileResourceRepository repository;

    @BeforeEach
    public void setup() {
        repository = new FileResourceRepository(Files.createTempDir().toPath());
        repository.create(new StringResource("template/1.html", "1"));
        repository.create(new StringResource("template/2.html", "2"));
    }

    @AfterEach
    public void clean() {
        repository.delete();
    }

    @Test
    void list() {
        List<Resource> list = repository.list("template/");
        assertEquals(2, list.size());
    }
}