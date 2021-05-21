package Engine.Tokens.Boss;

import Engine.MusicPlayer;
import Engine.Tokens.Enemy;

public abstract class Boss extends Enemy {

    String musicPath;

    abstract void BossFightMusic(MusicPlayer mp);

}
