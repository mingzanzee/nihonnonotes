package nihonnonotes;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class NihonnonotesApp extends Application {
    private final List<Card> cards = new ArrayList<>();
    private final ListView<Card> list = new ListView<>();
    private final Label count = new Label();
    private final BorderPane root = new BorderPane();
    private VBox bankContent;
    private int studyIndex;
    private List<Card> deck = List.of();
    private boolean flipped;

    @Override public void start(Stage stage) {
        stage.setTitle("Nihonnonotes");
        root.setStyle("-fx-background-color: #f5f3ed; -fx-font-family: 'System';");
        Label brand = new Label("こ   NIHONNONOTES");
        brand.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill: #315f4c;");
        Label tagline = new Label("Your Japanese phrase library");
        tagline.setStyle("-fx-text-fill: #7c8980;");
        HBox header = new HBox(16, brand, tagline);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(22, 28, 18, 28));
        header.setStyle("-fx-border-color: transparent transparent #dedfd6 transparent;");

        Label title = new Label("Card bank");
        title.setStyle("-fx-font-size: 27px; -fx-font-weight: bold; -fx-text-fill: #243b35;");
        Button add = button("＋  New card", true); add.setOnAction(e -> edit(null));
        Button study = button("Study all", false); study.setOnAction(e -> begin(cards));
        HBox heading = new HBox(14, title, count, new Region(), add, study);
        HBox.setHgrow(heading.getChildren().get(2), Priority.ALWAYS);
        heading.setAlignment(Pos.CENTER_LEFT); heading.setPadding(new Insets(22, 0, 14, 0));

        list.setCellFactory(v -> new ListCell<>() {
            @Override protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) { setGraphic(null); setText(null); return; }
                Label jp = new Label(card.jp); jp.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #243b35;");
                Label en = new Label(card.en); en.setStyle("-fx-text-fill: #7c8980;");
                VBox words = new VBox(5, jp, en); words.setPadding(new Insets(8));
                Button view = button("Study", false); view.setOnAction(e -> begin(List.of(card)));
                Button edit = button("Edit", false); edit.setOnAction(e -> edit(card));
                Button delete = button("Delete", false); delete.setOnAction(e -> confirmDelete(card));
                HBox actions = new HBox(7, view, edit, delete); actions.setAlignment(Pos.CENTER_RIGHT);
                HBox row = new HBox(words, new Region(), actions); HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS); row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(8, 12, 8, 12)); row.setStyle("-fx-background-color: #fffefa; -fx-border-color: #dedfd6; -fx-border-radius: 8; -fx-background-radius: 8;");
                setGraphic(row); setPadding(new Insets(4, 0, 4, 0));
            }
        });
        bankContent = new VBox(heading, list);
        bankContent.setPadding(new Insets(8, 28, 20, 28));
        root.setTop(header); root.setCenter(bankContent);
        refresh();
        stage.setScene(new Scene(root, 900, 620)); stage.show();
    }

    private Button button(String text, boolean primary) {
        Button b = new Button(text);
        b.setStyle(primary ? "-fx-background-color: #315f4c; -fx-text-fill: white; -fx-background-radius: 7; -fx-padding: 9 14;" : "-fx-background-color: #fffefa; -fx-text-fill: #243b35; -fx-border-color: #dedfd6; -fx-background-radius: 7; -fx-border-radius: 7; -fx-padding: 8 12;");
        return b;
    }
    private void refresh() {
        count.setText(cards.size() + (cards.size() == 1 ? " card" : " cards"));
        list.setItems(FXCollections.observableArrayList(cards));
        if (cards.isEmpty()) list.setPlaceholder(new Label("Your first phrase starts here. Add a card to begin."));
    }
    private void edit(Card card) {
        if (card == null && cards.size() >= 2000) { alert("The 2,000 card limit has been reached."); return; }
        Dialog<Card> dialog = new Dialog<>(); dialog.setTitle(card == null ? "New card" : "Edit card");
        ButtonType save = new ButtonType("Save card", ButtonBar.ButtonData.OK_DONE); dialog.getDialogPane().getButtonTypes().addAll(save, ButtonType.CANCEL);
        TextArea jp = new TextArea(card == null ? "" : card.jp), en = new TextArea(card == null ? "" : card.en);
        jp.setPromptText("Japanese phrase"); en.setPromptText("English translation"); jp.setPrefRowCount(2); en.setPrefRowCount(2);
        VBox fields = new VBox(8, new Label("Japanese"), jp, new Label("English translation"), en); fields.setPadding(new Insets(14)); dialog.getDialogPane().setContent(fields);
        dialog.setResultConverter(type -> type == save && !jp.getText().isBlank() && !en.getText().isBlank() ? new Card(jp.getText().trim(), en.getText().trim()) : null);
        dialog.showAndWait().ifPresent(result -> { if (card == null) cards.add(result); else { card.jp = result.jp; card.en = result.en; } refresh(); });
    }
    private void confirmDelete(Card card) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Delete “" + card.jp + "”?", ButtonType.CANCEL, ButtonType.OK);
        a.setHeaderText("Remove card"); a.showAndWait().filter(b -> b == ButtonType.OK).ifPresent(b -> { cards.remove(card); refresh(); });
    }
    private void begin(List<Card> selection) {
        if (selection.isEmpty()) { alert("Add a card before starting a study session."); return; }
        deck = new ArrayList<>(selection); studyIndex = 0; flipped = false; showStudy();
    }
    private void showStudy() {
        Card card = deck.get(studyIndex);
        Label progress = new Label("Card " + (studyIndex + 1) + " of " + deck.size());
        Label side = new Label(flipped ? card.en : card.jp); side.setStyle("-fx-font-size: 30px; -fx-text-fill: #243b35; -fx-font-weight: bold;");
        Label hint = new Label(flipped ? "English · click to flip back" : "Japanese · click to reveal"); hint.setStyle("-fx-text-fill: #7c8980;");
        VBox face = new VBox(20, new Label(flipped ? "ENGLISH" : "JAPANESE"), side, hint); face.setAlignment(Pos.CENTER); face.setPadding(new Insets(65)); face.setStyle("-fx-background-color: #fffefa; -fx-background-radius: 16; -fx-border-color: #dedfd6; -fx-border-radius: 16;");
        face.setOnMouseClicked(e -> { flipped = !flipped; showStudy(); });
        Button prev = button("← Previous", false); prev.setDisable(studyIndex == 0); prev.setOnAction(e -> { studyIndex--; flipped = false; showStudy(); });
        Button next = button(studyIndex == deck.size() - 1 ? "Finish" : "Next →", true);
        next.setOnAction(e -> { if (studyIndex + 1 == deck.size()) returnToBank(); else { studyIndex++; flipped = false; showStudy(); } });
        Button close = button("Close", false); close.setOnAction(e -> returnToBank());
        HBox controls = new HBox(10, prev, next, close); controls.setAlignment(Pos.CENTER);
        VBox pane = new VBox(20, progress, face, controls); pane.setAlignment(Pos.CENTER); pane.setPadding(new Insets(35, 100, 35, 100));
        root.setCenter(pane);
    }
    private void returnToBank() {
        root.setCenter(bankContent);
        refresh();
    }
    private void alert(String message) { new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait(); }
    private static class Card {
        String jp, en;
        Card(String jp, String en) { this.jp = jp; this.en = en; }
        @Override public String toString() { return jp; }
    }
    public static void main(String[] args) { launch(args); }
}
