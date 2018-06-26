package io.sited.database.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class SQLBuilderTest {
    @Test
    void sort() {
        assertEquals("SELECT t FROM Entity t ORDER BY name DESC", new SQLBuilder("Entity", "id", "SELECT t FROM Entity t").sort("name", true).selectSQL());
    }

    @Test
    void count() {
        assertEquals("SELECT count(0) FROM Entity t", new SQLBuilder("Entity", "id", "SELECT t FROM Entity t").sort("name", true).countSQL());
    }

    @Test
    void deleteById() {
        assertEquals("DELETE FROM Entity t WHERE t.id=?", new SQLBuilder("Entity", "id", "").deleteByIdSQL());
    }

    @Test
    void deleteByIds() {
        assertEquals("DELETE FROM Entity t WHERE t.id in (?,?,?,?,?) OR t.id in (?,?,?,?,?) OR t.id in (?)", new SQLBuilder("Entity", "id", "").setBatchSize(5).deleteByIdsSQL(11));
    }

    @Test
    void findByIds() {
        assertEquals("SELECT t FROM Entity t WHERE t.id in (?,?,?,?,?) OR t.id in (?,?,?,?,?) OR t.id in (?)", new SQLBuilder("Entity", "id", "").setBatchSize(5).findByIdsSQL(11));
    }
}