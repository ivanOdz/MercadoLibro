package ar.edu.itba.paw.persistence;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Image;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Primary
@Repository
public class ImageJpaDao implements ImageDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public Image createImage(byte[] image) {
		System.out.println("INSIDE ImageJpaDao");
		Image img = new Image(null, image);
		System.out.println("created image");
		em.persist(img);
		System.out.println("persisted image");
		return img;
	}

	@Override
	public Optional<Image> getImageById(Long imageId) {
		Image image = em.find(Image.class, imageId);
		return Optional.ofNullable(image);
	}
}
