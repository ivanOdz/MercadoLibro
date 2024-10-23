package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "image")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageId;
	
	@Lob
	@Column(name = "image", nullable = false)
	private byte[] image;
	
	public Image() {}
	
	public Image(Integer imageId, byte[] image) {
		this.imageId = imageId;
		this.image = image;
	}
	
	public Integer getImageId() {
		return imageId;
	}
	
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}
}
