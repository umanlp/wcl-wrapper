# WCL Wrapper

An *extremely* simple wrapper for the [Word-Class Lattices](http://lcl.uniroma1.it/wcl/) Java API.

## Installation

Clone this repository and install the dependencies by running `make`. Then, run `mvn package` to create `target/wcl-wrapper.jar`.

## Usage

```
$ echo -e 'WCL\tWCL is a classifier.\nhello\tHello, how are you.' | java -jar target/wcl-wrapper.jar -l en -t data/training/wiki_good.EN.html 2>/dev/null
1	true
2	false
```
