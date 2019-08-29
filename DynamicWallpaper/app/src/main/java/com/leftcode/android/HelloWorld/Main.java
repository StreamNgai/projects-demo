/* http://www.leftcode.com */

package com.leftcode.android.HelloWorld;

import android.app.Activity;
import android.opengl.*;
import android.os.Bundle;
import android.view.*;

public class Main extends Activity {
	private GLSurfaceView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置窗体为全屏模式，无标题
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		/* 创建一个 GLSurfaceView 用于绘制表面 */
		view = new GLSurfaceView(this);

		/* 设置 Renderer 用于执行实际的绘制工作 */
		view.setRenderer(new HelloWorldRenderer(this));

		/* 设置绘制模式为 持续绘制 */
		view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

		/* 将创建好的 GLSurfaceView 设置为当前 Activity 的内容视图 */
		setContentView(view);
	}
}