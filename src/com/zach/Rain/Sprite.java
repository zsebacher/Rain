package com.zach.Rain;

public class Sprite {
	public final int SIZE; // size of the sprite
	private int x, y;
	public int[] pixels;
	private SpriteSheet sheet;
	
	public static Sprite temp = new Sprite(16, 0, 0, SpriteSheet.tiles);
	
	public Sprite(int size, int x, int y, SpriteSheet sheet) {
		SIZE = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * size; // setting the location fo the target sprite in the spritesheet
		this.y = y * size;
		this.sheet = sheet;
		load();
	}
	
	private void load() {
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				pixels[x+y*SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
			}
		}
	}
}
