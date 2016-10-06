# scala-book-library
A little book library exercise, written in immutable FP style. 

The "lazy" interpreter simply parses a stream of commands and prints responses as output. Commands are only actually executed when the user requests to `show` the Library; only then does a Library object get built up out of the stream of commands we've seen so far. This "accounting ledger" approach lends itself to easy serialization, consistency and potential concurrency (thanks to immutability).

# Usage
This is a small system for managing a personal library. The system is accessible from the command line. A user interacts with it like so:

  $ ./library
	
	Welcome to your library!
	
	> add "The Grapes of Wrath" "John Steinbeck"
	
	Added "The Grapes of Wrath" by John Steinbeck
	
	> add "Of Mice and Men" "John Steinbeck"
	
	Added "Of Mice and Men" by John Steinbeck
	
	> add "Moby Dick" "Herman Melville"
	
	Added "Moby Dick" by Herman Melville
	
	> show all
	
	"The Grapes of Wrath" by John Steinbeck (unread)
	"Of Mice and Men" by John Steinbeck (unread)
	"Moby Dick" by Herman Melville (unread)
	
	> read "Moby Dick"
	
	You've read "Moby Dick!"
	
	> read "Of Mice and Men"
	
	You've read "Of Mice and Men"!
	
	> show all
	
	"The Grapes of Wrath" by John Steinbeck (unread)
	"Of Mice and Men" by John Steinbeck (read)
	"Moby Dick" by Herman Melville (read)
	
	> show unread
	
	"The Grapes of Wrath" by John Steinbeck (unread)
	
	> show all by "John Steinbeck"
	
	"The Grapes of Wrath" by John Steinbeck (unread)
	"Of Mice and Men" by John Steinbeck (read)
	
	> show unread by "John Steinbeck"
	
	"The Grapes of Wrath" by John Steinbeck (unread)
	
	> quit
	
	Bye!
	
	$

--------------------------

As shown above, the program accepts the following commands:

- **add "$title" "$author"**: adds a book to the library with the given title and author. All books are unread by default.
- **read "$title"**: marks a given book as read.
- **show all**: displays all of the books in the library
- **show unread**: display all of the books that are unread
- **show all by "$author"**: shows all of the books in the library by the given author.
- **show unread by "$author"**: shows the unread books in the library by the given author
- **quit**: quits the program.
