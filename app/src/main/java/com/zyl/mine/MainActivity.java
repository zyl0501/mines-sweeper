package com.zyl.mine;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	// private RelativeLayout relativeLayout;
	private ImageButton zoomInButton;
	private ImageButton zoomOutButton;
	private ImageButton flagButton;
	private MineView mineView;
	private Handler timeHandler = new Handler();
	private Runnable timeRunnable = new Runnable() {
		private int recLen = 0;

		public void run() {
			recLen++;
			System.out.println("ʱ�䣺" + recLen);
			timeHandler.postDelayed(this, 1000);
		}
	};

	private int zoomStep;
	private int zoomMax ;
	private int zoomMin ;
	private int defaultBlockSize;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Block.setBlockSize(defaultBlockSize);
		zoomStep = ViewUtils.dp2px(this, 20);
		zoomMax = ViewUtils.dp2px(this, 100);
		zoomMin = ViewUtils.dp2px(this, 28);
		defaultBlockSize = ViewUtils.dp2px(this, 30);
		Block.BLOCK_SIZE = defaultBlockSize;

		setContentView(R.layout.activity_mine);
		// relativeLayout = (RelativeLayout) findViewById(R.id.mine_layout);
		// mineView = new MineView(relativeLayout.getContext());
		// relativeLayout.addView(mineView);
		mineView = (MineView) findViewById(R.id.mineview);
		zoomInButton = (ImageButton) findViewById(R.id.zoom_in_btn);
		zoomOutButton = (ImageButton) findViewById(R.id.zoom_out_btn);
		flagButton = (ImageButton) findViewById(R.id.flag_btn);
		zoomInButton.setOnClickListener(new ZoomInBtnOnClickListener());
		zoomOutButton.setOnClickListener(new ZoomOutBtnOnClickListener());
		flagButton.setOnClickListener(new FlagBtnOnClickListener());
	}

	// �Ŵ�ť������
	class ZoomInBtnOnClickListener implements OnClickListener {

		public void onClick(View v) {
			if (Block.BLOCK_SIZE < zoomMax) {
				Block.BLOCK_SIZE += zoomStep;
				updateMineView(0);
			}
		}

	}

	// ��С��ť������
	class ZoomOutBtnOnClickListener implements OnClickListener {

		public void onClick(View v) {
			if (Block.BLOCK_SIZE > zoomMin) {
				Block.BLOCK_SIZE -= zoomStep;
				updateMineView(1);
			}
		}

	}

	class FlagBtnOnClickListener implements OnClickListener {

		public void onClick(View v) {
			if (mineView.getGameState() == MineView.STATE_FLAG) {
				mineView.setGameState(MineView.STATE_RUNNING);
				mineView.invalidate();
				flagButton.setImageResource(R.drawable.flag_btn_up);
			} else if (mineView.getGameState() == MineView.STATE_RUNNING) {
				mineView.setGameState(MineView.STATE_FLAG);
				mineView.invalidate();
				flagButton.setImageResource(R.drawable.flag_btn_down);
			}
		}
	}

	// ��ȡ������Ҫ�ƶ����Ͻǵ�scrollX��scrollY
	private Map<String, Integer> getPositionMap(int op) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int beforeBlockSize;
		if (op == 0)
			beforeBlockSize = Block.BLOCK_SIZE - zoomStep;
		else {
			beforeBlockSize = Block.BLOCK_SIZE + zoomStep;
		}
		int x = (mineView.getScrollX() + mineView.getMeasuredWidth() / 2) * Block.BLOCK_SIZE
				/ beforeBlockSize - mineView.getMeasuredWidth() / 2;
		int y = (mineView.getScrollY() + mineView.getMeasuredHeight() / 2) * Block.BLOCK_SIZE
				/ beforeBlockSize - mineView.getMeasuredHeight() / 2;
		if (x < 0) {
			// ��߽�
			x = 0;
		} else if (x > mineView.getColSize() * Block.BLOCK_SIZE - mineView.getMeasuredWidth()) {
			// �ұ߽�
			x = mineView.getColSize() * Block.BLOCK_SIZE - mineView.getMeasuredWidth();
		}
		if (y < 0) {
			// �ϱ߽�
			y = 0;
		} else if (y > mineView.getRowSize() * Block.BLOCK_SIZE - mineView.getMeasuredHeight()) {
			// �±߽�
			y = mineView.getRowSize() * Block.BLOCK_SIZE - mineView.getMeasuredHeight();
		}
		// ���view������ʾ��������Ȼ�߶ȣ��������ʾ
		if (mineView.getColSize() * Block.BLOCK_SIZE <= mineView.getMeasuredWidth()) {
			x = -(mineView.getMeasuredWidth() - mineView.getColSize() * Block.BLOCK_SIZE) / 2;
		}
		if (mineView.getRowSize() * Block.BLOCK_SIZE <= mineView.getMeasuredHeight()) {
			y = -(mineView.getMeasuredHeight() - mineView.getRowSize() * Block.BLOCK_SIZE) / 2;
		}
		map.put("x", x);
		map.put("y", y);
		return map;
	}

	private void updateMineView(int op) {
		Map<String, Integer> positionMap = getPositionMap(op);
		int x = positionMap.get("x");
		int y = positionMap.get("y");
		mineView.scrollTo(x, y);
		// mineView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_mine, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroy");
		timeHandler.removeCallbacks(timeRunnable);
	}

	public Handler getTimeHandler() {
		return timeHandler;
	}

	public void setTimeHandler(Handler timeHandler) {
		this.timeHandler = timeHandler;
	}

	public Runnable getTimeRunnable() {
		return timeRunnable;
	}

	public void setTimeRunnable(Runnable timeRunnable) {
		this.timeRunnable = timeRunnable;
	}
}
