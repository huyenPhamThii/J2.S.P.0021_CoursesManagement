
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

        display.setResizable(false);
        addCourse.setResizable(false);
        searchCourse.setResizable(false);
        listCourse.setResizable(false);

        searchCourse.getTxtName().setEditable(false);
        searchCourse.getTxtCredit().setEditable(false);

        setEventAllButton();
        setEventControlExit();
        setBtnInAddCourse();
        setEventSearch();
        setEventDisplayCourses();
    }

    public void setEventAllButton() {
        display.getBtnAdd().addActionListener((evt) -> {
            display.setVisible(false);
            addCourse.setVisible(true);
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

    public void setEventControlExit() {
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

    public boolean canAddCourse(String code, String name, String credit) {
        boolean resoult = true;

        if (code.isEmpty() || name.isEmpty() || credit.isEmpty()) {
            JOptionPane.showMessageDialog(addCourse, "Code/name/credit can not empty", "Can not add", 2);
            resoult = false;
        } else if (checkCode(code) >= 0) {
            JOptionPane.showMessageDialog(addCourse, "This code is already exited!!!", "Can not add", 2);
            resoult = false;
        } else {
            try {
                if(Integer.parseInt(credit) < 0 || Integer.parseInt(credit) > 33){
                    JOptionPane.showMessageDialog(addCourse, "Credit must be >= 0 and <= 33", "Can not add", 2);
                    resoult = false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(addCourse, "Credit must be a number", "Can not add", 2);
                resoult = false;
            }
        }

        return resoult;
    }

    public String processString(String temp) {
        temp = temp.trim();
        String[] tmp = temp.split("\\s+");
        String finalString = "";

        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].equals("and")) {
                finalString += " and";
            } else {
                int index = 0;
                while ((String.valueOf(tmp[i].charAt(index)).matches("\\W"))) {
                    if (index == tmp[i].length() - 1) {
                        break;
                    }
                    index++;
                }

                String first = tmp[i].substring(0, index + 1).toUpperCase();
                String second = tmp[i].substring(index + 1).toLowerCase();

                tmp[i] = (i != 0 ? " " : "") + first + second;
                finalString += tmp[i];
            }
        }
        return finalString;
    }

    public void setEventSearch() {
        searchCourse.getBtnSearch().addActionListener((evt) -> {
            String code = searchCourse.getTxtCode().getText();
            int index = checkCode(code);
            if (checkEmpty(code)) {
                JOptionPane.showMessageDialog(searchCourse, "Code can not be empty!!!", "Empty", 2);
            } else if (index < 0) {
                JOptionPane.showMessageDialog(searchCourse, "This code does not exist in the list!!!", "Not found", 2);
            } else {
                searchCourse.getTxtName().setText(list.get(index).getName());
                searchCourse.getTxtCredit().setText(list.get(index).getCredit() + "");
            }
        });
    }

    public void setEventDisplayCourses() {
        display.getBtnDisplay().addActionListener((evt) -> {
            list.sort((Courses o1, Courses o2) -> o1.getCredit() - o2.getCredit());
            list.forEach((temp) -> {
                listCourse.getTxtDisplay().append(temp.toString() + "\n");
            });
        });
    }

    //check code is exist in the list
    public int checkCode(String code) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCode().equals(code)) {
                return i;
            }
        }
        return -1;
    }

    //check code is empty
    public boolean checkEmpty(String s) {
        if (s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        new MainControl(new MainDisplay(), new AddCourse(), new SearchCourse(), new ListCourse()).control();
    }
}
