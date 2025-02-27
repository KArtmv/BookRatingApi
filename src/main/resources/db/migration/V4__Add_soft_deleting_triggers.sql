create or replace function update_books_on_author_delete()
    returns trigger as
$$
BEGIN
    update book
    set deleted = true
    where isbn in (select isbn
                   from book
                            join book_authors ba on book.isbn = ba.book_isbn
                            join author a on a.id = ba.author_id
                   where a.id = new.id);
    return NEW;
END;
$$ language plpgsql;

create trigger after_author_delete_trigger
    after update of deleted
    on author
    for each row
    when (old.deleted = false and new.deleted = true)
execute function update_books_on_author_delete();

create or replace function update_books_on_publisher_delete()
    returns trigger as
$$
BEGIN
    update book
    set deleted = true
    where publisher_id = new.id
      and new.deleted = true;
    return NEW;
END;
$$ language plpgsql;

create trigger after_publisher_delete_trigger
    after update of deleted
    on publisher
    for each row
    when (old.deleted = false and new.deleted = true)
execute function update_books_on_publisher_delete();

create or replace function update_rating_on_book_delete()
    returns trigger as
$$
BEGIN
    update rating
    set deleted = true
    where book_id = new.id
      and new.deleted = true;
    return NEW;
END;
$$ language plpgsql;

create trigger after_book_delete_trigger
    after update of deleted
    on book
    for each row
    when (old.deleted = false and new.deleted = true)
execute function update_rating_on_book_delete();

create or replace function update_rating_on_user_delete()
    returns trigger as
$$
BEGIN
    update rating
    set deleted = true
    where book_id = new.id
      and new.deleted = true;
    return NEW;
END;
$$ language plpgsql;

create trigger after_user_delete_trigger
    after update of deleted
    on users
    for each row
    when (old.deleted = false and new.deleted = true)
execute function update_rating_on_user_delete();

