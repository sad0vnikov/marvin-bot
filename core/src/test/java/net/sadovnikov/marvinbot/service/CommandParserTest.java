package net.sadovnikov.marvinbot.service;



import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.service.CommandParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandParserTest {
    
    @Test
    public void testNotCommand() {
        CommandParser parser = new CommandParser("!");
        assertFalse(parser.getIsCommand("this shouldn't be parsed as a command"));
        assertFalse(parser.getIsCommand(".and this to"));
    }
    
    @Test
    public void testWithoutArgs() {
        CommandParser parser = new CommandParser("!");
        assertTrue(parser.getIsCommand("!command"));
        Command command = parser.parse("!command");
        assertEquals("command", command.getCommand());

        parser = new CommandParser(".");
        assertFalse(parser.getIsCommand("!command"));
        assertTrue(parser.getIsCommand(".command"));
        command = parser.parse(".command");
        assertEquals("command", command.getCommand());
    }

    @Test
    public void testArgs() {
        CommandParser parser = new CommandParser("!");
        String cmdString = "!command test-action test-arg-1 test-arg-2";
        assertTrue(parser.getIsCommand(cmdString));
        Command command = parser.parse(cmdString);
        assertEquals("command", command.getCommand());
        assertArrayEquals(new String[]{"test-action", "test-arg-1", "test-arg-2"}, command.getArgs());

        assertTrue(command.action().isPresent());
        assertEquals("test-action", command.action().get());
        assertTrue(command.actionArgs().isPresent());
        assertArrayEquals(new String[]{"test-arg-1", "test-arg-2"}, command.actionArgs().get());


        cmdString = "!command test-action";
        command = parser.parse(cmdString);
        assertTrue(command.action().isPresent());
        assertEquals("test-action", command.action().get());
        assertFalse(command.actionArgs().isPresent());

        cmdString = "!command";
        command = parser.parse(cmdString);
        assertFalse(command.action().isPresent());
        assertFalse(command.actionArgs().isPresent());
    }
}
