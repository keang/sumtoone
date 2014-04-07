package com.vibol.sumtoone;

import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity{
	TextView num1TextView;
	TextView num2TextView;
	TextView num3TextView;
	TextView num4TextView;
	TextView sumTextView;
	TextView clickCountTextView;
	int one, two, three, four;
	int clickCount;
	int undoCount=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		getActionBar().hide();
		num1TextView = (TextView) findViewById(R.id.num1);
		num2TextView = (TextView) findViewById(R.id.num2);
		num3TextView = (TextView) findViewById(R.id.num3);
		num4TextView = (TextView) findViewById(R.id.num4);
		sumTextView = (TextView)findViewById(R.id.sumTextView);
		clickCountTextView = (TextView)findViewById(R.id.clickCountTextView);
		View wholescreen = findViewById(R.id.wholescreen);
		wholescreen.setOnTouchListener(new OnSwipeTouchListener(this){
			public void onSwipeTop() {
				one-=three;
				two-=four;
				updateGameState();
		    }
		    public void onSwipeRight() {
		    	two-=one;
		    	four-=three;
				updateGameState();
		    }
		    public void onSwipeLeft() {
		    	one-=two;
		    	three-=four;
				updateGameState();
		    	
		    }
		    public void onSwipeBottom() {
		    	three-=one;
		    	four-=two;
				updateGameState();
		    }
		});
		((Button)findViewById(R.id.resetButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				init();
			}
		});
		((Button)findViewById(R.id.undoButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				undo();
			}
		});
		if(savedInstanceState==null)init();
		else {
			one = savedInstanceState.getInt("one");
			two = savedInstanceState.getInt("two");
			three = savedInstanceState.getInt("three");
			four = savedInstanceState.getInt("four");
			clickCount = savedInstanceState.getInt("clickCount");
			refreshView();
		}
	}
	
	protected void undo() {
		Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		 // Vibrate for 500 milliseconds
		 v.vibrate(200);
		 undoCount++;
		 if(undoCount==10){
			 Toast.makeText(this, "Quit it; Just think before you move.", Toast.LENGTH_SHORT).show();
			 undoCount=0;
			 
		 } else if(undoCount==5){
			 Toast.makeText(this, "There is no Ctrl+Z in real life either", Toast.LENGTH_SHORT).show();
		 }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("one", one);
		outState.putInt("two", two);
		outState.putInt("three", three);
		outState.putInt("four", four);
		outState.putInt("clickCount", clickCount);
		super.onSaveInstanceState(outState);
	}

	protected void refreshView(){
		num1TextView.setText(""+one);
		num2TextView.setText(""+two);
		num3TextView.setText(""+three);
		num4TextView.setText(""+four);
		int sum = one+two+three+four;
		sumTextView.setText("Sum: "+sum);
		clickCountTextView.setText("Moves: "+clickCount);
	}
	protected void updateGameState() {
		clickCount++;
		refreshView();
		//check if game over
		if(hasLost()){
			gameOver();
		}
		else if (one+two+three+four==1) gameWon();
	}

	private boolean hasLost() {
		return abs(one)>1000||abs(two)>1000||abs(three)>1000||abs(four)>1000;
	}

	private int abs(int num) {
		return num>0?num:-num;
	}

	private void init(){
		Random generater = new Random();
		do{
			one = generater.nextInt(100);
			two = generater.nextInt(100);
			three = generater.nextInt(100);
			four = generater.nextInt(100);
		} while (one%2==0 && two%2==0&&three%2==0&&four%2==0);

		clickCount = 0;
		sumTextView.setTextSize(24);
		refreshView();
	}

	private void gameOver(){
		//ask play or exit
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setTitle(R.string.game_over_message)
				.setPositiveButton(R.string.replay, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       init();
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       finish();
                   }
               })
               .setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						init();
					}
			});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void gameWon() {
		sumTextView.setTextSize(42);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setTitle(R.string.game_won_message)
				.setNegativeButton(R.string.replay, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       init();
                   }
               })
               .setPositiveButton(R.string.won_cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               });

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
