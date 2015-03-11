/*
 * ******************************************** <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************
 */

/*
 * ******************************************** <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************
 */

/*
 * ******************************************** <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************
 */

package we.Heiden.gca.Weapons;


import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

/**
 * we.Heiden.gca.Weapons *
 */


public class Shoot {

    public static void shootSingle(int amount, float accuracy, Player p)
    {
        Vector velocity = null;
        Snowball snowball = null;
        for (int i = 0;i < amount; i++) { //Set i to 0 and as long as it is less than 5 add one to it then run the loop
            snowball = p.launchProjectile(Snowball.class); //set the snowball variable
            velocity = p.getLocation().getDirection(); //set the velocity variable
            velocity.add(new Vector(Math.random() * accuracy - accuracy, Math.random() * accuracy - accuracy, Math.random() * accuracy - accuracy)); //set it's modified velocity
            snowball.setVelocity(velocity); //set the snowball's new velocity
        }
    }
}
