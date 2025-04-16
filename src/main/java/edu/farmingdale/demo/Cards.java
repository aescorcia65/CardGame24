package edu.farmingdale.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.io.File;
import java.util.Random;

/**
 * Controller class for handling the logic behind the card game.
 * The user will try to create a mathematical expression that evaluates to 24 using four random cards.
 */
public class Cards {
    @FXML
    private Label answerLabel;
    @FXML
    private TextField textField;
    @FXML
    private ImageView firstView, secondView, thirdView, fourthView;

    Image[][] cardImageViews = new Image[13][4];
    public Random random = new Random();

    int index1, index2, index3, index4;
    int suit1, suit2, suit3, suit4;

    int[] cardValues = new int[4];
    String[] ranks = {"1","2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12", "13"};
    String[] suits = {"clubs","diamonds", "hearts","spades"};

    /**
     * Initializes the view by loading the card images and setting random card images for the four slots.
     */
    @FXML
    void initialize() {
        String folderPath = "src/main/resources/edu/farmingdale/demo/img";
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, img) -> img.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (int rankIndex = 0; rankIndex < ranks.length; rankIndex++) {
                for (int suitIndex = 0; suitIndex < suits.length; suitIndex++) {
                    String fileName = ranks[rankIndex] + "_of_" + suits[suitIndex] + ".png";
                    File cardFile = new File(folderPath + "/" + fileName);
                    if (cardFile.exists()) {
                        Image image = new Image(cardFile.toURI().toString());
                        cardImageViews[rankIndex][suitIndex] = image;
                    }
                }
            }
        }

        // Generate and show initial cards
        refreshImage();
    }

    /**
     * Verifies the user's input expression and checks if it evaluates to 24.
     */
    @FXML
    protected void VerifyButton() {
        String expression = textField.getText();
        if (isValidExpression(expression)) {
            try {
                double result = evaluateExpression(expression);
                if (result == 24) {
                    answerLabel.setText("Correct! The expression evaluates to 24.");
                } else {
                    System.out.println(result);
                    answerLabel.setText("Not 24. Hint: Follow PEMDAS rules.");
                }
            } catch (NullPointerException e) {
                answerLabel.setText("Please enter an input.");
            } catch (Exception e) {
                answerLabel.setText("Error evaluating expression.");
                System.out.println(e);
            }
        } else {
            answerLabel.setText("Invalid input. Only use the numbers from the cards.");
        }
    }

    /**
     * Refreshes the card images and clears the input field.
     */
    @FXML
    protected void refreshImage() {
        index1 = random.nextInt(13);
        index2 = random.nextInt(13);
        index3 = random.nextInt(13);
        index4 = random.nextInt(13);

        suit1 = random.nextInt(4);
        suit2 = random.nextInt(4);
        suit3 = random.nextInt(4);
        suit4 = random.nextInt(4);

        cardValues[0] = index1 + 1;
        cardValues[1] = index2 + 1;
        cardValues[2] = index3 + 1;
        cardValues[3] = index4 + 1;

        firstView.setImage(cardImageViews[index1][suit1]);
        secondView.setImage(cardImageViews[index2][suit2]);
        thirdView.setImage(cardImageViews[index3][suit3]);
        fourthView.setImage(cardImageViews[index4][suit4]);

        textField.clear();
        answerLabel.setText("");
    }

    /**
     * Validates that the user's expression only uses the card values and valid operators.
     */
    private boolean isValidExpression(String expression) {
        String card1 = Integer.toString(cardValues[0]);
        String card2 = Integer.toString(cardValues[1]);
        String card3 = Integer.toString(cardValues[2]);
        String card4 = Integer.toString(cardValues[3]);

        String allowed = String.format("^(%s|%s|%s|%s|\\+|\\-|\\*|\\/|\\(|\\)|\\s)*$",
                card1, card2, card3, card4);

        return expression.matches(allowed);
    }

    /**
     * Uses JavaScript engine to evaluate the arithmetic expression.
     */
    public double evaluateExpression(String expression) {
        Expression exp = new ExpressionBuilder(expression).build();
        return exp.evaluate();
    }
}