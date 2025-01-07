create or replace function update_books_on_author_restore()
    returns trigger as
$$
BEGIN
    update book
    set deleted = false
    where isbn in (select isbn
                   from book
                   join book_authors ba on book.isbn = ba.book_isbn
                   join publisher p on p.id = book.publisher_id
                   where ba.author_id = new.id and p.deleted = false);
    return NEW;
END;
$$ language plpgsql;

create trigger after_author_restore_trigger
    after update of deleted
    on author
    for each row
    when (old.deleted = true and new.deleted = false)
execute function update_books_on_author_restore();

create or replace function update_books_on_publisher_restore()
    returns trigger as
$$
BEGIN
    update book
    set deleted = false
    where publisher_id in (select publisher_id
                           from book
                           join book_authors ba on book.isbn = ba.book_isbn
                           join author a on a.id = ba.author_id
                           where book.publisher_id = new.id and a.deleted = false);
    return NEW;
END;
$$ language plpgsql;

create trigger after_publisher_restore_trigger
    after update of deleted
    on publisher
    for each row
    when (old.deleted = true and new.deleted = false)
execute function update_books_on_publisher_restore();

create or replace function update_rating_on_book_restore()
    returns trigger as
$$
BEGIN
    update rating
    set deleted = false
    where book_id in (select book_id
                                             from rating
                                             join users u on rating.user_id = u.id
                                             join book b on rating.book_id = b.id
                                             where b.id = new.id and u.deleted = false);
    return NEW;
END;
$$ language plpgsql;

create trigger after_book_restore_trigger
    after update of deleted
    on book
    for each row
    when (old.deleted = true and new.deleted = false)
execute function update_rating_on_book_restore();

create or replace function update_rating_on_user_restore()
    returns trigger as
$$
BEGIN
    update rating
    set deleted = false
    where book_id in (select book_id
                      from rating
                      join public.book b on b.id = rating.book_id
                      join public.book_authors ba on b.isbn = ba.book_isbn
                      join public.author a on ba.author_id = a.id
                      join public.publisher p on p.id = b.publisher_id
                      where user_id = new.id and p.deleted = false and a.deleted = false);
    return NEW;
END;
$$ language plpgsql;

create trigger after_user_restore_trigger
    after update of deleted
    on users
    for each row
    when (old.deleted = true and new.deleted = false)
execute function update_rating_on_user_restore();