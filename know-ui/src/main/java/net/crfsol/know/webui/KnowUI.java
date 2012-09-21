package net.crfsol.know.webui;

import org.eclipse.jetty.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: cfagiani
 * Date: 9/20/12
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class KnowUI {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/know-ui/ui-context.xml");
        Server jettyServer = (Server) context.getBean("server");
        inputLoop();

    }

    /**
     * displays the command prompt and waits for user input
     */
    public static void inputLoop() {

        Scanner scanner = new Scanner(System.in);
        String line = null;
        do {
            line = scanner.nextLine();
        } while (!"quit".equalsIgnoreCase(line.trim()));

    }
}
