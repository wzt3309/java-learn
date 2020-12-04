import groovy.transform.Immutable
import groovy.transform.ImmutableOptions

@Immutable
@ImmutableOptions(knownImmutables = "author")
class Book {
    String title
    Author author
}

class Author {
    String name
    int age
}

