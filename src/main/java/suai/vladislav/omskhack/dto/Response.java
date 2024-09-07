package suai.vladislav.omskhack.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response{

	@JsonProperty("slides")
	private List<SlidesItem> slides;

	@JsonProperty("title")
	private String title;

	public List<SlidesItem> getSlides(){
		return slides;
	}

	public String getTitle(){
		return title;
	}
}