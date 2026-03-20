package com.library.controller;

import com.library.entity.Book;
import com.library.entity.BorrowRecord;
import com.library.entity.Member;
import com.library.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

// ═══════════════════════════════════════════════
//  BOOK CONTROLLER  —  /api/books
// ═══════════════════════════════════════════════
@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
class BookController {
    private final BookService svc;

    @GetMapping              public List<Book>            getAll()                                      { return svc.getAll(); }
    @GetMapping("/{id}")     public Book                  getOne(@PathVariable Long id)                 { return svc.getById(id); }
    @GetMapping("/search")   public List<Book>            search(@RequestParam String keyword)          { return svc.search(keyword); }
    @PostMapping             public ResponseEntity<Book>  create(@Valid @RequestBody Book b)            { return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(b)); }
    @PutMapping("/{id}")     public Book                  update(@PathVariable Long id, @Valid @RequestBody Book b) { return svc.update(id, b); }
    @DeleteMapping("/{id}")  public ResponseEntity<String> delete(@PathVariable Long id)               { svc.delete(id); return ResponseEntity.ok("Deleted"); }
}

// ═══════════════════════════════════════════════
//  MEMBER CONTROLLER  —  /api/members
// ═══════════════════════════════════════════════
@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
class MemberController {
    private final MemberService svc;

    @GetMapping              public List<Member>           getAll()                                          { return svc.getAll(); }
    @GetMapping("/{id}")     public Member                 getOne(@PathVariable Long id)                     { return svc.getById(id); }
    @GetMapping("/search")   public List<Member>           search(@RequestParam String keyword)              { return svc.search(keyword); }
    @PostMapping             public ResponseEntity<Member> create(@Valid @RequestBody Member m)              { return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(m)); }
    @PutMapping("/{id}")     public Member                 update(@PathVariable Long id, @Valid @RequestBody Member m) { return svc.update(id, m); }
    @DeleteMapping("/{id}")  public ResponseEntity<String> delete(@PathVariable Long id)                    { svc.delete(id); return ResponseEntity.ok("Deleted"); }
}

// ═══════════════════════════════════════════════
//  BORROW CONTROLLER  —  /api/borrow
// ═══════════════════════════════════════════════
@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
class BorrowController {
    private final BorrowService svc;

    @GetMapping                                   public List<BorrowRecord> getAll()                                           { return svc.getAll(); }
    @PostMapping("/{bookId}/member/{memberId}")    public BorrowRecord       issue(@PathVariable Long bookId, @PathVariable Long memberId) { return svc.borrowBook(bookId, memberId); }
    @PutMapping("/{id}/return")                   public BorrowRecord       returnBook(@PathVariable Long id)                 { return svc.returnBook(id); }
}

// ═══════════════════════════════════════════════
//  DASHBOARD CONTROLLER  —  /api/dashboard/stats
// ═══════════════════════════════════════════════
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins="*")
@RequiredArgsConstructor
class DashboardController {
    private final BookService bookSvc;
    private final MemberService memberSvc;
    private final BorrowService borrowSvc;

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Object> map = new HashMap<>();
        map.put("totalBooks",    bookSvc.getAll().size());
        map.put("totalMembers",  memberSvc.getAll().size());
        map.put("borrowedBooks", borrowSvc.countBorrowed());
        map.put("returnedBooks", borrowSvc.countReturned());
        return map;
    }
}
