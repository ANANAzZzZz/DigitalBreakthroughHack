package suai.vladislav.omskhack.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ElementsItem{

	@JsonProperty("src")
	private String src;

	@JsonProperty("position")
	private Position position;

	@JsonProperty("type")
	private String type;

	@JsonProperty("fontSize")
	private int fontSize;

	@JsonProperty("content")
	private String content;

	@JsonProperty("headers")
	private List<String> headers;

	@JsonProperty("rows")
	private List<String> rows;

	public String getSrc(){
		return src;
	}

	public Position getPosition(){
		return position;
	}

	public String getType(){
		return type;
	}

	public int getFontSize(){
		return fontSize;
	}

	public String getContent(){
		return content;
	}

	public List<String> getHeaders(){
		return headers;
	}

	public List<String> getRows(){
		return rows;
	}
}