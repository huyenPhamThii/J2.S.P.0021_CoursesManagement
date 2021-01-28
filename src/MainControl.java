
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nmtun
 */
public class MainControl {

    MainDisplay display;
    AddCourse addCourse;
    SearchCourse searchCourse;
    ListCourse listCourse;

    ArrayList<Courses> list = new ArrayList<>();

    public MainControl(MainDisplay display, AddCourse addCourse, SearchCourse searchCourse, ListCourse listCourse) {
        this.display = display;
        this.addCourse = addCourse;
        this.searchCourse = searchCourse;
        this.listCourse = listCourse;
    }

    public void control() {
        display.setVisible(true);
        //not resizable form
        display.setResizable(false);
        addCourse.setResizable(false);
        searchCourse.setResizable(false);
        listCourse.setResizable(false);
        //not editable in result lable after search
        searchCourse.getTxtName().setEditable(false);
        searchCourse.getTxtCredit().setEditable(false);

        setEventAllButton();
        setEventControlExit();
        setBtnInAddCourse();
        setEventSearch();
        setEventDisplayCourses();
    }
    //for button
    public void setEventAllButton() {
        //Hide frame Menu and display frame fuctions
        display.getBtnAdd().addActionListener((evt) -> {
            addCourse.setVisible(true);
            display.setVisible(false);
        });
        display.getBtnSeach().addActionListener((evt) -> {
            searchCourse.setVisible(true);
            display.setVisible(false);
        });
        display.getBtnDisplay().addActionListener((evt) -> {
            listCourse.setVisible(true);
            display.setVisible(false);
        });
    }
    //for frame
    public void setEventControlExit() {
        //display menuFrame when close fuctionFrame
        listCourse.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                display.setVisible(true);
            }
            
        });
        searchCourse.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                display.setVisible(true);
            }
        });
        addCourse.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                display.setVisible(true);
            }
        });
        display.getBtnExit().addActionListener((evt) -> {
            System.exit(0);
        });
    }

    public void setBtnInAddCourse() {
        addCourse.getBtnAdd().addActionListener((evt) -> {

            String code = addCourse.getTxtCode().getText();
            String name = addCourse.getTxtName().getText();
            String credit = addCourse.getTxtCredit().getText();

            if (canAddCourse(code, name, credit)) {
                list.add(new Courses(code, processString(name), Integer.parseInt(credit)));
                clearText();
            } else {
                JOptionPane.showMessageDialog(addCourse, "Wrong input !!!", "Error", 2);
            }
            list.forEach((courses) -> {
                System.out.println(courses.toString());
            });
        });

        addCourse.getBtnClear().addActionListener((evt) -> {
            clearText();
        });
    }

    public void clearText() {
        addCourse.getTxtCode().setText("");
        addCourse.getTxtName().setText("");
        addCourse.getTxtCredit().setText("");
    }

    //check empty from code, name and credit
    public boolean canAddCourse(String code, String name, String credit) {
        boolean resoult = true;

        if (code.isEmpty() || name.isEmpty() || credit.isEmpty()) {
            resoult = false;
        } else {
            try {
                Integer.parseInt(credit);
            } catch (NumberFormatException e) {
                resoult = false;
            }
        }

        return resoult;
    }

    //format name
    public String processString(String temp) {
        temp = temp.trim();
        String[] tmp = temp.split("\\s+");
        String finalString = "";

        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].equals("and")) {
                continue;
            }

            int index = 0;
            while ((String.valueOf(tmp[i].charAt(index)).matches("\\W"))) {
                if (index == tmp[i].length() - 1) {
                    break;
                }
                index++;
            }

            String first = tmp[i].substring(0, index + 1).toUpperCase();
            System.out.println(first);
            String second = tmp[i].substring(index + 1).toLowerCase();

            tmp[i] = (i != 0 ? " " : "") + first + second;
            finalString += tmp[i];
        }

        return finalString;
    }

    public void setEventSearch() {
        searchCourse.getBtnSearch().addActionListener((evt) -> {
            String code = searchCourse.getTxtCode().getText();

            for (Courses courses : list) {
                if (courses.getCode().equals(code)) {
                    searchCourse.getTxtName().setText(courses.getName());
                    searchCourse.getTxtCredit().setText(courses.getCredit() + "");
                }
            }
        });
    }

    public void setEventDisplayCourses() {
        display.getBtnDisplay().addActionListener((evt) -> {
            list.sort((Courses o1, Courses o2) -> o1.getCredit() - o2.getCredit());
            list.forEach((temp) -> {
                System.out.println(temp);
            });
        });
    }

    public static void main(String[] args) {
        new MainControl(new MainDisplay(), new AddCourse(), new SearchCourse(), new ListCourse()).control();
    }
}
