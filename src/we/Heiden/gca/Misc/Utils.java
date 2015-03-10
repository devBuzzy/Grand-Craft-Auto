package we.Heiden.gca.Misc;

import org.bukkit.util.Vector;

public class Utils {

	public static Vector rotateYAxis(Vector vec, double angleD) {
		Vector dir = vec.clone();
		double angleR = Math.toRadians(angleD);
		double x = dir.getX();
		double z = dir.getZ();
		double cos = Math.cos(angleR);
		double sin = Math.sin(angleR);
		dir.setX(x*cos+z*(-sin));
		dir.setZ(x*sin+z*cos);
		return dir;
	}
	
}
