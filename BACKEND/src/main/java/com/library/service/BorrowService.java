package com.library.service;

import com.library.entity.*;
import com.library.repository.BorrowRecordRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service @RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository repo;
    private final BookService            bookService;
    private final MemberService          memberService;

    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5.00");

    public List<BorrowRecord> getAll()  { return repo.findAll(); }
    public long countBorrowed()         { return repo.countByStatus(BorrowRecord.Status.Borrowed); }
    public long countReturned()         { return repo.countByStatus(BorrowRecord.Status.Returned); }

    public BorrowRecord borrowBook(@NonNull Long bookId, @NonNull Long memberId) {

        Book   book   = bookService.getById(bookId);
        Member member = memberService.getById(memberId);

        if (book.getAvailableCopies() < 1)
            throw new RuntimeException("No copies available for: " + book.getTitle());
        if (member.getStatus() != Member.Status.Active)
            throw new RuntimeException("Member account is not active");

        // reduce available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.update(bookId, book);

        // build the record manually — avoids @Builder null safety warning
        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setMember(member);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        record.setStatus(BorrowRecord.Status.Borrowed);
        record.setFineAmount(BigDecimal.ZERO);

        return Objects.requireNonNull(repo.save(record), "Failed to save borrow record");
    }

    public BorrowRecord returnBook(@NonNull Long recordId) {

        BorrowRecord r = repo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found: " + recordId));

        if (r.getStatus() == BorrowRecord.Status.Returned)
            throw new RuntimeException("Book already returned");

        LocalDate today = LocalDate.now();
        r.setReturnDate(today);
        r.setStatus(BorrowRecord.Status.Returned);

        // calculate fine if overdue
        if (today.isAfter(r.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(r.getDueDate(), today);
            r.setFineAmount(FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays)));
        }

        // restore available copies
        Book bk = r.getBook();
        bk.setAvailableCopies(bk.getAvailableCopies() + 1);
        bookService.update(bk.getId(), bk);

        return Objects.requireNonNull(repo.save(r), "Failed to save return record");
    }
}
