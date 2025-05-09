import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author 30039419 Robert Sneddon
 * EcoFriendly Habit Tracking App v1
 */
public class ecoFriendlyGUI {
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JButton nextBtn;
    private JButton backBtn;
    private JPanel detailPanel;
    private JTextField nameTxt;
    private JPanel trackingPanel;
    private JPanel additionalPanel;
    private JComboBox bagBox;
    private JComboBox coffeeBox;
    private JComboBox recycleBox;
    private JComboBox walkBox;
    private JComboBox waterBox;
    private JButton summaryBtn;
    private JLabel bagImg;
    private JLabel coffeeImg;
    private JLabel recycleImg;
    private JLabel walkImg;
    private JLabel waterImg;
    private JButton printBtn;
    private JPanel summaryPanel;
    private JButton excellentBtn;
    private JButton goodBtn;
    private JButton averageBtn;
    private JButton badBtn;
    private JButton notEcoBtn;
    private JTextArea feedbackTxt;
    private JLabel beefImg;
    private JLabel carImg;
    private JTextField beefTxt;
    private JTextField carTxt;
    private JButton beefBtn;
    private JButton carBtn;
    private JLabel logoImg;
    private JTextArea beefFeedback;
    private JTextArea carFeedback;
    private JPanel welcomePanel;
    private JLabel lblLogo;
    private String userName;

