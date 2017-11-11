package com.zyl.mine;

import java.util.LinkedList;
import java.util.Queue;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class MinesMap extends GestureDetector.SimpleOnGestureListener {
	// private static final int BLOCK_SIZE = 47;

	private int rowSize;
	private int colSize;
	private int mineCount;
	private Block[][] blocks;
	private MinePosition[] minePositions;
	private MineView mineView;
	private int clickCount = 0;
	public static int flagCount;

	// private List<Block> updateBlocks;

	// public MinesMap(MineView mView) {
	// this(mView, 15, 15, 10);
	// }

	public MinesMap(MineView mView, int rowSize, int colSize, int mineCount) {
		this.mineView = mView;
		this.rowSize = rowSize;
		this.colSize = colSize;
		this.mineCount = mineCount;
		// updateBlocks = new LinkedList<Block>();
		ini();
	}

	private void ini() {
		flagCount = 0;
		setBlocks(new Block[rowSize][colSize]);
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				getBlocks()[i][j] = new Block(i, j, Block.EMPTY_TYPE);
			}
		}
		minePositions = new MinePosition[mineCount];
		// ���á��ס��İ�ť
		for (int i = 0; i < mineCount; i++) {
			int row = (int) (Math.random() * rowSize);
			int col = (int) (Math.random() * colSize);
			if (getBlocks()[row][col].getType() != Block.MINE_TYPE) {
				getBlocks()[row][col].setType(Block.MINE_TYPE);
				getBlocks()[row][col].setNum(-1);
				minePositions[i] = new MinePosition(row, col);
				// �����ܱ�8�������
				setNums(row, col);
			} else {
				i--;
			}
		}
	}

	// ����"��"��ť�ܱ�8�������
	private void setNums(int row, int col) {
		Block[] surroundBtns = getSurroundBlocks(row, col);
		for (int i = 0; i < surroundBtns.length; i++) {
			setNum(surroundBtns[i].getRow(), surroundBtns[i].getCol());
		}
		getBlocks()[row][col].setNum(-1);
	}

	private void setNum(int row, int col) {
		Block block = getBlocks()[row][col];
		if (block.getType() != Block.MINE_TYPE) {
			getBlocks()[row][col].setNum(block.getNum() + 1);
			getBlocks()[row][col].setType(Block.NUM_TYPE);
		}
	}

	// ��ȡ�ܱ߰�ť
	private Block[] getSurroundBlocks(int row, int col) {
		int num = 8;
		// �߽�
		boolean /* left = false, right = false, */up = false, down = false;
		if (row == 0) {
			num -= 3;
			up = true;
		}
		if (row == rowSize - 1) {
			num -= 3;
			down = true;
		}
		if (col == 0) {
			if (up || down) {
				num -= 2;
			} else {
				num -= 3;
			}
			// left = true;
		}
		if (col == colSize - 1) {
			if (up || down) {
				num -= 2;
			} else {
				num -= 3;
			}
			// right = true;
		}
		Block[] temps = new Block[8];
		System.out.println("row: " + row + "col: " + col);
		temps[0] = getSurBlock(row - 1, col - 1);
		temps[1] = getSurBlock(row - 1, col);
		temps[2] = getSurBlock(row - 1, col + 1);
		temps[3] = getSurBlock(row, col - 1);
		temps[4] = getSurBlock(row, col + 1);
		temps[5] = getSurBlock(row + 1, col - 1);
		temps[6] = getSurBlock(row + 1, col);
		temps[7] = getSurBlock(row + 1, col + 1);
		Block[] surroundBlocks = new Block[num];
		int j = 0;
		for (int i = 0; i < 8; i++) {
			if (temps[i] == null) {
				continue;
			}
			surroundBlocks[j++] = temps[i];
		}
		return surroundBlocks;
	}

	private Block getSurBlock(int row, int col) {
		if (row == -1 || row == rowSize || col == -1 || col == colSize) {
			return null;
		}
		return getBlocks()[row][col];
	}

	// ����հ׿�
	private Queue<Block> queue = new LinkedList<Block>();

	private void pressEmptyBtn(int row, int col) {
		Block[] surroundBlocks = getSurroundBlocks(row, col);
		// ���δ��ǣ���δ�����ӣ���������Ϊpressed��
		if (!blocks[row][col].isFlag()) {
			blocks[row][col].setPressed(true);
		}
		for (int i = 0; i < surroundBlocks.length; i++) {
			if (surroundBlocks[i].isPressed()) {
				continue;
			}
			if (!queue.contains(surroundBlocks[i])) {
				queue.add(surroundBlocks[i]);
			}
		}
		while (!queue.isEmpty()) {
			Block temp = queue.remove();
			// ���δ��ǣ���δ�����ӣ���������Ϊpressed��
			if (!temp.isFlag()) {
				temp.setPressed(true);
			}
			// mineView.getUpdateBlocks().add(temp);
			if (temp.getType() == Block.NUM_TYPE) {
			}
			if (temp.getType() == Block.EMPTY_TYPE) {
				pressEmptyBtn(temp.getRow(), temp.getCol());
			}
		}
	}
	
	private void gameOver() {
		System.out.println("game over!!!");
		// for (int i = 0; i < mineCount; i++) {
		// Block mineBlock =
		// blocks[minePositions[i].getRow()][minePositions[i].getCol()];
		// mineBlock.setPressed(true);
		// mineView.getUpdateBlocks().add(mineBlock);
		// mineView.setPartRefresh(true);
		// }
		mineView.setGameState(MineView.STATE_OVER);
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// �ж��Ƿ�ᳬ���߽磬���ƶ�
		int x = (int) distanceX;
		int y = (int) distanceY;
		if (mineView.getScrollX() + distanceX < 0
				|| mineView.getScrollX() + distanceX > (colSize * Block.BLOCK_SIZE - mineView
						.getMeasuredWidth())) {
			x = 0;
		}
		if (mineView.getScrollY() + distanceY < 0
				|| mineView.getScrollY() + distanceY > (rowSize * Block.BLOCK_SIZE - mineView
						.getMeasuredHeight())) {
			y = 0;
		}
		mineView.scrollBy(x, y);
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if (clickCount == 0) {
			MainActivity mainActivity = (MainActivity) mineView.getContext();
			mainActivity.getTimeHandler().postDelayed(mainActivity.getTimeRunnable(), 1000);
			mineView.setGameState(MineView.STATE_RUNNING);
		}
		clickCount++;
		if (mineView.getGameState() == MineView.STATE_OVER) {
			return;
		}
		super.onLongPress(e);
		Block block = getPressBlock(e);
		if (block == null) {
			return;
		}
		block.setFlag(true);
		// mineView.getUpdateBlocks().add(block);
		// mineView.setPartRefresh(true);
		mineView.invalidate();
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		System.out.println("onSingleTapUp");
		return super.onSingleTapUp(e);
	}

	@Override
	public void onShowPress(MotionEvent e) {
		System.out.println("onShowPress");
		super.onShowPress(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		System.out.println("onDown");
		return super.onDown(e);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (clickCount == 0) {
			MainActivity mainActivity = (MainActivity) mineView.getContext();
			mainActivity.getTimeHandler().postDelayed(mainActivity.getTimeRunnable(), 1000);
			mineView.setGameState(MineView.STATE_RUNNING);
		}
		clickCount++;
		if (mineView.getGameState() == MineView.STATE_OVER) {
			return super.onSingleTapConfirmed(e);
		}
		Block block = getPressBlock(e);
		if (block == null) {
			return super.onSingleTapConfirmed(e);
		}
		if (mineView.getGameState() == MineView.STATE_FLAG) {
			if (block.isFlag()) {
				block.setFlag(false);
			} else {
				block.setFlag(true);
			}
			mineView.invalidate();
			return super.onSingleTapConfirmed(e);
		}
		if (block.isFlag()) {
			block.setFlag(false);
			mineView.invalidate();
			return super.onSingleTapConfirmed(e);
		}
		if (block.getType() == Block.MINE_TYPE) {
			gameOver();
			MainActivity mainActivity = (MainActivity) mineView.getContext();
			mainActivity.getTimeHandler().removeCallbacks(mainActivity.getTimeRunnable());
			block.setPressed(true);
		} else if (block.getType() == Block.NUM_TYPE) {
			int surroundFlagCount = 0;
			Block[] surBlocks = MinesMap.this.getSurroundBlocks(block.getRow(), block.getCol());
			for (int i = 0; i < surBlocks.length; i++) {
				if (surBlocks[i].isFlag()) {
					surroundFlagCount++;
				}
			}
			// ����ɿ����Ǹ�����ܱ����Ĵ��ڵ�������
			// �ж�������û��Ū��
			// û���󣬽�ʣ�µķǡ��ס�����ʾ����
			// ������ֱ��gameover
			if (block.isPressed() && surroundFlagCount >= block.getNum()) {
				if (!MinesMap.this.checkBtn(block.getRow(), block.getCol())) {
					MinesMap.this.gameOver();
				} else {
					// ��ʾʣ�µķǡ��ס���
					for (int i = 0; i < surBlocks.length; i++) {
						// �հ׿飬����ʾ����0
						if (surBlocks[i].getNum() > 0) {
							surBlocks[i].setPressed(true);
						} else if (surBlocks[i].getNum() == 0) {
							MinesMap.this.pressEmptyBtn(surBlocks[i].getRow(),
									surBlocks[i].getCol());
						}
					}
				}
			} else {
				block.setPressed(true);
			}
		} else {
			pressEmptyBtn(block.getRow(), block.getCol());
		}
		// mineView.getUpdateBlocks().add(block);
		// mineView.setPartRefresh(true);
		mineView.invalidate();
		return super.onSingleTapConfirmed(e);
	}

	// ���һ����ť�ܱߵ�������û��Ū��
	// ����true��ʾû��false��ʾ�д���
	private boolean checkBtn(int row, int col) {
		Block[] blocks = getSurroundBlocks(row, col);
		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i].isFlag() && blocks[i].getType() != Block.MINE_TYPE) {
				return false;
			}
		}
		return true;
	}

	// ��ȡ���µ��Ǹ���ť
	private Block getPressBlock(MotionEvent e) {
		int c = (int) ((e.getX() + mineView.getScrollX()) / Block.BLOCK_SIZE);
		int r = (int) ((e.getY() + mineView.getScrollY()) / Block.BLOCK_SIZE);
		if (c >= colSize || c < 0 || r >= rowSize || r < 0) {
			return null;
		}
		return getBlocks()[r][c];
	}

	public Block[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}

}

// "��"λ��
class MinePosition {
	private int row;
	private int col;

	public MinePosition(int row, int col) {
		super();
		this.setRow(row);
		this.setCol(col);
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

}