package dev.weihl.opengl.shape;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

abstract public class Shape implements GLSurfaceView.Renderer {

    View mView;

    public Shape(View view) {
        this.mView = view;
    }


    public int loadShader(int type, String shaderCode){
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    /**
     * 数据本地内存化
     */
    FloatBuffer asFloatBuffer(float src[]) {
        // float 4个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(src.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = vbb.asFloatBuffer();
        buffer.put(src);
        buffer.position(0);
        return buffer;
    }

    ShortBuffer asShortBuffer(short src[]) {
        // short 2个字节
        ByteBuffer ibb = ByteBuffer.allocateDirect(src.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        ShortBuffer buffer = ibb.asShortBuffer();
        buffer.put(src);
        buffer.position(0);
        return buffer;
    }


}
