package be.thomasmore.bookdb.controller;

import be.thomasmore.bookdb.AuthorForm;
import be.thomasmore.bookdb.BookForm;
import be.thomasmore.bookdb.model.Author;
import be.thomasmore.bookdb.model.Book;
import be.thomasmore.bookdb.model.Genre;
import be.thomasmore.bookdb.repositories.AuthorRepository;
import be.thomasmore.bookdb.repositories.BookRepository;
import be.thomasmore.bookdb.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/create-book")
    public String createParty(Model model){
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "admin/create-book";
    }
    @PostMapping({"/create-book"})
    public String createBookPost(@ModelAttribute BookForm bookForm,
                                 Model model) {
        Optional<Book> bookOptional = bookRepository.findByTitleLike(bookForm.getBookTitle());
        Optional<Genre> genreOptional =genreRepository.findByGenreNameLike(bookForm.getBookGenre());
        Optional<Author> authorOptional = authorRepository.findByNameLike(bookForm.getBookAuthor());
        if(!bookOptional.isPresent()){
            if(bookForm.getBookTitle()!= null && bookForm.getBookAuthor()!= null && bookForm.getBookGenre()!= null ){
                Book book = new Book();
                bookDetails(book, genreOptional, authorOptional, bookForm);
            }
        }
        return "redirect:/books";
    }

    private void bookDetails(Book book,Optional <Genre> genreOptional, Optional<Author> authorOptional, BookForm bookForm){
        book.setTitle(bookForm.getBookTitle());
        if(authorOptional.isPresent()){
            book.setAuthor(authorOptional.get());
        }
        if (genreOptional.isPresent()) {
            book.setGenre(genreOptional.get());
        }
        book.setIsbn(bookForm.getBookIsbn());
        book.setReadFromString(bookForm.getBookRead());
        book.setPages(bookForm.getBookPages());
        bookRepository.save(book);
    }

    @GetMapping("/edit-book/{bookID}")
    public String editBook(@PathVariable(required = false) int bookID, Model model){
        Optional<Book> bookOptional = bookRepository.findById(bookID);
        Book bookFromDb = null;
        if(bookOptional.isPresent()){
            bookFromDb = bookOptional.get();
        }
        model.addAttribute("book", bookFromDb);
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        for(Genre g: genreRepository.findAll()){
            if(g.getGenreName().equals(bookFromDb.getGenre().getGenreName())){
                model.addAttribute("selectedGenre", g);
            }

        }
        for (Author a: authorRepository.findAll()){
            if(a.getName().equals(bookFromDb.getAuthor().getName())){
                model.addAttribute("selectedAuthor", a);
            }
        }
        return "admin/edit-book";
    }
    @PostMapping("/edit-book/{bookID}")
    public String postEditBook(@PathVariable(required = false) int bookID, @ModelAttribute BookForm bookForm ){

        Optional<Book> bookOptional = bookRepository.findById(bookID);
        Optional<Genre> genreOptional = genreRepository.findByGenreNameLike(bookForm.getBookGenre());
        Optional<Author> authorOptional = authorRepository.findByNameLike(bookForm.getBookAuthor());
        if(bookOptional.isPresent()){
            Book book = bookOptional.get();
            bookDetails(book, genreOptional, authorOptional,bookForm);
        }
        return "redirect:/book/"+bookID;
    }

    private void authorDetails(AuthorForm authorForm, Author author){
        author.setName(authorForm.getAuthorName());
        author.setMoreInfo(authorForm.getAuthorInfo());
        author.setMostKnownFor(authorForm.getAuthorKnownFor());
        authorRepository.save(author);
    }

    @GetMapping("/create-author")
    public String createAuthor(Model model){
        return "admin/create-author";
    }

    @PostMapping({"/create-author"})
    public String createAuthorPost(@ModelAttribute AuthorForm authorForm, Model model) {
        Optional<Author> authorOptional = authorRepository.findByNameLike(authorForm.getAuthorName());
        if(!authorOptional.isPresent()){
            if(authorForm.getAuthorName()!= null && authorForm.getAuthorKnownFor()!= null && authorForm.getAuthorInfo() != null){
                Author author = new Author();
                authorDetails(authorForm,  author);
            }
        }

        return "redirect:/authors";
    }

    @GetMapping("/edit-author/{authorID}")
    public String editAuthor(@PathVariable(required = false) int authorID, Model model){
        Optional<Author> authorOptional = authorRepository.findById(authorID);
        Author authorFromDb = null;
        if(authorOptional.isPresent()){
            authorFromDb = authorOptional.get();
        }
        model.addAttribute("author", authorFromDb);
        return "admin/edit-author";
    }

    @PostMapping({"/edit-author/{authorID}"})
    public String editAuthorPost(  @PathVariable(required = false) int authorID, @ModelAttribute AuthorForm authorForm,
                                   Model model) {
        Optional<Author> authorOptional = authorRepository.findById(authorID);
        if(authorOptional.isPresent()){
                Author author = authorOptional.get();
                authorDetails(authorForm, author);
        }

        return "redirect:/author/"+authorID;
    }
    @GetMapping("/create-genre")
    public String createGenre(Model model){
        return "admin/create-genre";
    }
    @PostMapping({"/create-genre"})
    public String createGenrePost( @RequestParam String genreName,
                                   Model model) {
        Optional<Genre> genreOptional = genreRepository.findByGenreNameLike(genreName);
        if(!genreOptional.isPresent()){
            if(genreName!= null){
                Genre genre =new Genre();
                genre.setGenreName(genreName);
               genreRepository.save(genre);
            }
        }

        return "redirect:/books";
    }








}
