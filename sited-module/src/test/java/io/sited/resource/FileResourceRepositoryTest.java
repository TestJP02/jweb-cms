package io.sited.resource;

import com.google.common.io.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class FileResourceRepositoryTest {
    FileResourceRepository repository;

    @BeforeEach
    public void setup() throws IOException {
        File dir = Files.createTempDir();
        new File(dir, "template").mkdir();
        new File(dir, "template/1.html").createNewFile();
        new File(dir, "template/2.html").createNewFile();
        repository = new FileResourceRepository(dir.toPath());
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