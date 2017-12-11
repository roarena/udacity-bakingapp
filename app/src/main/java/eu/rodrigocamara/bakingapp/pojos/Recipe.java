package eu.rodrigocamara.bakingapp.pojos;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Recipe {

	@SerializedName("image")
	private String image;

	@SerializedName("servings")
	private int servings;

	@SerializedName("name")
	private String name;

	@SerializedName("ingredients")
	private List<IngredientsItem> ingredients;

	@SerializedName("id")
	private int id;

	@SerializedName("steps")
	private List<StepsItem> steps;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setServings(int servings){
		this.servings = servings;
	}

	public int getServings(){
		return servings;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIngredients(List<IngredientsItem> ingredients){
		this.ingredients = ingredients;
	}

	public List<IngredientsItem> getIngredients(){
		return ingredients;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setSteps(List<StepsItem> steps){
		this.steps = steps;
	}

	public List<StepsItem> getSteps(){
		return steps;
	}

	public int getNumberSteps(){
		return steps.size();
	}

	@Override
 	public String toString(){
		return 
			"Recipe{" +
			"image = '" + image + '\'' + 
			",servings = '" + servings + '\'' + 
			",name = '" + name + '\'' + 
			",ingredients = '" + ingredients + '\'' + 
			",id = '" + id + '\'' + 
			",steps = '" + steps + '\'' + 
			"}";
		}
}