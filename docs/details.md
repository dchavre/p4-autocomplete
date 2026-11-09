# Details for P4 Autocomplete in Fall 2026

All coding aspects of the assignment, including workflow and the classes you use and write, are part of this document.

You should have already read the [README](../README.md) file to get a high-level overview of the project and early engagement points. The details here are needed to explain the classes you're given, the code you'll write, and the implementation details.

## Starter Code and Using Git

**_You should have installed all software (Java, Git, VS Code) before completing this project._** You can find 
the [directions for installation here](https://coursework.cs.duke.edu/201fall26/resources-201/-/blob/main/installingSoftware.md) (including workarounds for submitting without Git if needed).

We'll be using Git and the installation of GitLab at [coursework.cs.duke.edu](https://coursework.cs.duke.edu). All code for classwork will be kept here. Git is software used for version control, and GitLab is an online repository to store code in the cloud using Git.

**[This document details the workflow](https://coursework.cs.duke.edu/201fall26/resources-201/-/blob/main/projectWorkflow.md) for downloading the starter code for the project, updating your code on coursework using Git, and ultimately submitting to Gradescope for autograding.** We recommend that you read and follow the directions carefully this first time working on a project! While coding, we recommend that you periodically (perhaps when completing a method or small section) push your changes.

## Overview: What to Do

To complete the assignment you'll do the following, roughly in the order shown.

1. Run `AutocompleteMain` using `BruteAutoComplete` (as you get in the starter code) to see how the autocomplete application works. 
2. Implement the `compare` method in the `PrefixComparator` class that is used in the `BinarySearchAutocomplete` class. Test (JUnit) with `TestTerm`.
3. Implement two methods in `BinarySearchLibrary`: `firstIndex` and `lastIndex`, both of which will use the `PrefixComparator` you completed in the previous step. Test with `TestBinarySearchLibrary`. **We recommend you try to finish these first three steps early**.
4. Finish implementing `BinarySearchAutocomplete` that implements the `Autocompletor` interface by completing the `topMatches` method. Test with  `firstIndex` and `lastIndex` methods you wrote in the previous step and the code in `BruteAutocomplete.topMatches`  as a model. Test with `TestBinarySearchAutocomplete` and run `AutocompleteMain` using `BinarySearchAutocomplete` (uncomment in lines 21-23 to change the class used).
5. Create and implement a new class `HashListAutocomplete` that implements interface `Autocompletor`. Test by running `AutocompleteMain` using `HashListAutocomplete` (uncomment in lines 21-23 to change the class used).
6. Run benchmarks and answer analysis questions. Submit code, analysis.


## Part 1: Run Autocomplete Main

When you fork and clone the project you'll be able to run the `main` method of `AutocompleteMain`. Doing so will launch a "GUI" (Graphical User Interface) that allows you to select a data file. The data file will determine the set of possible words to be recommended by the autocompleter application, and also includes weights for how common/popular/heavy the words are. Several such files are included along with this project. (Note: you may get an error about _Secure Coding_ in the terminal/console when running the GUI. Just ignore that.)

After you select a file, the GUI will allow you to enter a term. As you type, you should see the most common/heavy words that complete what you have typed so far appearing. For example, if you run `AutocompleteMain` and select the file `words-333333.txt` from the data folder you should see the output below shown in the GUI window for the search query **auto**.  You *will use this same search term*, `auto` to test the other implementations you develop. **Refer back to this visual to see if your new classes and code work.**

<div align="center">
  <img src="../p4-figures/astrachanSearch.png">
</div>

By default, `AutocompleteMain` is using `BruteAutocomplete` to find the correct words/weights (terms) to display. You will write two additional implementations of the `Autocompletor` interface: `BinarySearchAutocomplete` and `HashListAutocomplete`. When you finish one, you can again run `AutocompleteMain` using your new implementation by changing the `AUTOCOMPLETOR_CLASS_NAME` just before the `main` method of `AutocompleteMain`. You should see the same matching terms as when `BruteAutoComplete` is used -- this serves as part of testing your code!


## Part 2: Implement the `compare` method in `PrefixComparator`

A `PrefixComparator` object is obtained by calling `PrefixComparator.getComparator` with an integer argument `r`, the size of the prefix for comparison purposes. The prefix size value is stored in the instance variable `myPrefixSize` as you'll see in the code. This class is used in `BinarySearchAutocomplete`, but not in `BruteAutocomplete`.

You can test your code with the `TestTerm` JUnit class which has several tests for the `PrefixComparator` class.

Much of the framework is given to you in `PrefixComparator.java`. However, you must
add code to the `.compare` method as described below and in the code.

You must use only the first `myPrefixSize` characters of the words stored in `Term` objects `v` and `w` that are passed to `PrefixComparator.compare`. However, if the length of either word is less than `myPrefixSize`, this comparator only compares up ***until the end of the shorter word.*** This means that although `"beeswax"` is greater than `"beekeeper"` when compared lexicographically, i.e., with the natural order for strings, the two words are considered equal using a `PrefixComparator.getComparator(3)` since only the first three characters are compared. More details and examples follow.

#### More details on PrefixComparator

For a `PrefixComparator.getComparator(4)`, `"beeswax"` is greater than `"beekeeper"` since `"bees"` is greater than `"beek"`. But `"bee"` is less than `"beekeeper"` and `"beeswax"` since only the first three characters are compared --- since `"bee"` has only three characters and these three characters are the same. ***The length*** of `"bee"` ***makes it less than*** `"beekeeper"`, just as it is when eight characters are used to compare these words.


***Your code should examine only as many characters as needed to return a value.*** You should examine this minimal number of characters using a loop and calling `.charAt` to examine characters--- you'll need to write your loop and comparisons carefully to ensure that the prefix size is used correctly. See the table below for some examples. Recall that you can subtract characters, so `'a'` - `'b'` is a negative value and `'z'` - `'a'` is a positive value. You can also use `<` and `>` with `char` values.

Here is a reference table for the `PrefixComparator` comparator. 

|r/prefix|v| |w| Note |
|    -   |-|-|-| -    |
|   4  |bee|<|beekeeper|"bee" < "beek"|
|4|bees|>|beek|‘s’ > ‘k’|
|4|bug|>|beeswax|‘u’ > ‘e’|
|4|bees|=|beeswax|"bees" == "bees"|
|3|beekeeper|=|beeswax|"bee" == "bee"|
|3|bee|=|beeswax|"bee" == "bee"|

If you compare (in a loop) the minimal number of characters 
(based on `Term.getWord().length()` values and the
`myPrefixSize` value) and the characters are all the same, you will still need to check
a word length, e.g., in the test of `bee` and `beekeeper` which are equal
when the prefix size is 3, but `bee` is less than `beekeeper` when the prefix size is 4. So after the loop, if all characters compared are the same, using lengths will be necessary. Use the JUnit tests in `TestTerm` class to help ensure your
code is more likely to be correct.

## Part 3: Implement `BinarySearchLibrary`

The class `BinarySearchLibrary` stores static utility methods used in the implementation of the `BinarySearchAutocomplete` class. You will need to implement two methods in particular: `firstIndex` and `lastIndex`. Both are similar to the Java API [`Collections.binarySearch(list, key, c)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html#binarySearch(java.util.List,T,java.util.Comparator)) method. The code you write, in addition to returning an index `dex` such that `c.compare(list.get(dex), key)==0`, also **guarantees to find the first or last such index, respectively.** Thus your methods are "better" than those
in the `java.util` class.

`BinarySearchAutocomplete` will use these methods along with the `PrefixComparator` you already completed to efficiently determine the *range of possible completions of a given prefix of a word typed so far*.

### BinarySearchLibrary firstIndex and lastIndex

You're given code in `BinarySearchLibrary.firstIndexSlow` and a corresponding `lastIndexSlow` that is correct, ***but these do not meet performance requirements***. These slow implementations will be very slow in some situations, e.g., when a list has many equal values according to the given comparator. The code in the slow method  is **O(*N*)** where there are *N* equal values since the code could examine all the values. To meet performance criteria your code should be **O(log *N*)**, more specifically it should only need $`1 + \lceil log_2N \rceil`$ comparisons -- that is, one more than $`log_2N`$ rounded up.

You can see how many comparisons the slow methods make by running `BinaryBenchmark` when you clone the repository. It will print the following where the `sdex` column uses `firstIndexSlow` and the `fdex` column uses the `BinaryBenchmark.firstMatch` that implements an efficient binary search that returns the first index. The code in `BinaryBenchmark.firstMatch` is **exactly** the code you need in `BinarySearchLibrary` and you should copy/paste it into the `firstIndex` method of that class. The reason you're given the code is to match it to the invariant described below so that you can mirror the development in writing `lastIndex` in `BinarySearchLibrary`. 
```
        size    sdex    scomps  fdex    fcomps
    1024        0       644     0       13
    2048        0       1297    0       14
    4096        0       2578    0       15
    8192        0       5139    0       16
   16384        0       10260   0       17
   32768        0       20501   0       18
   65536        0       40982   0       19
  131072        0       81943   0       20
  262144        0       163864  0       21
  524288        0       327705  0       22
 1048576        0       655386  0       23
```

You will find code for binary search in the `BinaryBenchmark.java` file in method `bsearch` that mirrors the standard code for binary search found online: it is $`log_2N`$ but it does *NOT* return the first matching index, so `bsearch` meets the *performance* requirement and returns an index `i` such that `comp.compare(list.get(i), target)==0`. However, it does *not* guarantee to return the first or last such index `i`. **Your task is to adapt this approach (outlined in the explanation below and in code you get with the project in `BinaryBenchmark.firstMatch` (to copy)) so that methods`firstIndex` and `lastIndex` in `BinarySearchLibrary` return the first and last such indices respectively, while maintaining the same performance guarantee.** 


At a high level, binary search is efficient because at each iteration of the `while` loop it reduces the effective search range (`high`-`low`) by a factor of 2, leading to the **O(log *N*)** performance. It is also correct because of the following *loop invariant* - at the start of the loop, the target is always at an index between `low` and `high` (if it is in the list). Your algorithm will need to do this as well. However, the example code shown above `return`s an index as soon as it finds a match. You will need to change this so that your algorithm keeps searching to find the first or last match respectively.


### Implementing method firstIndex (and lastIndex)

In many implementations of binary search, variables `low` and `high` are used to bracket/delimit the range of possible values for the search target -- as is the case in the code shown in `BinaryBenchark.bsearch`. 

### Invariant for `firstIndex`

As seen in `BinaryBenchmark.firstMatch` ***you should initialize the values of `low` and `high` to establish the following loop invariant*** -- an expression that will be true every time the loop test is evaluated, that is both before the loop executes the first time and at the end of every loop iteration.

**(low, high] is interval containing  target, if target is in the list. **

Notice that this is an **open interval on the left**, in particular this means that **`list.get(low)` cannot be equal to target.** To establish this invariant before the loop executes the first time you should consider that ***the open interval `(-1,list.length()-1]` must contain target if it is present since this interval represents every possible index in `list`.*** Since the interval is open on the left, you cannot initialize low to zero since `(0,99]` includes 99, but not zero! So for a 100-element array you'd set `low=-1` and `high=99`, so the interval `(-1,99]` ensures that if target is present, it's either `list.get(0)` or `list.get(1)` or … `list.get(99)`.

After calculating the midpoint (`int mid = (high-low)/2 + low;`), you'll need to ***re-establish the invariant by comparing the target to the middle value.*** In particular, in the while loop, if you determine (conceptually)

`list[mid] < target`

Then you can set **low to mid** since you know that before the loop `(low,high]` was the interval and if `list[mid] < target` then `(mid,high]` still maintains the invariant.

Otherwise, meaning `list[mid] >= target`, then you can set `high` to `mid` since if before the loop `(low,high]` was the interval and `list[mid] >= target`, then `(low,mid]` still maintains the invariant. Note that this is `>=`, but using a Comparator this means the value of the comparator will be greater than or equal to zero. Remember that you won't
write the code `list[mid] < target`, but rather
```
    comp.compare(list.get(mid),target) < 0
```

### Loop Termination

If `low` and `high` differ by 1 in the interval `(low,high]`, and the list is sorted, then `list.get(high)` is target and `high` is the lowest/first index (if the interval isn't empty) since the interval contains a single value. This means you should use a loop guard/test that loops 

`while (low+1 != high)`

since you know that `low <= high` is always true. Thus, when the loop exits, you'll know that `low == high-1` and the interval `(low,high]` is the same as `(high-1,high]` -- **which contains a single value** -- the index whose value is `high`. 

You can determine whether to return -1 (target not present) based on the value of `high` and the value of `list.get(high)`. For example, if `high` is not a valid index, the interval is empty. ***After the loop you'll need to make one more comparison*** of `a.get(high)` with `target` to see if they are equal. ***This loop will be correct and will meet the performance bounds if you develop it using the invariant.***


### Code for `lastIndex`

You should develop a similar invariant and loop for the method `lastIndex` that you'll implement. In this case, the interval you'll consider is `[low,high)`, i.e., **open on the right**. Establish the invariant before the loop, and reason about how to assign to `low` or `high` depending on how the middle value compares to `target`. You'll initialize `low = 0` before the loop since the interval is open on the left. You'll need to think about resetting `low` or `high` based on the `Comparator` value. Since the interval is open on the right, when resetting `high` you'll know the value being searched for, `target`, cannot have index `high`. After the loop, you'll need to test the actual value of the list at index `low`, if that's a valid index, the same way you checked the value at index `high` in the code for `firstIndex`.

### Testing BinarySearchLibrary Methods

You're given two classes to help verify that your methods are correct and meet performance requirements. The JUnit tests in `TestBinarySearchLibrary` can help you verify that your methods return the correct values. The output of running `BinaryBenchmark` can help verify both ***correctness and performance requirements.*** The output was shown above when calling `BinaryBenchmark.firstResults()` in `main`. You can replace the call to `firstMatch` with `BinarySearchLibrary.firstIndex` after implementing that. 

There is a commented out call to `lastResults` in `BinaryBenchmark.main`. This
tests `slowLastIndex` and the `lastIndex` you will write in `BinarySearchLibrary`. The `sdex` and `fdex` columns should be the same,  the number of comparisons will illustrate the logarithmic performance.

### BinarySearchAutocomplete topMatches


The `topMatches` method requires that you return the weightiest `k` matches that match `prefix` that's a parameter to `topMatches` --- note that `k` is a parameter to the method as well -- in order of weight. The calls to `firstIndexOf` and `lastIndexOf` give the first and last indices of `myTerms` that match. The code you write will need to return the `k` greatest `weight` of these in order. If there are fewer than `k` matches, it should just return all of the matches in order. Code in static methods `firstIndexOf` and `lastIndexOf` is written to use the API exported by `BinarySearchLibrary`. You'll see that the `Term[]` parameter to these methods is transformed to a `List<Term>` since that's the type of parameter that must be passed to `BinarySearchLibrary` methods. 

**The code you get when you fork and clone in `firstIndexOf` calls `BinarySearchLibrary.firstIndexSlow`. You should replace that call with `BinarySearchLibrary.firstIndex` when you know that works. Similarly for the method `lastIndexOf`. 

The binary search in the `firstIndex` and `lastIndex` methods are both `O(log N)`. Then, if there are `M` terms that match the prefix, the simple method of finding the `M` matches, copying them to a list, sorting them in reverse weight order, and then choosing the first `k` of them will run in the total of the times given below. Using this approach will thus have complexity/performance of `O(log N + M log M)`. 

|Complexity|Reason|
| ---      |  ---  |
|O(log N)|Call firstIndex and lastIndex|
|O(M log(M))|Sort all M elements that match prefix|
|O(k)|Return list of top k matches|

It's quite possible that `k < M`, and often `k` will be *much* less than `M`. Rather than sorting all `M` entries that match the prefix, you can use a size-limited priority queue **using the same idea and code that's used in** the `topMatches` method from `BruteAutocomplete`. Reference the code there for ideas. ***This is the approach you should implement.*** The code you copy loops over all the `Term` objects in instance variable `myTerms`. **Modify this loop to only examine the `Term` objects whose indexes are between `first` and `last` since those are the only indexes that share `prefix`.**

This should make `topMatches` run in `O(log N + M log k)` time instead of `O(log N + M log M)`. In benchmarking, there may not be a noticeable difference for the data files you're given for small values of `M`, though with larger values of `M` there will be a difference.

|Complexity|Reason|
| ---      |  ---  |
|O(log N)|Call firstIndex and lastIndex|
|O(M log(k))|Keep best k elements in priority queue|
|O(k log(k))|Return list of top k matches, removing one at a time from priority queue|


You're given a JUnit test class `TestBinarySearchAutocomplete` that you should run to verify your methods work correctly. You should also change the code in `AutocompleteMain` to use the `BinarySearchAutocomplete` class -- see the commented out lines as shown below. Then be sure that the output matches what you saw earlier when running the `main` method using `BruteAutocomplete`.

```java
final static String AUTOCOMPLETOR_CLASS_NAME = BRUTE_AUTOCOMPLETE;
//final static String AUTOCOMPLETOR_CLASS_NAME = BINARY_SEARCH_AUTOCOMPLETE;
//final static String AUTOCOMPLETOR_CLASS_NAME = HASHLIST_AUTOCOMPLETE;
```

You'll also see a `Term` object called `dummy` created from the `String` passed to `topMatches`. The weight for the `Term` doesn't matter since only the `String` field of the `Term` is used in `firstIndex` and `lastIndex` calls.

## Part 5: Implement `HashListAutocomplete`

In this part, you must write another implementation of the `Autocompletor` interface, this time from scratch. Unlike `BruteAutocomplete` and `BinarySearchAutocomplete`, this third implementation will be based on the use of a `HashMap` instead of the binary search algorithm. This class will provide an `O(1)` implementation of `topMatches` --- with a tradeoff of requiring more memory.

A skeleton of `HashListAutocomplete` can be found in the `HashListAutocomplete.java` file that `implements` the `Autocompletor` interface.

There are four methods you will need to implement, stubs of which are provided in the starter code: `initialize`, `topMatches`, and `sizeInBytes` (details below).

The class should maintain a `HashMap` of _every possible prefix_ (for each term) (up to the number of characters specified by a constant `MAX_PREFIX` that you should set to 10 as shown. Note, this is the maximal _number
of characters_ in a prefix. The key in the map is a prefix/substring. The value for each prefix key is a weight-sorted list of `Term` objects that share that prefix. The diagram below shows part of such a `HashMap`. Three prefixes are shown---the corresponding values are shown as a weight-sorted list of `Term` objects.

|Prefix|Term Objects|
| --   |    ----    |
|"ch"| ("chat",50), ("chomp",40), ("champ",30), ("chocolate",10)|
|"cho"|("chomp",40), ("chocolate",10)|
|"cha | ("chat",50), ("champ", 30)|


### HashListAutocomplete constructor

You should create a constructor similar to those in the other implementations like `BruteAutocomplete` and `BinarySearchAutocomplete`; look at those for examples. The constructor should check for invalid conditions and throws exceptions in those cases, otherwise it should simply call the `initialize()` method passing `terms` and `weights`.

The declaration of the class and the instance variables you will need to add are shown in the code below. **Be sure to add these, and initialze `myMap` in the constructor.**

```java
public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;
}
```

### HashListAutocomplete initialize method

For each `Term` in `initialize`, use **up to** the first `MAX_PREFIX` substrings as a key in the map the class maintains and uses. For each prefix you'll store the `Term` objects with that prefix in an `ArrayList` that is the corresponding value for the prefix in the map. Note that if `MAX_PREFIX = 10`, then using
`substring(0,MAX_PREFIX)` as well as `substring(0,0)` will ensure your code
has up to 10 substrings -- the zero-length prefix is always included as shown
below.

***After*** all keys and values have been entered into the map, you'll write code to sort every value in the map, that is each `ArrayList` corresponding to each prefix. You must use a `Comparator.comparing(Term::getWeight).reversed()` object to sort so that the list is maintained sorted from high to low by weight, e.g., see below, and sort using this idea for each list associated with a key in the map.

```java
Collections.sort(list, Comparator.comparing(Term::getWeight).reversed());
```

While it is not required, we highly recommend updating `mySize` as you're creating the map. The
instance variable `mySize` is a rough estimate of the number of bytes required to create the HashMap (both the String keys and the Term values in the HashMap). For each string you create **as a key in the map**, you'll need to add to `mySize` the value `BYTES_PER_CHAR * length` as the number of bytes needed (note: if 
the String is already in the map, it's not counted again). For each term you store, each string stored contributes `BYTES_PER_CHAR * length` and each double stored contributes `BYTES_PER_DOUBLE`. If you follow these instructions, then you can ignore the sizeInBytes instructions and just return `mySize`. Ideally you will
create each `Term` once, and store references to that term in the list that's
a value associated with each prefix key.


**_Be sure that you include the empty string as a substring!_** As an example, if you initialize HashListAutocomplete with one Term ("hippopotamus", 40), then the HashMap should be 

|Prefix|Term Objects|
| --   |    ----    |
|""| ("hippopotamus", 40)|
|"h"| ("hippopotamus", 40)|
|"hi"| ("hippopotamus", 40)|
|"hip"| ("hippopotamus", 40)|
|"hipp"| ("hippopotamus", 40)|
|"hippo"| ("hippopotamus", 40)|
|"hippop"| ("hippopotamus", 40)|
|"hippopo"| ("hippopotamus", 40)|
|"hippopot"| ("hippopotamus", 40)|
|"hippopota"| ("hippopotamus", 40)|
|"hippopotam"| ("hippopotamus", 40)|

We stop at "hippopotam" since we take prefixes up to `MAX_PREFIX` length.

### HashListAutocomplete topMatches method

The implementation of `topMatches` can then be done in about five lines of code or fewer. First, check that the `prefix` parameter has at most `MAX_PREFIX` characters, otherwise shorten it by truncating the trailing characters to `MAX_PREFIX` length. 

Then, if `prefix` is in the map, get the corresponding value (a `List` of `Term` objects) and return a sublist of the first `k` entries (or all of the entries if there are fewer than `k`). Otherwise, you should return an empty list. Here's code that can help:

```java
List<Term> all = myMap.get(prefix);
List<Term> list = all.subList(0, Math.min(k, all.size()));
```

### The sizeInBytes method

You'll need to implement the required `sizeInBytes` method. This method should return an estimate of the amount of memory (in bytes) necessary to store all of the keys and values in the `HashMap`. This can be computed once the first time `sizeInBytes` is called (that is, when `mySize == 0`) and stored in the instance variable `mySize`; on subsequent calls it can just return `mySize`. You can see similar examples in the `sizeInBytes` methods of `BruteAutocomplete` and `BinarySearchAutocomplete`.

Your method should account for every `Term` object and every String/key in the map. Use the implementations of `sizeInBytes` in the other `Autocomplete` classes as a model. Each string stored contributes `BYTES_PER_CHAR * length` to the bytes need. Each double stored contributes `BYTES_PER_DOUBLE`. You'll account for every `Term` stored in one of the lists in the map (each consisting of a String and a double) as well as every key (Strings) in the map.

## Analysis Questions and Benchmarking

For a template to use when submitting to Gradescope, [use this .docx](https://coursework.cs.duke.edu/201fall26/p4-autocomplete/-/blob/main/docs/p4-analysis-template.docx).

You'll submit the analysis as a PDF separate from the code in Gradescope. **Be sure to label your pages appropriately for each analysis question.**

**Question 1.** Copy/Paste the results of when you run benchmarking code in `BenchmarkForAutoComplete`. In that class code look at the instance variables for `BenchmarkForAutocomplete` and uncomment the two other implementation names so that `String[] myCompletorNames` has all three Strings: `"BruteAutocomplete"`, `"BinarySearchAutocomplete"`, and `"HashListAutocomplete"` (if you want to benchmark only a subset of these, perhaps because one isn't working, just leave it commented out).

Run `BenchmarkForAutocomplete` three times, once for each of the files you'll see in the Benchmark program: `threeletterwords.txt`, `fourletterwords.txt`, and `alexa.txt`. You can change which file is being used inside of the `doMark` method. **Copy and paste all three results into your analysis**. An example and detailed information about the output is described in the section below.

#### Benchmarking Details

On Professor Astachan's laptop, the first few lines are what's shown below for `data/threeletterwords.txt` (in addition, the `sizeInBytes` for the implementations are shown at the bottom). These numbers are for a file of every three letter word "aaa, "aab", … "zzy", "zzz", not actual words, but 3-character strings. All times are listed in seconds.

- The `init time` data shows how long it took to initialize the different implementations.
- The `search` column shows the prefix being used to search for autocompletions; unlabeled "search" is for an empty string `""` which matches on every term. 
- The `size` column shows how many terms have `search` as a prefix. This is described as `M` in the [details](docs/details.md) document.
- `#match` shows the number of highest weight results being returned by `topMatches`. This is described as `k` in the description of runtimes in the [details](docs/details.md) document.
- The next three columns give the running time in seconds for `topMatches` with the given parameters for the different implementations.


```
init time: 0.004612     for BruteAutocomplete
init time: 0.003348     for BinarySearchAutocomplete
init time: 0.03887      for HashListAutocomplete
search  size    #match  BruteAutoc      BinarySear      HashListAu
        17576   50      0.00191738      0.00306458      0.00001950
        17576   50      0.00039575      0.00198267      0.00000546
a       676     50      0.00034438      0.00014479      0.00000942
a       676     50      0.00035567      0.00015113      0.00000350
b       676     50      0.00016033      0.00011954      0.00000292
```

**Question 2.** Let `N` be the total number of terms, let `M` be the number of terms that prefix-match a given `search` term (the `size` column above), and let `k` be the number of highest weight terms returned by `topMatches` (the `#match` column above). The runtime complexity of `BruteAutocomplete` is `O(N + M log(k))`. The runtime complexity of `BinarySearchAutocomplete` is `O(log(N) + M log(k))`. Yet you should notice (as seen in the example timing above) that `BruteAutocomplete` is similarly efficient or even slightly more efficient than `BinarySearchAutocomplete` on the empty `search` String `""`. Answer the following:
- Explain why this observation (that `BruteAutocomplete` is similarly efficient or even slightly more efficient than `BinarySearchAutocomplete` on the empty `search` String `""`) makes sense given the values of `N` and `M` -- in other words, is this an anomaly based on small clocktimes or is there a reason for it given the values of N, M, and what will happen with BinarySearch when searching for the empty string? (Hint: how many terms match the empty prefix?)


**Question 3.** Run the `BenchmarkForAutocomplete` again using `alexa.txt` but doubling `matchSize` to `100` (`matchSize` is specified in the `runAM` method). Again copy and paste your results. Recall that `matchSize` determines `k`, the number of highest weight terms returned by `topMatches` (the `#match` column above). Do your data support the hypothesis that the dependence of the runtime on `k` is logarithmic for `BruteAutocomplete` and `BinarySearchAutocomplete`?

**Question 4.** Briefly explain why `HashListAutocomplete` is much more efficient in terms of the empirical runtime of `topMatches`, but uses more memory than the other `Autocomplete` implementations.






