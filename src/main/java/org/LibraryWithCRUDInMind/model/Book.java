package org.LibraryWithCRUDInMind.model;

/**
 * This is a DTO that mirrors all we have in the database on BOOK table.
 */
public class Book implements Comparable<Book>{
    private long id;
    private String title;
    private String author;

    public Book() {}

    public Book(long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public Book(String title, String author) {
        this(0, title, author);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;

        if (!title.equals(book.title)) return false;
        return author.equals(book.author);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }

    @Override
    public int compareTo(Book o) {
        int valueAfterComparison = o.getTitle().compareTo(title);

        if( valueAfterComparison == 0) {
            return o.getAuthor().compareTo(author);
        }

        return valueAfterComparison;
    }

    public String[] getAttributesAsAnArray() {
        return new String[] {
                String.valueOf(this.getId()), this.getTitle(), this.getAuthor()
        };
    }

    @Override
    public String toString() {
        return "Id=" + id +
                ", Title='" + title + '\'' +
                ", Author='" + author + '\'';
    }
}
