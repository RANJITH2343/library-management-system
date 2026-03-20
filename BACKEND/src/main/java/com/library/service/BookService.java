package com.library.service;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service @RequiredArgsConstructor
public class BookService {
    private final BookRepository repo;

    public List<Book> getAll()                       { return repo.findAll(); }
    public List<Book> search(String kw)              { return repo.search(kw); }

    public Book getById(@NonNull Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public Book create(Book b) {
        if (repo.existsByIsbn(b.getIsbn()))
            throw new RuntimeException("ISBN already exists: " + b.getIsbn());
        return repo.save(b);
    }

    public Book update(@NonNull Long id, Book updated) {
        Book b = getById(id);
        b.setTitle(updated.getTitle());
        b.setAuthor(updated.getAuthor());
        b.setIsbn(updated.getIsbn());
        b.setCategory(updated.getCategory());
        b.setPublisher(updated.getPublisher());
        b.setPublishedYear(updated.getPublishedYear());
        b.setTotalCopies(updated.getTotalCopies());
        b.setAvailableCopies(updated.getAvailableCopies());
        b.setDescription(updated.getDescription());
        return repo.save(b);
    }

    public void delete(@NonNull Long id) {
        repo.deleteById(Objects.requireNonNull(id, "ID must not be null"));
    }
}
