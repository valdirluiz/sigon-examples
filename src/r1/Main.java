package r1;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import agent.AgentLexer;
import agent.AgentParser;
import br.ufsc.ine.agent.Agent;
import br.ufsc.ine.parser.AgentWalker;
import br.ufsc.ine.parser.VerboseListener;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.shape.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {

	static GridPane root;

	// Aumentei o tamanho
	static int SIZE = 12;
	static int length = SIZE;
	static int width = SIZE;

	public static int rowIndex = 0;
	public static int columnIndex = 0;
	private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	private Image pedestrian = new Image(getClass().getResourceAsStream("agent.png"));

	@Override
	public void start(Stage primaryStage) {
		root = new GridPane();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setHgap(2);
		root.setVgap(2);
		root.getStyleClass().addAll("game-root");

		addChildrens(rowIndex, columnIndex, root);
		;

		Scene scene = new Scene(root, 550, 550);
		scene.getStylesheets().add("r1/game.css");

		// Adicionei essas duas linhas
		root.getChildren().get(getButton(6, 6)).getStyleClass().add("game-button-agent");

		primaryStage.setTitle("R1");
		primaryStage.setScene(scene);
		primaryStage.show();

		Runnable runnableLeftTask = leftCars();
		executorService.schedule(runnableLeftTask, 150, TimeUnit.MILLISECONDS);
	}

	private Runnable leftCars() {
		return () -> {

			try {
				for (int i = 11; i > -1; i--) {
					if (i == 0) {
						root.getChildren().get(getButton(i + 1, 5)).getStyleClass().clear();
						root.getChildren().get(getButton(i + 1, 5)).getStyleClass().addAll("game-button-road");
						root.getChildren().get(getButton(i, 5)).getStyleClass().add("game-button-car");
						root.getChildren().get(getButton(i, 5)).getStyleClass().addAll("game-button-road");
					}else if (i < 11) {
						TimeUnit.MILLISECONDS.sleep(100);
						root.getChildren().get(getButton(i + 1, 5)).getStyleClass().clear();
						root.getChildren().get(getButton(i + 1, 5)).getStyleClass().addAll("game-button-road");
						root.getChildren().get(getButton(i, 5)).getStyleClass().add("game-button-car");

					}   else {
						root.getChildren().get(getButton(i, 5)).getStyleClass().add("game-button-car");
					}
				}

				executorService.schedule(this::leftCars, Long.valueOf(randInt(30000, 60000)), TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
	}

	private static void startAgent() {
		try {

			File agentFile = new File("r1.on");
			CharStream stream = CharStreams.fromFileName(agentFile.getAbsolutePath());
			AgentLexer lexer = new AgentLexer(stream);
			CommonTokenStream tokens = new CommonTokenStream(lexer);

			AgentParser parser = new AgentParser(tokens);
			parser.removeErrorListeners();
			parser.addErrorListener(new VerboseListener());

			ParseTree tree = parser.agent();
			ParseTreeWalker walker = new ParseTreeWalker();

			AgentWalker agentWalker = new AgentWalker();
			walker.walk(agentWalker, tree);

			Agent agent = new Agent();
			agent.run(agentWalker);

		} catch (IOException e) {
			System.out.println("I/O exception.");
		}
	}

	// Método inteiro mudado
	private static void addChildrens(int rowIndex, int columnIndex, GridPane root) {

		for (int y = 0; y < length; y++) {

			for (int x = 0; x < width; x++) {

				Button button = new Button();

				button.setPrefHeight(50);
				button.setPrefWidth(50);
				button.setAlignment(Pos.CENTER);

				// Set all as default
				button.setText("");
				button.getStyleClass().addAll("game-button");

				// Horizontal pedestrians
				if (x < 4 || x > 7) {
					if (y == 1 || y == 4 || y == 7 || y == 10)
						button.getStyleClass().add("game-button-pedestrian");
				}

				// Vertical pedestrians

				if (x == 4 || x == 7) {
					if (y != 2 && y != 3 && y != 8 && y != 9)
						button.getStyleClass().add("game-button-pedestrian");
				}

				// Vertical streets
				if (x == 5 || x == 6)
					button.getStyleClass().addAll("game-button-road");

				// Horizontal streets
				if (y == 2 || y == 3 || y == 8 || y == 9)
					button.getStyleClass().addAll("game-button-road");

				root.setRowIndex(button, y);
				root.setColumnIndex(button, x);
				root.getChildren().add(button);
			}
		}

	}

	public static void startEnvironment() {
		launch();
	}

	public static void main(String[] args) {
		startAgent();
		startEnvironment();
		// startAgent();

	}

	private int getButton(int x, int y) {
		return x * SIZE + y;
	}

	private static int randInt(int min, int max) {
		int randomNum = new Random().nextInt((max - min) + 1) + min;
		return randomNum;
	}

}