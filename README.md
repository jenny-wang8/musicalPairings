#Instructions:
The input.txt file contains the favorite musical artists of 1000 users from Some Popular Music Review Website. Each line is a list of up to 50 artists, formatted as follows:

Radiohead,Pulp,Morrissey,Delays,Stereophonics,Blur,Suede,Sleeper,The La's,Super Furry Animals,Iggy Pop\n

Band of Horses,Smashing Pumpkins,The Velvet Underground,Radiohead,The Decemberists,Morrissey,Television\n

etc.

Write a program that, using this file as input, produces an output file containing a list of pairs of artists which appear TOGETHER in at least fifty different lists. For example, in the above sample, Radiohead and Morrissey appear together twice, but every other pair appears only once. Your solution should be a cvs, with each row being a pair. For example:

Morrissey,Radiohead\n

Your solution MAY return a best guess, i.e. pairs which appear at least 50 times with high probability, as long as you explain why this tradeoff improves the performance of the algorithm. 

#Thought process and solution:
After reading the instructions, the easiest solution would be to iterate through each list, pair each of them up (make sure you don't duplicate groupings) and count them up.  That's what MusicalPairerBruteForce does but its not super performant.  It took over 2000ms.

The next iteration I came up with was, if the artists have to be together at least 50 times, they should show up at least 50 times.  So, get rid of any artists that don't appear at least 50 times.  What does that give me?  A smaller list to figure out our problem with and smaller lists take less time to process.  (Possibly more performant, yay!).  So now what?  Iterate back through the lists, keeping only those "popular" artists and do what we did with the brute force method (pair up and count).  That's what MusicalPairer does and it took 245ms.  (Woo!)

And the final thought was, why not multi-thread it?  In theory that should speed things up right?  So I decided to thread the processing of the "popular" artists as well as the popular pairing per list.  That's what MusicalPairerThreaded is but it took around 533ms (slower, boo!).  