package net.crfsol.know.cli;

import net.crfsol.know.KnowEngine;
import net.crfsol.know.core.domain.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Scanner;

/**
 * @author Christopher Fagiani
 */
public class KnowCli {
    private static final String PROMPT = "> ";
    private static final String QUIT_CMD = "quit";
    private static final String ADD_ROOT_CMD = "addRoot";
    private static final String SEARCH_CMD = "search";

    @Inject
    KnowEngine knowEngine;


    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/know-engine/engine-context.xml");
        KnowCli cli = (KnowCli) context.getBean("knowCli");
        cli.showWelcome();
        cli.inputLoop();

    }

    public void showWelcome() {
        System.out.println("KNOW Command Line\n-------------------\n");
    }

    public void inputLoop() {

        Scanner scanner = new Scanner(System.in);
        String line = null;
        do {
            System.out.print(PROMPT);
            line = scanner.nextLine();
        } while (handleCommand(line));

    }

    private boolean handleCommand(String command) {
        if (QUIT_CMD.equalsIgnoreCase(command.trim())) {
            return false;
        } else {
            command = command.trim();

            String[] tokenizedLine = command.split(" ");
            String remainder = null;
            if(command.contains(" ")){
                remainder = command.substring(command.indexOf(" ")+1).trim();
            }
            String cmd = tokenizedLine[0].trim();
            //TODO handle commands
            if(ADD_ROOT_CMD.equalsIgnoreCase(cmd)){
                knowEngine.addResourceRoot(remainder);
            }else if (SEARCH_CMD.equalsIgnoreCase(cmd)){
                printResources(knowEngine.executeSearch(remainder));
            }
        }
        return true;
    }

    private void printResources(List<Resource> resources){
        if(resources != null){
            for(Resource r: resources){
                System.out.printf("%s\t%s\n",r.getName(),r.getType().getCode());
            }
        }
    }


}