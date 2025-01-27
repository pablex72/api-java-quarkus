package quarkus;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import java.util.List;
import java.util.NoSuchElementException;

@Path("/books")
@Transactional
public class BookResource {

    @Inject
    private BookRepository bookRepository;

//    @GET
//    public List<Book> index(){
//        return bookRepository.listAll();
//    }

    /**
     * Filter by genre
     *
     * @return
     */
    @GET
    @Path("/genre")
    public List<Book> indexByGenre() {
        return bookRepository.list("genre", "comic");
    }

    /**
     * Filter by numPagesRange
     *
     * @return
     */
    @GET
    public List<Book> index(@QueryParam("numPages") Integer numPages) {
        if (numPages == null) {
            return bookRepository.listAll();
        } else {
            return bookRepository.list("numPages >= ?1", numPages);
        }
    }

    /**
     * Filter by titleMatch
     *
     * @return
     */
    @GET
    @Path("/filter-by-title")
    public List<Book> indexByTitle(@QueryParam("q") String query) {
        if (query == null) {
            return bookRepository.listAll();
        } else {
            String filter = "%" + query + "%";
            return bookRepository.list("title ILIKE ?1 OR  description ILIKE ?2", filter, filter);
        }
    }

    /**
     * Filter by order sort
     *
     * @return
     */
    @GET
    @Path("/filter-by-pubDate")
    public List<Book> indexBySort(@QueryParam("q") String query) {
        if (query == null) {
            return bookRepository.listAll(Sort.by("pubDate", Sort.Direction.Descending));
        } else {
            String filter = "%" + query + "%";
            return bookRepository.list("title ILIKE ?1 OR  description ILIKE ?2", filter, filter);
        }
    }

    @POST
    public Book insert(Book insertedBook) {
        bookRepository.persist(insertedBook);
        return insertedBook;
    }

    @GET
    @Path("{id}")
    public Book retrieve(@PathParam("id") Long id) {
        var book = bookRepository.findById(id);

        if (book != null) {
            return book;
        }
        throw new NoSuchElementException("No hay libro con el id" + id);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        bookRepository.deleteById(id);
    }

    @PUT
    @Path("{id}")
    public Book update(@PathParam("id") Long id, Book book) {
        var updatedBook = bookRepository.findById(id);
        if (updatedBook != null) {
            updatedBook.setTitle(book.getTitle());
            updatedBook.setPubDate(book.getPubDate());
            updatedBook.setNumPages(book.getNumPages());
            updatedBook.setDescription(book.getDescription());
            bookRepository.persist(updatedBook);
            return updatedBook;
        }
        throw new NoSuchElementException("No hay libro con el id " + id);
    }
}
