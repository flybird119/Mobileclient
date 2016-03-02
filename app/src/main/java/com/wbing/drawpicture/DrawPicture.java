package com.wbing.drawpicture;
import com.yqman.service.WiFiSupportService;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawPicture {
	private static final String TAG="DrawPicture";
	private SurfaceHolder surfaceholder;


	private SurfaceView msurfacedraw;
	private float startx=60;  //最小为20
	private float endx=670;
	private float starty=380;
	private float endy=10;
	public DrawPicture(SurfaceView surfacedraw)
	{
		msurfacedraw = surfacedraw;
		surfaceholder = surfacedraw.getHolder();
		msurfacedraw.setZOrderOnTop(true);
		surfaceholder.setFormat(PixelFormat.TRANSLUCENT);
	}
	public void drawPhoto(int moist, int temperature, int sun,boolean isHistory) {

		WiFiSupportService.moiValue[WiFiSupportService.counter]=moist;
		WiFiSupportService.temValue[WiFiSupportService.counter]=temperature + 120;
		WiFiSupportService.lightValue[WiFiSupportService.counter]=sun + 240;
		if(isHistory)
		{
			drawfigure_history();
		}
		else
		{
			drawfigure();
		}

		WiFiSupportService.counter+=1;
		if (WiFiSupportService.counter >= WiFiSupportService.counter_num) {
			WiFiSupportService.counter = WiFiSupportService.counter_num-1;
			for (int i = 0; i < WiFiSupportService.counter_num-1; ++i) {
				WiFiSupportService.moiValue[i] = WiFiSupportService.moiValue[i + 1];
				WiFiSupportService.temValue[i] = WiFiSupportService.temValue[i + 1];
				WiFiSupportService.lightValue[i] = WiFiSupportService.lightValue[i + 1];
			}
		}

	}
	public void drawfigure(){
		Log.d(TAG, "surfaceholder  ---"+surfaceholder);
		Canvas canvas = surfaceholder.lockCanvas(new Rect(0,0,650,420));// 关键:获取画布  
		Log.d(TAG, "cancas"+canvas);
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);// 画笔为黑色
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);

		Paint temPaint = new Paint();
		temPaint.setColor(Color.RED);
		temPaint.setStyle(Paint.Style.STROKE);
		temPaint.setAntiAlias(true);

		Paint lightPaint = new Paint();
		lightPaint.setColor(Color.BLUE);
		lightPaint.setStyle(Paint.Style.STROKE);
		lightPaint.setAntiAlias(true);

		canvas.drawLine(startx, starty, endx, starty, paint);//画x轴，改250为150
		canvas.drawLine(startx, starty, startx, endy, paint);//画y轴，改250为150
		//画x轴刻度

		//
		for(int x=0; x<13; x++)
		{
			canvas.drawLine(startx+50*x,  starty, startx+50*x, starty-3, paint);
			canvas.drawLine(startx+50*x,  starty-120, startx+50*x, starty-120-3, paint);
			canvas.drawLine(startx+50*x,  starty-240, startx+50*x, starty-240-3, paint);

		}

		//画y轴刻度
		for(int y=0; y<12; y++)
		{
			canvas.drawLine(startx, starty-30*y, startx+3, starty-30*y, paint);

		}

		//画三条分割线
		canvas.drawLine(startx,  starty-120, endx, starty-120, paint);
		canvas.drawLine(startx,  starty-240, endx, starty-240, paint);



		paint.setTextSize(10);
		for(int x=1; x<12; x++)
		{
			//x轴描十个点 范围从startx~startx+600  隔50个像素一个点
			canvas.drawText(""+5*x+"s", startx+50*x-5, starty+10, paint);
		}
		for(int x=1; x<12; x++)
		{
			//x轴描十个点 范围从startx~startx+600  隔50个像素一个点
			canvas.drawText(""+5*x+"s", startx+50*x-5, starty+10-120, paint);
		}
		for(int x=1; x<12; x++)
		{
			//x轴描十个点 范围从startx~startx+600  隔50个像素一个点
			canvas.drawText(""+5*x+"s", startx+50*x-5, starty+10-240, paint);
		}


//	    for(int y=1; y<6; y++)
//	    {
//	    	//正常显示 以后再改 starty~starty+300  隔60个像素一个点共五个
//	    	canvas.drawText(""+60*y, startx-20, starty-60*y+5, paint);
//	    }
		for(int y=0; y<4; y++)
		{
			//正常显示 120像素三十1个点
			canvas.drawText(""+30*y, startx-20, starty-30*y+5, paint);
		}
		for(int y=0; y<4; y++)
		{
			//正常显示 120像素三十1个点
			canvas.drawText(""+30*y, startx-20, starty-30*y+5-120, paint);
		}
		for(int y=0; y<4; y++)
		{
			//正常显示 120像素三十1个点
			canvas.drawText(""+30*y, startx-20, starty-30*y+5-240, paint);
		}
