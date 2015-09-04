

# Simple demo run #
This is just a simple example of how one can use the _Interval_ _Tree_ _Clocks_ library, both in C and JAVA.

## Demo run ##
The image shows a run from ITC witch is divided in sections. Each section represents the state of the system between operations (_fork_, _event_ or _join_) and is labeled with a letter. This letter maps the state of the operations presented in both demo programs.

<a href='http://picasaweb.google.com/lh/photo/07P2CBMlkfauJ651E6eYpQ?feat=embedwebsite'><img src='http://lh3.ggpht.com/_tR0W8QwQsQY/S4ULQBCxDKI/AAAAAAAAAfQ/XW4C9AwOmJc/s800/execFlow.png' /></a>
## Sample code C ##

The initial Stamp must be initialized as a _seed_, after that, the Stamps are modified according to the operation that has been executed.
All operations use pointers to Stamps and return an _int_ value as a result for the success of the operations, meaning that the resulting Stamp, or Stamps, are returned by reference.

```
#include "itc.h"

int main(){
    stamp* seed = itc_seed(seed);
	
    stamp* a = newStamp(); // a
    stamp* b = newStamp();
    stamp* c = newStamp();
	
    itc_fork(seed, a, b); // b
	
    itc_event(a, a); // c
    itc_event(b, b); // c
	
    itc_fork(a, a, c); // d
    itc_event(b, b); // d
	
    itc_event(a, a); // e
    itc_join(b, c, b); // e
	
    itc_fork(b, b, c); // f
	
    itc_join(a, b, a); // g
	
    itc_event(a, a); // h
	
    printStamp(a);
    printStamp(c);	
}
```

## Sample code JAVA ##
When using objects, in JAVA, every new Stamp is defined as a seed, meaning that there is no need to apply any method in order to create the seed Stamp.
There are alternatives (functional style) to these methods, which are defined in the API.

```
import itc.*;

public class teste2 {
    public static void main(String[] args){
        Stamp a = new Stamp(); // a
        Stamp b;
        Stamp c;

        b = a.fork(); // b

        a.event(); // c
        b.event(); // c

        c = a.fork(); // d
        b.event(); // d

        a.event(); // e
        b.join(c); // e

        c = b.fork(); // f

        a.join(b); // g

        a.event(); // h
        
        System.out.println(a.toString());
        System.out.println(c.toString());
    }
}
```