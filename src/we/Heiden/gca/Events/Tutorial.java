package we.Heiden.gca.Events;

import org.bukkit.entity.Player;

import we.Heiden.gca.Messages.JsonMessage;
import we.Heiden.gca.Messages.JsonMessage.ClickAction;
import we.Heiden.gca.Messages.JsonMessage.HoverAction;

/**
 * ********************************************* <p>
 * <b>This has been made by <i>Heiden Team</b>
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 *         <b>All rights reserved <p>
 *           Heiden Team 2015 <p></b>
 * ********************************************* 
 **/
public class Tutorial {
	
	/**
	 * This is just a tutorial.
	 * It`s ordered easiest to hardest
	 * I`ll recommend you to see them all
	 */

	public static void Tuto01(Player p) {
		/**
		 * sendJson:
		 *  type: void
		 *  use:
		 *   param 1: target (Player)
		 *   param 2: String... (Built Json) <-- You can use as many as you like
		 */
		/*This will send the json to the player*/
		JsonMessage.sendJson(p,
				/**
				 * new Json(String):
				 *   type: Json Code (String)
				 *   use: Send a string and it will return it as a json String.
				 */
				/*The simplest text, does nothing.*/
				JsonMessage.newJson("This is an example"));
	}
	
	public static void Tuto02(Player p) {
		/*As done before, this will send the Json*/
		JsonMessage.sendJson(p, 
				/**
				 * new Json:
				 *   type: Json
				 *   use: It allows you to build your json.
				 *  
				 *  differences beetween "new Json(String)":
				 *   * The one used on the previous just
				 *     generates an simple json message that
				 *     does nothing else than sending a message
				 *   * This one opens an Json builder, who
				 *     allows you to use multiple texts (same line)
				 *     with hoverEvent and clickEvent features
				 */
				/*Let`s Start our builder*/
				JsonMessage.newJson()
					/**
					 * add:
					 *  type: Json
					 *  use: Allows you to use different texts on the same line.
					 *  example: see under
					 */
					/*Here we`re adding the texts*/
					.add("&a&lVisit Google&d! &bClick")
						/*Please remember this step*/
						.build()
					.add("&1&o&nHere")
						/**
						 * clickEvent:
						 *  type: Json
						 *  use: this will be done everytime this text is clicked
						 *   param 1: action (ClickAction)
					 	*    This is a enum. Just do ClickAction.ACTION
					 	*   param 2: value (String)
					 	*/
						/*This will take the player to google*/
						.clickEvent(ClickAction.Open_Url, "http://www.google.com/")
						
						/**
						 * hoverEvent:
						 *  type: Json
						 *  use: this will be shown everytime the player put the mause over this text
						 *   param 1: action (HoverAction)
						 *    This is a enum. Just do HoverAction.ACTION
						 *   param 2: value (String)
						 */
						/*This will show the url*/
						.hoverEvent(HoverAction.Show_Text, "&b&o&nhttp://www.google.com/")
						
						/*Never forgot to build your Json*/
						.build()
					
					/*Now that our Json line is ready, let`s build it*/
					.build()
				);
	}
}
