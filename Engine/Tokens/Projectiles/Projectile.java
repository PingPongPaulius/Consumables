package Engine.Tokens.Projectiles;

import Engine.Tokens.Token;

public abstract class Projectile extends Token {

    float speed;

    boolean isHarming;

    public boolean isHarming(){
        return isHarming;
    };

}
