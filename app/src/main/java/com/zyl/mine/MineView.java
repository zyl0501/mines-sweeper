package com.zyl.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class MineView extends View {

	// private static final int BLOCK_SIZE = 47;
	public static final int STATE_OVER = -1;
	public static final int STATE_UNSTART = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_FLAG = 2;

	private int rowSize = 30;
	private int colSize = 30;
	private int mineCount = 80;
	private GestureDetector gestureDetector;
	private Block[][] blocks;
	private MinesMap minesMap;
	private int gameState = STATE_UNSTART;

	// private List<Block> updateBlocks;
	// private boolean partRefresh;

	Bitmap unpressBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_unpress);
	Bitmap flagBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_flag_yes);
	Bitmap num1Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_01);
	Bitmap num2Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_02);
	Bitmap num3Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_03);
	Bitmap num4Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_04);
	Bitmap num5Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_05);
	Bitmap num6Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_06);
	Bitmap num7Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_07);
	Bitmap num8Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_08);
	Bitmap emptyPressedBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.metal_empty_pressed);
	Bitmap mineUnpressBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.metal_mine_unpress);
	Bitmap minePressBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.metal_mine_pressed);
	Bitmap flagWrongBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.metal_flag_wrong);
	Bitmap flagNoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.metal_flag_no);

	public MineView(Context context) {
		super(context);
		minesMap = new MinesMap(MineView.this, rowSize, colSize, mineCount);
		gestureDetector = new GestureDetector(context, minesMap);
		// setUpdateBlocks(new LinkedList<Block>());
		blocks = minesMap.getBlocks();
	}

	public MineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		minesMap = new MinesMap(MineView.this, rowSize, colSize, mineCount);
		gestureDetector = new GestureDetector(context, minesMap);
		// setUpdateBlocks(new LinkedList<Block>());
		blocks = minesMap.getBlocks();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// if (isPartRefresh()) {
		// for (Iterator<Block> iterator = updateBlocks.iterator();
		// iterator.hasNext();) {
		// drawBlock(canvas, iterator.next());
		// }
		// System.out.println(updateBlocks.size());
		// updateBlocks.clear();
		// setPartRefresh(false);
		// return;
		// }

		if (gameState == MineView.STATE_UNSTART) {
			// 如果view可以显示下整个宽度或高度，则居中显示
			int x = 0;
			int y = 0;
			if (MineView.this.getColSize() * Block.BLOCK_SIZE <= MineView.this.getMeasuredWidth()) {
				x = -(MineView.this.getMeasuredWidth() - MineView.this.getColSize()
						* Block.BLOCK_SIZE) / 2;
			}
			if (MineView.this.getRowSize() * Block.BLOCK_SIZE <= MineView.this.getMeasuredHeight()) {
				y = -(MineView.this.getMeasuredHeight() - MineView.this.getRowSize()
						* Block.BLOCK_SIZE) / 2;
			}
			if (x != 0 || y != 0) {
				MineView.this.scrollTo(x, y);
			}
			// gameState = MineView.STATE_RUNNING;
		}

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				drawBlock(canvas, blocks[i][j]);
			}
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		System.out.println("onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// WindowManager windowManager = (WindowManager)
		// MineView.this.getContext().getSystemService(
		// Context.WINDOW_SERVICE);
		// setMeasuredDimension(windowManager.getDefaultDisplay().getWidth(),
		// windowManager
		// .getDefaultDisplay().getHeight() * 5 / 8);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return true;
	}

	// draw相应位置的block
	private void drawBlock(Canvas canvas, Block block) {
		int row = block.getRow();
		int col = block.getCol();
		// 按下的block
		if (block.isPressed()) {
			switch (block.getNum()) {
			case 1:
				// canvas.drawBitmap(num1Bitmap, col * Block.BLOCK_SIZE, row *
				// Block.BLOCK_SIZE, null);
				canvas.drawBitmap(num1Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 2:
				canvas.drawBitmap(num2Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 3:
				canvas.drawBitmap(num3Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 4:
				canvas.drawBitmap(num4Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 5:
				canvas.drawBitmap(num5Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 6:
				canvas.drawBitmap(num6Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 7:
				canvas.drawBitmap(num7Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 8:
				canvas.drawBitmap(num8Bitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			case 0:
				canvas.drawBitmap(emptyPressedBitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);

				break;
			case -1:
				canvas.drawBitmap(minePressBitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
				break;
			default:
				break;
			}
		}
		// 未按下，非标记的block
		else if (!block.isFlag()) {
			if (block.getType() == Block.MINE_TYPE && gameState == MineView.STATE_OVER) {
				canvas.drawBitmap(mineUnpressBitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
			} else if (gameState == MineView.STATE_FLAG) {
				canvas.drawBitmap(flagNoBitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
			} else {
				canvas.drawBitmap(unpressBitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
			}
		}
		// 未按下，标记的block
		else {
			if (block.getType() != Block.MINE_TYPE && gameState == MineView.STATE_OVER) {
				canvas.drawBitmap(flagWrongBitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
			} else {
				canvas.drawBitmap(flagBitmap, null, new Rect(col * Block.BLOCK_SIZE, row
						* Block.BLOCK_SIZE, (col + 1) * Block.BLOCK_SIZE, (row + 1)
						* Block.BLOCK_SIZE), null);
			}
		}
	}

	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	public int getRowSize() {
		return rowSize;
	}

	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}

	public int getColSize() {
		return colSize;
	}

	public void setColSize(int colSize) {
		this.colSize = colSize;
	}
}
