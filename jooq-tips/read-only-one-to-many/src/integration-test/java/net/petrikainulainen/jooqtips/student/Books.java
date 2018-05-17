package net.petrikainulainen.jooqtips.student;

/**
 * This class contains the test data that is inserted
 * into the <code>books</code> database table.
 */
final class Books {

    /**
     * Prevents instantiation.
     */
    private Books() {}

    static final class LearnJavaIn21Days {

        static final Long ID = 1L;
        static final String NAME = "Learn Java in 21 Days";
    }

    static final class EffectiveJava {

        static final Long ID = 2L;
        static final String NAME = "Effective Java";
    }

    static final class LearnCPlusPlusIn21Days {

        static final Long ID = 3L;
        static final String NAME = "Learn C++ in 21 Days";
    }

    static final class TheCPlusPlusProgrammingLanguage {

        static final Long ID = 4L;
        static final String NAME = "The C++ Programming Language";
    }
}
