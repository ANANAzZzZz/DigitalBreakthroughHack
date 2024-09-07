package suai.vladislav.omskhack.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SlidesItem{

	@JsonProperty("elements")
	private List<ElementsItem> elements;

	@JsonProperty("title")
	private String title;

	public List<ElementsItem> getElements(){
		return elements;
	}

	public String getTitle(){
		return title;
	}
}