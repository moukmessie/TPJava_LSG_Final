package lsg.graphics.widgets.characters.renderers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.HashSet;

public abstract class CharacterRenderer extends ImageView {

    private enum TL_NAME {
        IDLE, WALK, ATTACK, HURT, DIE
    }

    private final int FPS = 12 ;

    private Image IDLE_SPRITES[], WALK_SPRITES[], ATTACK_SPRITES[], HURT_SPRITES[], DIE_SPRITES[] ;

    private HashMap<TL_NAME, HashSet<KeyFrame>> frames = new HashMap<>() ;

    private Timeline animationTl = new Timeline() ;
    private Timeline gotoTl = new Timeline() ;

    public CharacterRenderer() {
        setPreserveRatio(true);
        loadSprites();
        setImage(IDLE_SPRITES[0]);
        createFrames();
    }

    protected abstract Image[] loadAttackSprites() ;
    protected abstract Image[] loadHurtSprites() ;
    protected abstract Image[] loadDieSprites() ;
    protected abstract Image[] loadIdleSprites() ;
    protected abstract Image[] loadWalkSprites() ;

    public Timeline getTimeline(){
        return animationTl;
    }

    private void playFrames(TL_NAME timelineName, int cycleCount, boolean autoreverse, EventHandler<ActionEvent> finishedHandler){
        animationTl.stop();
        animationTl.setCycleCount(cycleCount);
        animationTl.setAutoReverse(autoreverse);
        animationTl.getKeyFrames().clear();
        animationTl.getKeyFrames().addAll(frames.get(timelineName)) ;
        animationTl.setOnFinished(finishedHandler);
        animationTl.playFromStart();
    }

    public void attack(EventHandler<ActionEvent> finishedHandler){
        playFrames(TL_NAME.ATTACK, 1, false, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                idle();
                if(finishedHandler!=null) finishedHandler.handle(event);
            }
        });
    }

    public void die(EventHandler<ActionEvent> finishedHandler) {
        gotoTl.stop();
        playFrames(TL_NAME.DIE, 1, false, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (finishedHandler != null) finishedHandler.handle(event);
            }
        });
    }

    public void hurt(EventHandler<ActionEvent> finishedHandler){
        gotoTl.stop();
        playFrames(TL_NAME.HURT, 1, false, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playFrames(TL_NAME.IDLE, 1, false, new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        idle();
                        if(finishedHandler!=null) finishedHandler.handle(event);
                    }
                });
            }
        });
    }

    private void idle(){
        gotoTl.stop();
        playFrames(TL_NAME.IDLE, Timeline.INDEFINITE, true, null);
    }

    private void walk(){
        playFrames(TL_NAME.WALK, Timeline.INDEFINITE, false, null);
    }

    public void goTo(double x, EventHandler<ActionEvent> finishedHandler){
        gotoTl.stop();
        gotoTl.getKeyFrames().clear();
        gotoTl.getKeyFrames().add(new KeyFrame(Duration.millis(5000),
                new KeyValue(this.xProperty(), x))) ;
        gotoTl.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                idle();
                if(finishedHandler!=null) finishedHandler.handle(event);
            }
        });
        gotoTl.playFromStart();
        walk();
    }

    private void loadSprites(){
        IDLE_SPRITES = loadIdleSprites() ;
        WALK_SPRITES = loadWalkSprites() ;
        ATTACK_SPRITES = loadAttackSprites() ;
        HURT_SPRITES = loadHurtSprites() ;
        DIE_SPRITES = loadDieSprites() ;
    }

    private void createFrames(){
        KeyFrame kf ;
        HashSet<KeyFrame> kfs ;

        // IDLE
        kfs = new HashSet<>() ;
        for(int i=0 ; i < IDLE_SPRITES.length ; i++){
            final int frame = i ;
            kf = new KeyFrame(Duration.millis(frame*1000/FPS), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CharacterRenderer.this.setImage(IDLE_SPRITES[frame]);
                }
            }) ;
            kfs.add(kf) ;
        }
        frames.put(TL_NAME.IDLE, kfs) ;

        // WALK
        kfs = new HashSet<>() ;
        for(int i=0 ; i<WALK_SPRITES.length ; i++){
            final int frame = i ;
            kf = new KeyFrame(Duration.millis(frame*1000/FPS), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CharacterRenderer.this.setImage(WALK_SPRITES[frame]);
                }
            }) ;
            kfs.add(kf) ;
        }
        frames.put(TL_NAME.WALK, kfs) ;

        // ATTACK
        kfs = new HashSet<>() ;
        for(int i=0 ; i < ATTACK_SPRITES.length ; i++){
            final int frame = i ;
            kf = new KeyFrame(Duration.millis(frame*1000/FPS), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CharacterRenderer.this.setImage(ATTACK_SPRITES[frame]);
                }
            }) ;
            kfs.add(kf) ;
        }
        frames.put(TL_NAME.ATTACK, kfs) ;

        // HURT
        kfs = new HashSet<>() ;
        for(int i=0 ; i < HURT_SPRITES.length ; i++){
            final int frame = i ;
            kf = new KeyFrame(Duration.millis(frame*1000/FPS), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CharacterRenderer.this.setImage(HURT_SPRITES[frame]);
                }
            }) ;
            kfs.add(kf) ;
        }
        frames.put(TL_NAME.HURT, kfs) ;

        // DIE
        kfs = new HashSet<>() ;
        for(int i=0 ; i < DIE_SPRITES.length ; i++){
            final int frame = i ;
            kf = new KeyFrame(Duration.millis(frame*1000/FPS), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    CharacterRenderer.this.setImage(DIE_SPRITES[frame]);
                }
            }) ;
            kfs.add(kf) ;
        }
        frames.put(TL_NAME.DIE, kfs) ;
    }

}
