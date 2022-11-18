package com.ek.flooringmastery;

import com.ek.flooringmastery.controller.FlooringController;
import com.ek.flooringmastery.service.FlooringPersistenceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    public static void main(String[] args) throws FlooringPersistenceException {

        ApplicationContext ctx =
                //pass the name of the application context config file into this constructor
                new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringController controller =
                //use getBean method to get the beans you want; first parameter is the ID (from the xml file)
                //second parameter is the type of the bean (so the class name) as well as the type of the reference (the fact that it is a class)
                ctx.getBean("controller", FlooringController.class);
        controller.run();
    }
}
