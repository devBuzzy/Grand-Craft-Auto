package we.Heiden.gca.Stores;

import we.Heiden.gca.NPCs.NPCs;

public enum Stores {

	Bank(new BankStore()),
	Car(new CarStore()),
	Clerk(new ClerkStore()),
	Home(new HomeStore()),
	Pet(new PetStore());
	
	private BasicStore store;
	
	private Stores(BasicStore store) { this.store = store; }
	
	public BasicStore getStore() { return store; }
	
	public static BasicStore match(NPCs npc) { return npc.getStore(); }
}
