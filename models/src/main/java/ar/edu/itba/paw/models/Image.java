package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_image_id_seq")
	@SequenceGenerator(sequenceName = "image_image_id_seq", name = "image_image_id_seq", allocationSize = 1)
	@Column(name = "imageid")
	private Integer imageId;
	
	@Column
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
