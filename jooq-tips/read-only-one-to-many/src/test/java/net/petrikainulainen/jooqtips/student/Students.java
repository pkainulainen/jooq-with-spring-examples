package net.petrikainulainen.jooqtips.student;

/**
 * A constant class which contains the test data that's found
 * from the database when our integration tests are run.
 */
final class Students {

    static final Long UNKNOWN_ID = 999L;

    /**
     * Prevents instantiation.
     */
    private Students() {}

    /**
     * Contains the test data of a student whose name is
     * Petri Kainulainen.
     */
    static class PetriKainulainen {

        static final Long ID = 1L;
        static final String NAME = "Petri Kainulainen";

        static class Books {

            static final int COUNT = 2;

            static class LearnJavaIn21Days {

                static final Long ID = 1L;
                static final String NAME = "Learn Java in 21 Days";
            }

            static class EffectiveJava {

                static final Long ID = 2L;
                static final String NAME = "Effective Java";
            }
        }
    }

    /**
     * Contains the test data of a student whose name is
     * Lukas.
     */
    static class Lukas {

        static final Long ID = 2L;
        static final String NAME = "Lukas";

        static class Books {

            static final int COUNT = 1;

            static class JOOQInAction {

                static final Long ID = 3L;
                static final String NAME = "jOOQ in Action";
            }
        }
    }
}
