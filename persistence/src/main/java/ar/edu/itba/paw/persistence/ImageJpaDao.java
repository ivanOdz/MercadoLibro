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
	private EntityManager entityManager;
	
	@Override
	@Transactional
	public Image createImage(byte[] image) {
		
		Image img = new Image();
		img.setImage(image);
		entityManager.persist(img);
		return img;
	}
	
	@Override
	public Optional<Image> getImageById(Long imageId) {
	
	Image image = entityManager.find(Image.class, imageId);
		return Optional.ofNullable(image);
	}
	
	@Override
	public Image getFirstImageByBookId(Long bookId) {
		
		String query = "SELECT i FROM Image i JOIN BookImage b ON i.imageId = b.image.imageId " +
		   "WHERE b.book.bookId = :bookId AND b.imageOrder = 0";
		
		return entityManager.createQuery(query, Image.class)
		.setParameter("bookId", bookId)
		.getResultList()
		.stream()
		.findFirst()
		.orElse(null);
	}
}
