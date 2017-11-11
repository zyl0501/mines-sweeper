package com.zyl.mine;

import android.content.Context;

public class Block {
	public static int BLOCK_SIZE;
	public static final int MINE_TYPE = -1;
	public static final int EMPTY_TYPE = 0;
	public static final int NUM_TYPE = 1;

	private boolean isPressed;
	private boolean isFlag;
	private int row;
	private int col;
	private int type;
	private int num;

	public Block() {
		this(0, 0, EMPTY_TYPE);
	}

	public Block(int row, int col, int type) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.num = 0;
		isPressed = false;
		isFlag = false;
	}

	public boolean isPressed() {
		return isPressed;
	}

	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
	}

	public boolean isFlag() {
		return isFlag;
	}

	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
		if (isFlag == true) {
			MinesMap.flagCount++;
		} else {
			MinesMap.flagCount--;
		}
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}


	public static void setBlockSize(int size){
		BLOCK_SIZE = size;
	}
}
