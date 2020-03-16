package nl.saxion.playground.squarerootofpopeye.shisharun.entity;

import java.util.Random;

public final class RandomMessage {
    private String[] messages;
    private Random random = new Random();                           // init random at ART init

    private RandomMessage() {
        messages = new String[7];
        addMessages();
    }

    private void addMessages() {
        messages[0] = "Where are you, dude?";
        messages[1] = "Yo, delete!";
        messages[2] = "Get a life!";
        messages[3] = "Get a clue!";
        messages[4] = "We've been waiting for ages!";
        messages[5] = "Please delete...";
        messages[6] = "We're gonna smoke everything without you man!";
    }

    /**
     * method to generate a random number and return the string from the generated index
     */
    public String getRandomMessage() {
        int index = random.nextInt(messages.length);
        return messages[index];
    }

    // Bob plough singleton pattern
    public static RandomMessage getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final RandomMessage INSTANCE = new RandomMessage();
    }
}
