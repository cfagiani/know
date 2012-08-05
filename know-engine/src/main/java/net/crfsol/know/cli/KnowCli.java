package net.crfsol.know.cli;

import net.crfsol.know.KnowEngine;
import net.crfsol.know.core.domain.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Scanner;

/**
 * command-line interface for the Know system. This class will start up a KnowEngine and wait on the command line for commands until the user issues the QUIT command.
 *
 *
 * @author Christopher Fagiani
 */
public class KnowCli {
    private static final String PROMPT = "> ";
    private static final String QUIT_CMD = "quit";
    private static final String ADD_ROOT_CMD = "addRoot";
    private static final String SEARCH_CMD = "search";
    private static final String ADD_TAG_CMD = "addTag";
    private static final String TAG_DOC_CMD = "tagDoc";

    @Inject
    KnowEngine knowEngine;


    /**
     * gets the knowCli instance from the application context and starts the input loop
     * @param args
     */
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/know-engine/engine-context.xml");
        KnowCli cli = (KnowCli) context.getBean("knowCli");
        cli.showWelcome();
        cli.inputLoop();

    }

    /**
     * prints the banner to standard out
     */
    public void showWelcome() {
        System.out.println("KNOW Command Line\n-------------------\n");
    }

    /**
     * displays the command prompt and waits for user input
     */
    public void inputLoop() {

        Scanner scanner = new Scanner(System.in);
        String line = null;
        do {
            System.out.print(PROMPT);
            line = scanner.nextLine();
        } while (handleCommand(line));

    }


    /**
     * handles the commands typed by the user by parsing the input and delegating to the engine for execution.
     *
     * TODO: refactor command handling
     *
     * @param command
     * @return
     */
    private boolean handleCommand(String command) {
        if (QUIT_CMD.equalsIgnoreCase(command.trim())) {
            return false;
        } else {
            command = command.trim();

            String[] tokenizedLine = command.split(" ");
            String remainder = null;
            if (command.contains(" ")) {
                remainder = command.substring(command.indexOf(" ") + 1).trim();
            }
            String cmd = tokenizedLine[0].trim();

            if (ADD_ROOT_CMD.equalsIgnoreCase(cmd)) {
                knowEngine.addResourceRoot(remainder);
            } else if (SEARCH_CMD.equalsIgnoreCase(cmd)) {
                printResources(knowEngine.executeSearch(remainder));
            } else if (ADD_TAG_CMD.equalsIgnoreCase(cmd)) {
                knowEngine.addTag(tokenizedLine[1], tokenizedLine.length > 2 ? tokenizedLine[2] : null);
            } else if (TAG_DOC_CMD.equalsIgnoreCase(cmd)) {
                String loc = command.substring(command.indexOf(" ") + 1).trim();
                loc = loc.substring(loc.indexOf(" ") + 1).trim();
                knowEngine.tagResource(tokenizedLine[1], loc);
            }
        }
        return true;
    }

    /**
     * prints a list of resources to standard out
     * @param resources
     */
    private void printResources(List<Resource> resources) {
        if (resources != null) {
            for (Resource r : resources) {
                System.out.printf("%s\t%s\t%s\n", r.getLocation(), r.getType().getCode(), r.getTagString());
            }
        }
    }


}
