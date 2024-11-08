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
	public Image createImage(byte[] image) {
		Image img = new Image(null, image);
		em.persist(img);
		return img;
	}

	@Override
	public Optional<Image> getImageById(Long imageId) {
		Image image = em.find(Image.class, imageId);
		return Optional.ofNullable(image);
	}
}