    public static void main(String[] args) {
        JFrame myAPP = new JFrame("Eco-Friendly Habit Tracking App v1");
        //make frame and set for the class then the form
        myAPP.setContentPane(new ecoFriendlyGUI().mainPanel);
        myAPP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myAPP.pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - myAPP.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - myAPP.getHeight()) / 2);
        myAPP.setLocation(x, y);
        myAPP.setSize(1200, 650);
        myAPP.setVisible(true);
    }

    /**
     * The main program, buttons lead to other methods, can resize images.
     * Also contains calculations when you hit summary and prints out text when you hit print.
     */
    public ecoFriendlyGUI() {
        //the back, next and additional panel buttons call certain methods
        nextBtn.addActionListener(e -> nextScreen());
        backBtn.addActionListener(e -> previousScreen());
        beefBtn.addActionListener(e -> beefInformation());
        carBtn.addActionListener(e -> carInformation());

        //Images placed here using a method to resize each one individually
        logoImg.setIcon(resizeImageIcon("logo.jpg",220,250));
        bagImg.setIcon(resizeImageIcon("Bag.jpg", 75, 75));
        coffeeImg.setIcon(resizeImageIcon("CoffeeCup.jpg", 75, 75));
        recycleImg.setIcon(resizeImageIcon("Recycle.jpg", 75, 75));
        walkImg.setIcon(resizeImageIcon("Walk.jpg", 75, 75));
        waterImg.setIcon(resizeImageIcon("WaterBottle.jpg", 75, 75));
        beefImg.setIcon(resizeImageIcon("beef.jpg", 200, 150));
        carImg.setIcon(resizeImageIcon("car.jpg", 200, 150));

        //summary button on second panel calculates and gives user feedback also lighten up a button
        summaryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset all buttons to a default gray
                JButton[] buttons = {excellentBtn, goodBtn, averageBtn, badBtn, notEcoBtn};
                for (JButton button : buttons) {
                    button.setBackground(Color.GRAY);
                }
                //Gets number from box and times it by average price then by 365 to calculate year
                int bag = bagBox.getSelectedIndex();
                double bagTotal = bag * 0.10 * 365;

                int coffee = coffeeBox.getSelectedIndex();
                double coffeeTotal = coffee * 0.25 * 365;
                int recycle = recycleBox.getSelectedIndex();
                double recycleTotal = recycle * 0.20 * 365;
                int walk = walkBox.getSelectedIndex();
                double walkTotal = walk * 1.70 * 365;
                int water = waterBox.getSelectedIndex();
                double waterTotal = water * 1.00 * 365;

                double total = bagTotal+coffeeTotal+recycleTotal+walkTotal+waterTotal;

                // Format values to 2 decimal places since it's money
                String bagTotal2 = String.format("%.2f", bagTotal);
                String coffeeTotal2 = String.format("%.2f", coffeeTotal);
                String recycleTotal2 = String.format("%.2f", recycleTotal);
                String walkTotal2 = String.format("%.2f", walkTotal);
                String waterTotal2 = String.format("%.2f", waterTotal);
                String total2 = String.format("%.2f", total);

                int score = (bag+coffee+recycle+walk+water);
                int scoreCategory = scoreResults(score); // Call the method to get score category
                String hint = getHintForCategory(scoreCategory);

                // Highlight the correct button based on score with different colours based on what one
                if (scoreCategory >= 1 && scoreCategory <= 5) {
                    Color categoryColor = getColorForCategory(scoreCategory); // Go method to get color
                    buttons[scoreCategory - 1].setBackground(categoryColor);
                }

                feedbackTxt.setText("Formula is not 100% accurate since many factors come into play.\n\nFeedback for "+userName+"\n\n" +
                        "By using a reusable bag "+bag+" times a week you're saving £"+bagTotal2+" a year.\n" +
                        "By using a reusable coffee cup "+coffee+" times a week you're saving £"+coffeeTotal2+" a year.\n" +
                        "Recycling "+recycle+" times a week could net you roughly £"+recycleTotal2+" a year.\n" +
                        "Walking instead of driving "+walk+" times a week is saving you about £"+walkTotal2+" a year.\n" +
                        "By using a reusable water bottle "+water+" times a week you're saving about £"+waterTotal2+" a year.\n" +
                        "In total, you're saving roughly about £"+total2+"\n\nHints:\n"+userName+hint);
            }
        });
        //print button on second panel uses a method to print text file
        printBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeToFile(feedbackTxt);
            }
        });
    }

    /**
     * Method to display how much O2 is created to produce beef
     * Will make sure is not negative, blank or a string
     */
    public void beefInformation() {
        try {
            String beefInput = beefTxt.getText().trim(); // Get input text and trim it
            if (beefInput.isEmpty()) { // Check for blank input
                throw new IllegalArgumentException("Input cannot be blank.");
            }

            double kg = Double.parseDouble(beefInput);  // Parse input to a double

            if (kg < 0){
                throw new IllegalArgumentException("KG can't be negative!");
            }

            double beefCalculation = kg * 29.1;
            beefFeedback.setText("If you're eating "+beefInput+"kg a year then you're creating about "+beefCalculation+" CO2!\n" +
                    "Downsides:\n" +
                    "Beef production is one of the largest methane contributors!\n" +
                    "It is the second highest emission maker in agricultural production.\n" +
                    "Beef requires vast amounts of land for grazing and crops!\n " +
                    "This means more deforestation and water scarcity!\n" +
                    "Benefits:\n" +
                    "Eating less beef will benefit you by reducing heart disease!\n" +
                    "Saves you money due to it's costs!\n" +
                    "Less animals will be killed!");

        } catch (NumberFormatException e) {
            String errorMessage = e.getMessage();
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a numeric value!", "Input Error", JOptionPane.ERROR_MESSAGE);
            errorLog(errorMessage);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            errorLog(errorMessage);
        }
    }

    /**
     * Method to display how much O2 is created when driving around
     * Will make sure is not negative, blank or a string
     */
    public void carInformation() {
        try {
            String carInput = carTxt.getText().trim();

            if (carInput.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be blank.");
            }

            double miles = Double.parseDouble(carInput);

            if (miles < 0) { // Check for negative value
                throw new IllegalArgumentException("Miles can't be negative!");
            }

            double carCalculation = miles * 0.4;
            carFeedback.setText("If you drive "+miles+" miles you are creating about "+carCalculation+" CO2!\n" +
                    "Downsides:\n" +
                    "Cars when driven cause emissions to pollute the air.\n" +
                    "Reducing this improves air quality and climate change.\n" +
                    "They also require fuels that are an finite amount.\n" +
                    "Their noisy in urban areas and pollute the air.\n" +
                    "They cause wear and tear damage to the environment!\n" +
                    "Benefits:\n" +
                    "Walking or cycling will improve your mentally and physical fitness.\n" +
                    "Will be cheaper and can be faster in peak times.");

        } catch (NumberFormatException e) {
            String errorMessage = e.getMessage();
            JOptionPane.showMessageDialog(null, "Invalid input! Please enter a numeric value!", "Input Error", JOptionPane.ERROR_MESSAGE);
            errorLog(errorMessage);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            JOptionPane.showMessageDialog(null, e.getMessage(),"Input Error",JOptionPane.ERROR_MESSAGE);
            errorLog(errorMessage);
        }
    }
    /**
     * When button pressed will print out the feedback for the user into a text document
     */
    public static void writeToFile(JTextArea feedbackTxt) {
        File file = new File("Feedback.txt");
        if (feedbackTxt.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Feedback is empty. Nothing to write.", "Write Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(feedbackTxt.getText());
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Feedback was successfully written to 'Feedback.txt'.", "Write Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            String errorMessage = "Failed to write feedback to file: " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMessage, "Write Error", JOptionPane.ERROR_MESSAGE);
            errorLog(errorMessage);
        }
    }
    /**
     * Logs any errors to ErrorLog.txt.
     * @param errorMessage Error message written to log
     */
    public static void errorLog(String errorMessage) {
        File errorLog = new File("errorLog.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorLog, true))) {
            writer.write(errorMessage);
            writer.newLine();
        } catch (IOException e) {
            // If logging fails, print to console to avoid silent failure.
            System.err.println("Error logging failed: " + e.getMessage());
        }
    }
    /**
     * Used for score to see what button will lit up based on user progress
     * @param score the total of things they do a week
     * @return 1-5 for switch case
     */
    private int scoreResults(int score) {
        if (score >= 28 && score <= 35){
            return 1;
        }else if (score >= 21 && score <= 27){
            return 2;
        }else if (score >= 14 && score <= 20){
            return 3;
        }else if (score >= 7 && score <= 13){
            return 4;
        }else{
            return 5;
        }
    }
    /**
     * Used to correct sizing of the images
     * @param resourcePath where image is located
     * @param width width size of image
     * @param height height size of image
     * @return the resized image
     */
    private ImageIcon resizeImageIcon(String resourcePath, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(resourcePath));
        Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
    /**
     * When called will go to the next panel and check what screen it is on
     * Will enable or disable buttons to go back and forth depending on screen.
     */
    public void nextScreen() {
        // Check if the current panel is WelcomePanel
        String currentPanel = getCurrentPanelName();

        if (currentPanel.equals("WelcomePanel")) {
            //Will remove leading and trailing spaces
            String name = nameTxt.getText().trim();
            //validates that name field is not blank when user tries to go next
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(detailPanel, "Please enter your name to proceed!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
                //checks that name is at least 2 letters long
            } else if (name.length() < 2) {
                JOptionPane.showMessageDialog(detailPanel, "Name must be at least 2 characters long.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
                //check that name is only letters and spaces
            } else if (!name.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(detailPanel, "Enter a valid name! (Letters and spaces only)", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userName = name;
        }
        // Proceed to the next panel
        CardLayout cardLayout = (CardLayout) detailPanel.getLayout();
        cardLayout.next(detailPanel);
        // Update button states on panels
        currentPanel = getCurrentPanelName();
        if (currentPanel.equals("WelcomePanel")) {
            backBtn.setEnabled(false);  // Disable the Back button
            nextBtn.setEnabled(true);   // Enable the Next button
        } else if (currentPanel.equals("TrackingPanel")) {
            backBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else {
            backBtn.setEnabled(true);
            nextBtn.setEnabled(false);
        }
    }

    /**
     * When called will go back to the previous panel and will check the panel it's on
     * Depending on  panel will disable or enable next and back buttons.
     */
    public void previousScreen() {
        CardLayout cardLayout = (CardLayout) detailPanel.getLayout();
        cardLayout.previous(detailPanel);  // Navigate to previous panel

        // Check which panel is currently on and enable or disable the buttons
        String currentPanel = getCurrentPanelName();
        if (currentPanel.equals("WelcomePanel")) {
            backBtn.setEnabled(false);  // Disable the Back button
            nextBtn.setEnabled(true);   // Enable the Next button

        } else if (currentPanel.equals("TrackingPanel")) {
            backBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        } else {
            backBtn.setEnabled(true);
            nextBtn.setEnabled(false);
        }
    }
    /**
     * Used to check what the current panel is for the next and back buttons
     *
     * @return CurrentPanel or nothing
     */
    private String getCurrentPanelName() {
        CardLayout cardLayout = (CardLayout) detailPanel.getLayout();
        //Used to check what card you're on for the next and back buttons to be enabled or disabled
        if (detailPanel.getComponent(0).isVisible()) {
            return "WelcomePanel";  //WelcomePanel is at index 0
        } else if (detailPanel.getComponent(1).isVisible()) {
            return "TrackingPanel";  //TrackingPanel is at index 1
        } else if (detailPanel.getComponent(2).isVisible()) {
            return "AdditionalPanel";  //AdditionalPanel is at index 2
        }
        return "";
    }

    /**
     * So the buttons aren't all green this method adjusts them from green to dark red the lower they are on score
     * @param category 1 is best and 5 is worst
     * @return the colour of button to be used
     */
    private Color getColorForCategory(int category) {
        return switch (category) {
            case 1 -> Color.GREEN;   // Excellent
            case 2 -> Color.YELLOW;  // Good
            case 3 -> Color.ORANGE;  // Average
            case 4 -> Color.RED;     // Bad
            case 5 -> new Color(139, 0, 0); // Dark Red for Not Eco
            default -> Color.GRAY;   // Default
        };
    }

    /**
     * Give the user hints to help based on how their doing
     * @param category 1 being the best and 5 the worst
     * @return hint text
     */
    private String getHintForCategory(int category) {
        return switch (category) {
            case 1 -> ", you're doing brilliant, keep it up! You're helping the environment and living a healthy life by constantly doing these habits!";
            case 2 -> ", you're doing quite good, if you push to try do a bit more of one of these habits you will help the environment a bit more!";
            case 3 -> ", you're a bit between which is not bad. You could put a bit more effort in so you help the environment more!";
            case 4 -> ", you're not doing great. You should put more effort in to doing these habits. You already do some so you should try do more. It can help the environment and your health!";
            case 5 -> ", you don't seem to care for the environment or your health much. You really should try some eco-friendly habits, you could save a lot of money and have a healthier life!";
            default -> "";
        };
    }
}