package net.petrikainulainen.jooqtips.student;

/**
 * This class contains the test data that is inserted to the
 * <code>students</code> database table.
 */
final class Students {

    /**
     * Prevents instantiation.
     */
    private Students() {}

    static final Long UNKNOWN_ID = 99L;

    static class PetriKainulainen {

        static final Long ID = 1L;
        static final String NAME = "Petri Kainulainen";
    }

    static class JaneDoe {

        static final Long ID = 2L;
        static final String NAME = "Jane Doe";
    }
}
