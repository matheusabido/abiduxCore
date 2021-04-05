package br.com.abidux.core.scheduler;

public abstract class Scheduler {
	
	private final int ticks;
	private int current = 0;
	public Scheduler(int ticks) {
		this.ticks = ticks;
	}
	
	public void tick() {
		current++;
		if (current == ticks) {
			execute();
			current = 0;
		}
	}
	
	public abstract void execute();
	
}