package lsg.graphics;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ImageFactory {

    public static enum SPRITES_ID {

        HERO_HEAD                   ("images/craftpix/_PNG/3_KNIGHT/3_head_.png"),
        HERO_ATTACK_ANIMATION       ("images/craftpix/_PNG/3_KNIGHT/_ATTACK/"),
        HERO_DIE_ANIMATION          ("images/craftpix/_PNG/3_KNIGHT/_DIE/"),
        HERO_HURT_ANIMATION         ("images/craftpix/_PNG/3_KNIGHT/_HURT/"),
        HERO_IDLE_ANIMATION         ("images/craftpix/_PNG/3_KNIGHT/_IDLE/"),
        HERO_WALK_ANIMATION         ("images/craftpix/_PNG/3_KNIGHT/_WALK/"),

        ZOMBIE_HEAD                 ("images/craftpix/_PNG/Zombie1/bodyparts/head.png"),
        ZOMBIE_ATTACK_ANIMATION     ("images/craftpix/_PNG/Zombie1/_ATTACK/"),
        ZOMBIE_DIE_ANIMATION        ("images/craftpix/_PNG/Zombie1/_DIE/"),
        ZOMBIE_HURT_ANIMATION       ("images/craftpix/_PNG/Zombie1/_HURT/"),
        ZOMBIE_IDLE_ANIMATION       ("images/craftpix/_PNG/Zombie1/_IDLE/"),
        ZOMBIE_WALK_ANIMATION       ("images/craftpix/_PNG/Zombie1/_WALK/"),

        ATTACK_SKILL                ("images/Hero Skill Free/Power of blessing.png"),
        RECUPERATE_SKILL            ("images/Hero Skill Free/Drain mana.png"),

        SUPER_BERRY                 ("images/Basic RPG Item Free/Berry_03.png"),
        SMALL_STAM_POTION           ("images/Basic RPG Item Free/Small Potion_01.png"),

        ;

        private final String path ;
        SPRITES_ID(String path) { this.path = path ; }
    }

    private static HashMap<SPRITES_ID, Image[]> sprites = new HashMap<>() ;

    public static void preloadAll(Runnable finishedHandler){
        new Thread(()-> {
            try {
                for (SPRITES_ID id : SPRITES_ID.values()) {
                    load(id);
                }
                if(finishedHandler != null) finishedHandler.run();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Image[] getSprites(SPRITES_ID id){
        Image[] images = sprites.get(id) ;
        if(images == null) {
            try {
                images = load(id) ;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images ;
    }

    private static Image[] load(SPRITES_ID id) throws URISyntaxException, IOException {
        String pathName = id.path ;
        Path path = Paths.get(ImageFactory.class.getResource(pathName).toURI()) ;
        Image[] images ;
        if(Files.isDirectory(path)){
            List<Path> paths = Files.list(path).filter(f -> !(Files.isDirectory(f))).sorted().collect(Collectors.toList()) ;
            images = new Image[paths.size()] ;
            int i=0 ;
            for (Path p: paths){
                String name = pathName + p.getFileName() ;
                images[i] = new Image(ImageFactory.class.getResource(name).toExternalForm());
                int finalI = i;
                i++ ;
            }
        }else{
            images = new Image[]{ new Image(ImageFactory.class.getResource(id.path).toExternalForm()) };
        }

        ImageFactory.sprites.put(id, images) ;
        return images ;
    }

    private static void loadV2(SPRITES_ID id, Consumer<Image[]> finishedHandler) throws URISyntaxException, IOException {

        ObservableList<Image> array = FXCollections.observableArrayList();;
        ListProperty list = new SimpleListProperty<>(array) ;

        String pathName = id.path ;
        System.out.println("Loading " + pathName);
        Path path = Paths.get(ImageFactory.class.getResource(pathName).toURI()) ;
        Image[] images ;
        if(Files.isDirectory(path)){
            List<Path> paths = Files.list(path).filter(f -> !(Files.isDirectory(f))).sorted().collect(Collectors.toList()) ;
            images = new Image[paths.size()] ;
            list.addListener(new ChangeListener<ObservableList<Image>>() {
                @Override
                public void changed(ObservableValue<? extends ObservableList<Image>> observable, ObservableList<Image> oldValue, ObservableList<Image> newValue) {
                    if(array.size() == images.length) {
                        ImageFactory.sprites.put(id, images) ;
                        finishedHandler.accept(images);
                    };
                }
            });
            int i=0 ;
            for (Path p: paths){
                String name = pathName + p.getFileName() ;
                images[i] = new Image(ImageFactory.class.getResource(name).toExternalForm(), true);
                int finalI = i;
                images[i].progressProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if(newValue.doubleValue() == 1) array.add(images[finalI]) ;
                    }
                });
                i++ ;
            }
        }else{
            images = new Image[]{ new Image(ImageFactory.class.getResource(id.path).toExternalForm(), true) } ;
            images[0].progressProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    ImageFactory.sprites.put(id, images) ;
                    finishedHandler.accept(images);
                }
            });
        }

    }

}
