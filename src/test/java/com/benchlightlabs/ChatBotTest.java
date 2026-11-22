package com.benchlightlabs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatBotTest {
   private final ChatBot bot = new ChatBot();

   @Test
    void greetsWhenSaidHello() {
       assertEquals("Hello! How can I help?", bot.reply("Hello there"));
   }

   @Test
    void promptsOnBlankInput() {
       assertEquals("Say something!", bot.reply("   "));
   }

    @Test
    void echoesOtherMessages() {
        assertEquals("You said: weather", bot.reply("weather"));
    }

}
