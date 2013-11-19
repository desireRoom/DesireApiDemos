package com.desire.desireapidemos.animation;

import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class ShapeHolder {
	private Paint mPaint;
	private float x, y;
	private ShapeDrawable shape;
	private int color;
	private RadialGradient gradient;
	private float alpha = 1.0f;

	public void setPaint(Paint paint) {
		mPaint = paint;
	}

	public Paint getPaint() {
		return mPaint;
	}

	public void setX(float value) {
		x = value;
	}

	public float getX() {
		return x;
	}

	public void setY(float value) {
		y = value;
	}

	public float getY() {
		return y;
	}

	public void setShape(ShapeDrawable value) {
		shape = value;
	}

	public ShapeDrawable getShape() {
		return shape;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int value) {
		shape.getPaint().setColor(value);
		color = value;
	}

	public void setGradient(RadialGradient value) {
		gradient = value;
	}

	public RadialGradient getGradient() {
		return gradient;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		shape.setAlpha((int) ((alpha * 255f) + .5f));
	}

	public float getWidth() {
		return shape.getShape().getWidth();
	}

	public void setWidth(float width) {
		Shape s = shape.getShape();
		s.resize(width, s.getHeight());
	}

	public float getHeight() {
		return shape.getShape().getHeight();
	}

	public void setHeight(float height) {
		Shape s = shape.getShape();
		s.resize(s.getWidth(), height);
	}

	public ShapeHolder(ShapeDrawable s) {
		shape = s;
	}

}
