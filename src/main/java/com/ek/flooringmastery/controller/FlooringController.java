package com.ek.flooringmastery.controller;

import com.ek.flooringmastery.service.FlooringPersistenceException;
import com.ek.flooringmastery.view.FlooringView;
import com.ek.flooringmastery.view.UserIO;


public class FlooringController {

    private FlooringView view;
    private UserIO io;

    public FlooringController(FlooringView view, UserIO io) {
        this.view = view;
        this.io = io;
    }

    // create run() method to display menu, let user choose, and perform action based on choice
    public void run() throws FlooringPersistenceException {
        boolean keepGoing = true;
        int menuSelection;

        while(keepGoing) {
            view.welcomeBanner();
            menuSelection = view.mainMenu();

            switch (menuSelection){
                case 1: displayAllOrders();
                    break;
                case 2: addOrder();
                    break;
                case 3: editOrder();
                    break;
                case 4: removeOrder();
                    break;
                case 5: //export all data
                    io.print("Not available at the moment");
                    break;
                case 6: keepGoing = false;
                    break;
            }
        }
        view.exitBanner();
    }

    // 1. Display all orders
    private void displayAllOrders(){
        view.displayAllBanner();
        view.displayOrders();
    }

    // 2. Add an order
    private void addOrder() throws FlooringPersistenceException {
        view.addOrderBanner();
        view.addOrder();
    }

    // 3. Edit an order
    private void editOrder() throws FlooringPersistenceException {
        view.editOrderBanner();
        view.updateOrder();
    }

    // 4. Remove an order
    private void removeOrder() throws FlooringPersistenceException {
        view.removeOrderBanner();
        view.removeOrder();
    }
}
