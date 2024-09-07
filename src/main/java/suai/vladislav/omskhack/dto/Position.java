package suai.vladislav.omskhack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position{

	@JsonProperty("x")
	private int X;

	@JsonProperty("width")
	private int width;

	@JsonProperty("y")
	private int Y;

	@JsonProperty("height")
	private int height;

	public int getX(){
		return X;
	}

	public int getWidth(){
		return width;
	}

	public int getY(){
		return Y;
	}

	public int getHeight(){
		return height;
	}
}