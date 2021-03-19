package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Игра"); // заголовок формы

        // наша панель
        Pane root = new Pane();

        // загружаем картинку
        Image image = new Image(getClass().getResourceAsStream(""));
        ImageView imageView = new ImageView(image); // для отображения
        imageView.setFitHeight(300); // высота и длинна
        imageView.setFitWidth(400);
        root.getChildren().add(imageView); // добавить на панель

        // 1 группа меню
        MenuItem newGame = new MenuItem("Новая игра");
        MenuItem optionGame = new MenuItem("Настройки");
        MenuItem exitGame = new MenuItem("Выход");
        SubMenu mainMenu = new SubMenu(newGame, optionGame, exitGame); //хранилище меню

        // 2 группа меню
        MenuItem soundOptionGame = new MenuItem("Звук");
        MenuItem videoOptionGame = new MenuItem("Видео");
        MenuItem keysOptionGame = new MenuItem("Управление");
        MenuItem exitOptionGame = new MenuItem("Назад");
        SubMenu optionMenu = new SubMenu(soundOptionGame, videoOptionGame, keysOptionGame, exitOptionGame);

        // 3 группа меню
        MenuItem ng1 = new MenuItem("Турнир");
        MenuItem ng2 = new MenuItem("Своя игра");
        MenuItem ng3 = new MenuItem("2 игрока");
        MenuItem ng4 = new MenuItem("Назад");
        SubMenu newGameMenu = new SubMenu(ng1, ng2, ng3, ng4);

        // в главное меню добавляем основное хранилищще меню
        MenuBox menuBox = new MenuBox(mainMenu);

        // действия на каждое нажатие для необходимых кнопок
        newGame.setOnMouseClicked(event -> menuBox.setSubMenu(newGameMenu)); // меняем его на другое хранилище меню
        optionGame.setOnMouseClicked(event -> menuBox.setSubMenu(optionMenu));
        exitGame.setOnMouseClicked(event -> System.exit(0)); // выход
        exitOptionGame.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));
        ng4.setOnMouseClicked(event -> menuBox.setSubMenu(mainMenu));

        root.getChildren().addAll(menuBox); // добавляем главное меню, на панель

        // добавление на сцены | на форму
        Scene scene = new Scene(root, 400, 300);

        // действие на нажатие кнопки ESCAPE
        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){ // нажали ESCAPE?

                // анимация прозрачности
                FadeTransition ft = new FadeTransition(Duration.seconds(1), menuBox);
                if(!menuBox.isVisible()){ // видим меню?
                    ft.setFromValue(0); // начало анимации
                    ft.setToValue(1); // конец меню
                    ft.play(); // проиграть
                    menuBox.setVisible(true); // теперь меню видно
                    primaryStage.setTitle("Меню игра"); // заголовок формы
                } else { // всё наоборот
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.setOnFinished(event1 -> menuBox.setVisible(false));
                    ft.play();
                    primaryStage.setTitle("Игра"); // заголовок формы
                }
            }
        });
        primaryStage.setScene(scene);  // размер формы и сцена
        primaryStage.show(); // отобразить
    }

    public static void main(String[] args) {
        launch(args);
    }

    //------------------------------------------ отдлеьный класс MenuItem
    class MenuItem extends StackPane {

        public MenuItem(String name) {
            // основа для кнопки
            Rectangle rectangle = new Rectangle(200, 20, Color.GRAY);
            rectangle.setOpacity(0.5); // прозрачность

            // добавление текста
            Text text = new Text(name);
            text.setFill(Color.WHITE); // заливка
            text.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // основные параметры
            setAlignment(Pos.CENTER); // позиция текста по центру
            getChildren().addAll(rectangle, text); // добавить на форму

            // анимация заливки
            FillTransition fillTransition = new FillTransition(Duration.seconds(0.5), rectangle);

            // событие при наведении
            setOnMouseEntered(event -> {
                fillTransition.setToValue(Color.DARKGOLDENROD); // начальный цвет
                fillTransition.setFromValue(Color.DARKBLUE); // конечный цвет
                fillTransition.setCycleCount(Animation.INDEFINITE); // сколько повторить? бесконечно!
                fillTransition.setAutoReverse(true); // запускать абратную анимацию?
                fillTransition.play(); // запустить анимацию
            });

            // событие при отведении мышки
            setOnMouseExited(event -> {
                fillTransition.stop(); // остановить анимацию
                rectangle.setFill(Color.GRAY); // перекрашиваем прямоугольник
            });
        }
    }

    //----------------------------- класс для отображения и хранения меню
    private static class MenuBox extends Pane {
        static SubMenu subMenu;

        public MenuBox(SubMenu subMenu) {
            MenuBox.subMenu = subMenu; // записываем приходящее меню

            setVisible(false); // убираем отображение хранилища меню
            // цвет хранилища меню и его размеры
            Rectangle rectangle = new Rectangle(400, 300, Color.LIGHTBLUE);
            rectangle.setOpacity(0.4); // прозрачность
            getChildren().addAll(rectangle, subMenu); // добавляем наше меню в хранилище
        }

        public void setSubMenu(SubMenu subMenu) {
            getChildren().remove(MenuBox.subMenu); // удаляем текущее меню в хранилище
            MenuBox.subMenu = subMenu; // записываем приходящее меню
            getChildren().add(subMenu); // добавляем её в хранилище
        }
    }

    // ----------------------------------- класс который хранит наше меню
    private static class SubMenu extends VBox {
        public SubMenu(MenuItem... items) {
            setSpacing(15); // отступы между пунктами меню
            for (MenuItem item : items) { // проходим по всем эл.
                setTranslateY(100); // позиция нашего меню
                setTranslateX(50);
                getChildren().addAll(item); // добовляем пункт меню
            }
        }
    }
}