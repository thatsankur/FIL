package fl.ub.model;

public class FlickerAPIResponse {
	private String stat;
	private Photos photos;

	public void setStat(String stat){
		this.stat = stat;
	}

	public String getStat(){
		return stat;
	}

	public void setPhotos(Photos photos){
		this.photos = photos;
	}

	public Photos getPhotos(){
		return photos;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"stat = '" + stat + '\'' + 
			",photos = '" + photos + '\'' + 
			"}";
		}
}