//	    Paint tmpPaint = new Paint();
//	    
//	    tmpPaint.setStyle(Paint.Style.STROKE);
//	    tmpPaint.setAntiAlias(true);
//	    paint.setTextSize(50);
//	    tmpPaint.setColor(Color.BLACK);
//	    canvas.drawText("湿度", endx-20, starty-120+20, tmpPaint);	    
//	    tmpPaint.setColor(Color.RED);
//	    canvas.drawText("温度", endx-20, starty-240+20, tmpPaint);
//	    tmpPaint.setColor(Color.BLUE);
//	    canvas.drawText("光强", endx-20, starty-360+20, tmpPaint);
		for(int i=0;i<WiFiSupportService.counter;i++) {
			//一个空格之间即一秒之间会画五个点  共计会画50个点
			canvas.drawLine(startx+12*i,starty - WiFiSupportService.moiValue[i], startx+12+12*i, starty-WiFiSupportService.moiValue[i+1], paint);
			canvas.drawLine(startx+12*i,starty - WiFiSupportService.temValue[i], startx+12+12*i, starty-WiFiSupportService.temValue[i+1], temPaint);
			canvas.drawLine(startx+12*i,starty - WiFiSupportService.lightValue[i], startx+12+12*i, starty-WiFiSupportService.lightValue[i+1], lightPaint);
		}
		surfaceholder.unlockCanvasAndPost(canvas);
	}
	public void drawfigure_history(){
		//显示历史情况下的画图
		Log.d(TAG, "surfaceholder  ---"+surfaceholder);
		Canvas canvas = surfaceholder.lockCanvas(new Rect(0,0,650,420));// 关键:获取画布  
		Log.d(TAG, "cancas"+canvas);
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);// 画笔为黑色
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);

		Paint temPaint = new Paint();
		temPaint.setColor(Color.RED);
		temPaint.setStyle(Paint.Style.STROKE);
		temPaint.setAntiAlias(true);

		Paint lightPaint = new Paint();
		lightPaint.setColor(Color.BLUE);
		lightPaint.setStyle(Paint.Style.STROKE);
		lightPaint.setAntiAlias(true);

		canvas.drawLine(startx, starty, endx, starty, paint);//画x轴，改250为150
		canvas.drawLine(startx, starty, startx, endy, paint);//画y轴，改250为150
		//画x轴刻度

		//
		for(int x=0; x<13; x++)
		{
			canvas.drawLine(startx+50*x,  starty, startx+50*x, starty-3, paint);
			canvas.drawLine(startx+50*x,  starty-120, startx+50*x, starty-120-3, paint);
			canvas.drawLine(startx+50*x,  starty-240, startx+50*x, starty-240-3, paint);

		}

		//画y轴刻度
		for(int y=0; y<12; y++)
		{
			canvas.drawLine(startx, starty-30*y, startx+3, starty-30*y, paint);

		}

		//画三条分割线
		canvas.drawLine(startx,  starty-120, endx, starty-120, paint);
		canvas.drawLine(startx,  starty-240, endx, starty-240, paint);


		///////////////////////////////////////////////////////////////////////////
		//十个像素一个一分钟的点  共现实一小时数据
		paint.setTextSize(10);
		for(int x=1; x<13; x++)
		{
			//x轴描十个点 范围从startx~startx+600  隔50个像素一个点
			canvas.drawText(""+5*x+"分", startx+50*x-5, starty+10, paint);
		}
		for(int x=1; x<13; x++)
		{
			//x轴描十个点 范围从startx~startx+600  隔50个像素一个点
			canvas.drawText(""+5*x+"分", startx+50*x-5, starty+10-120, paint);
		}
		for(int x=1; x<13; x++)
		{
			//x轴描十个点 范围从startx~startx+600  隔50个像素一个点
			canvas.drawText(""+5*x+"分", startx+50*x-5, starty+10-240, paint);
		}

		///////////////////////////////////////////////////////////////////////////
//	    for(int y=1; y<6; y++)
//	    {
//	    	//正常显示 以后再改 starty~starty+300  隔60个像素一个点共五个
//	    	canvas.drawText(""+60*y, startx-20, starty-60*y+5, paint);
//	    }
		for(int y=0; y<4; y++)
		{
			//正常显示 120像素三十1个点
			canvas.drawText(""+30*y, startx-20, starty-30*y+5, paint);
		}
		for(int y=0; y<4; y++)
		{
			//正常显示 120像素三十1个点
			canvas.drawText(""+30*y, startx-20, starty-30*y+5-120, paint);
		}
		for(int y=0; y<4; y++)
		{
			//正常显示 120像素三十1个点
			canvas.drawText(""+30*y, startx-20, starty-30*y+5-240, paint);
		}
//	    Paint tmpPaint = new Paint();
//	    
//	    tmpPaint.setStyle(Paint.Style.STROKE);
//	    tmpPaint.setAntiAlias(true);
//	    paint.setTextSize(50);
//	    tmpPaint.setColor(Color.BLACK);
//	    canvas.drawText("湿度", endx-20, starty-120+20, tmpPaint);	    
//	    tmpPaint.setColor(Color.RED);
//	    canvas.drawText("温度", endx-20, starty-240+20, tmpPaint);
//	    tmpPaint.setColor(Color.BLUE);
//	    canvas.drawText("光强", endx-20, starty-360+20, tmpPaint);
		for(int i=0;i<WiFiSupportService.counter;i++) {
			//一个空格之间即一秒之间会画五个点  共计会画50个点
			canvas.drawLine(startx+12*i,starty - WiFiSupportService.moiValue[i], startx+12+12*i, starty-WiFiSupportService.moiValue[i+1], paint);
			canvas.drawLine(startx+12*i,starty - WiFiSupportService.temValue[i], startx+12+12*i, starty-WiFiSupportService.temValue[i+1], temPaint);
			canvas.drawLine(startx+12*i,starty - WiFiSupportService.lightValue[i], startx+12+12*i, starty-WiFiSupportService.lightValue[i+1], lightPaint);
		}
		surfaceholder.unlockCanvasAndPost(canvas);
	}
}
